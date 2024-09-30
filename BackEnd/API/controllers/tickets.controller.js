const pool = require('../helpers/mysql-config');
const { get } = require('../routes/tickets');

/*
    Función que obtiene todos los datos de la tabla "areas" para el menú desplegable
    Función para usuarios clientes
*/
const getAreas = (req, res) => {
    const sql = `SELECT * FROM areas`;
    
    pool.query(sql, (err, results) => {
        if (err) {
            console.error("Error fetching areas:", err);
            return res.status(500).json({ error: "Error fetching areas" });
        }
        return res.status(200).json(results);
    });
};

/*
    Función que obtiene todos los datos de la tabla "extensions" para el menú desplegable
    Función para usuarios clientes
*/
const getExtensions = (req, res) => {
    const sql = `SELECT * FROM extensions`;
    
    pool.query(sql, (err, results) => {
        if (err) {
            console.error("Error fetching extensions:", err);
            return res.status(500).json({ error: "Error fetching extensions" });
        }
        return res.status(200).json(results);
    });
};

/*
    Función que obtiene todos los datos de la tabla "problemTypes" para el menú desplegable
    Función para usuarios clientes
*/
const getProblemTypes = (req, res) => {
    const sql = `SELECT * FROM problem_types`;
    
    pool.query(sql, (err, results) => {
        if (err) {
            console.error("Error fetching problem types:", err);
            return res.status(500).json({ error: "Error fetching problem types" });
        }
        return res.status(200).json(results);
    });
};

/*
    Función que obtiene todos los datos de la tabla "deviceTypes" para el menú desplegable
    Función para usuarios clientes
*/
const getDeviceTypes = (req, res) => {
    const sql = `SELECT * FROM device_types`;
    
    pool.query(sql, (err, results) => {
        if (err) {
            console.error("Error fetching device types:", err);
            return res.status(500).json({ error: "Error fetching device types" });
        }
        return res.status(200).json(results);
    });
};

/*
    Función que obtiene todos los componentes
    Función para usuarios técnicos
*/
const getComponents = (req, res) => {
    const sql = `SELECT * FROM components`;
    
    pool.query(sql, (err, results) => {
        if (err) {
            console.error("Error fetching components:", err);
            return res.status(500).json({ error: "Error fetching components" });
        }
        return res.status(200).json(results);
    });
};

/*
    Función que publica un ticket a la base de datos
    Función para usuarios clientes
    Recibe el nombre del usuario, la extensión de su área, el nombre de su departamento, el tipo de equipo de cómputo, el tipo de problema que presenta y la descripción del problema
*/
const postTicket = (req, res) => {
    const { senderName, idExtension, idDeviceType, idProblemType, description } = req.body;
    const sql = `INSERT INTO tickets (senderName, idExtension, idDeviceType, idProblemType, description) VALUES (?,?,?,?,?)`;
    
    pool.query(sql, [senderName, idExtension, idDeviceType, idProblemType, description], (err, results) => {
        if (err) {
            console.error("Error inserting ticket:", err);
            return res.status(500).json({ error: "Error creating the ticket" });  // Ensure the response is returned
        }
        return res.status(201).json({ message: 'Ticket creado exitosamente' });
    });
};

/*
    Función que obtiene todos los tickets, sin importar su estado.
    Función para usuarios administradores
*/
const getAllTickets = (req, res) => {
    const sql = `SELECT * FROM tickets t 
	                JOIN problem_types p ON t.idProblemType = p.idProblemType
                ORDER BY dateOpened DESC`;  // Fixed WHERE clause (it was incomplete)
    
    pool.query(sql, (err, results) => {
        if (err) {
            console.error("Error fetching tickets:", err);
            return res.status(500).json({ error: "Error fetching tickets" });
        }
        return res.status(200).json(results);  // Return the response
    });
};

/*
    Función que cambia el estado de un ticket
    Función para usuarios técnicos
*/
const updateTicketStatus = (req, res) => {
    const { status, idTechnician } = req.body;
    const { id } = req.params;

    const sql = `UPDATE tickets SET status = ?, idTechnician = ? WHERE idTicket = ?`;
    
    pool.query(sql, [status, idTechnician, id], (err, results) => {
        if (err) {
            console.error("Error updating ticket:", err);
            return res.status(500).json({ error: 'Error al actualizar el ticket' });
        }

        if (results.affectedRows > 0) {
            return res.status(200).json({ message: 'Estado de ticket actualizado', idTicket: id, status: status });
        } else {
            return res.status(404).json({ message: 'Ticket no encontrado' });
        }
    });
};

/*
    Función para dar el resumen de todos los tickets no propios
    Función para usuarios técnicos
*/
const getAllOtherTickets = (req, res) => {
    const { idTechnician } = req.params;

    const sql = `SELECT t.idTicket, t.status, t.dateOpened, p.problemName FROM tickets t
	JOIN problem_types p ON t.idProblemType = p.idProblemType
WHERE (t.idTechnician != ? OR t.idTechnician IS NULL) 
	AND (t.status = 'Sin empezar' OR t.status = 'En progreso')
ORDER BY 
	CASE
		WHEN t.status = 'Not started' THEN 1 
		WHEN t.status = 'In progress' THEN 2 
	END,
t.dateOpened ASC`; 
    
    pool.query(sql, [idTechnician], (err, results) => {
        if (err) {
            console.error("No autorizado:", err);
            return res.status(401).json({ error: "No autorizado" });
        }

        res.status(200).json(results);
    });
};

/*
    Función para obtener todos los tickets que tiene asignados un técnico
    Función para usuarios técnicos
*/
const getAllMyTickets = (req, res) => {
    const { idTechnician } = req.params;

    const sql = `SELECT t.idTicket, t.status, t.dateOpened, p.problemName FROM tickets t
	                JOIN problem_types p ON t.idProblemType = p.idProblemType
                WHERE t.idTechnician = ? AND t.status = 'En progreso'
                ORDER BY t.dateOpened ASC`; 
    
    pool.query(sql, [idTechnician], (err, results) => {
        if (err) {
            console.error("No autorizado:", err);
            return res.status(401).json({ error: "No autorizado" });
        }
        res.status(200).json(results);
    });
};

/*
    Función para obtener todos los datos de un ticket en particular
    Función para usuarios técnicos
*/
const getTicketDetails = (req, res) => {

    const { id } = req.params;
    const sql = `SELECT t.idTicket,
	                t.status,
                    t.senderName,
                    t.dateOpened,
                    AVG(TIMESTAMPDIFF(MINUTE, t.dateOpened, COALESCE(t.dateClosed, CURRENT_TIMESTAMP)))/60 AS resolutionTimeHours,
                    a.areaName,
                    e.extensionNumber,
                    dt.deviceName,
                    p.problemName,
                    u.name as technicianName,
                    t.revisionProcess,
                    t.diagnosis,
                    t.solutionProcess,
                    t.failedReason,
                    i.imageData as ticketImage,
                    GROUP_CONCAT(c.componentName) AS componentNames,
                    GROUP_CONCAT(tc.quantity) AS componentQuantities
	            FROM tickets t
                    LEFT JOIN problem_types p ON t.idProblemType = p.idProblemType
                    LEFT JOIN extensions e ON t.idExtension = e.idExtension
                    LEFT JOIN areas a ON e.idArea = a.idArea
                    LEFT JOIN device_types dt ON t.idDeviceType = dt.idDeviceType
                    LEFT JOIN users u ON t.idTechnician = u.idUser
                    LEFT JOIN images i ON t.idTicket = i.idTicket
                    LEFT JOIN ticket_components tc ON t.idTicket = tc.idTicket
                    LEFT JOIN components c ON tc.idComponent = c.idComponent
                WHERE t.idTicket = ?`;
    
    pool.query(sql, [id], (err, results) => {
        if (err) {
            console.error("Error fetching ticket:", err);
            return res.status(500).json({ error: "Error fetching ticket" });
        }

        const componentNames = results[0].componentNames ? results[0].componentNames.split(',') : [];
        const componentQuantities = results[0].componentQuantities ? results[0].componentQuantities.split(',') : [];

        // Crear un array de objetos para los componentes
        const components = componentNames.map((name, index) => ({
            name: name,
            quantity: componentQuantities[index]
        }));

        // Eliminar las propiedades concatenadas
        delete results[0].componentNames;
        delete results[0].componentQuantities;

        // Agregar los componentes en forma de array
        results[0].components = components;

        return res.status(200).json(results);
    });
}

/*
    Función para resolver un ticket
    Función para usuarios técnicos
*/
const postMySolvedTicket = (req, res) => {
    const { id } = req.params;
    const { revisionProcess, diagnosis, solutionProcess, imageData, components } = req.body; // imageData viene en base64 y components es un arreglo de objetos

    const decodedImageData = Buffer.from(imageData, 'base64');
    const componentsJSON = JSON.stringify(components);

    const sql = `CALL markTicketAsSolved(?, ?, ?, ?, ?, ?);`;

    pool.query(sql, [id, revisionProcess, diagnosis, solutionProcess, decodedImageData, componentsJSON], (err, results) => {
        if (err) {
            console.error("Error executing stored procedure:", err);
            return res.status(500).json({ error: 'Error al ejecutar el procedimiento almacenado' });
        }

        return res.status(200).json({ message: 'Ticket no resuelto y evidencia guardada', idTicket: id });
    });
};

/*
    Función para publicar un ticket no resuelto
    Función para usuarios técnicos
*/
const postMyNotSolvedTicket = (req, res) => {
    const { id } = req.params;
    const { failedReason, imageData } = req.body; // imageData viene en base64

    const decodedImageData = Buffer.from(imageData, 'base64');

    const sql = `CALL markTicketAsNotSolved(?, ?, ?);`;

    pool.query(sql, [id, failedReason, decodedImageData], (err, results) => {
        if (err) {
            console.error("Error executing stored procedure:", err);
            return res.status(500).json({ error: 'Error al ejecutar el procedimiento almacenado' });
        }

        return res.status(200).json({ message: 'Ticket no resuelto y evidencia guardada', idTicket: id });
    });
};

/*
    Función para obtener las estadísticas de los tickets
*/
const getTicketsStats = (req, res) => {
    const sql = `SELECT 
	                COUNT(idTicket) AS totalTickets,
	                COUNT(CASE WHEN status = 'Sin empezar' THEN 1 END) AS unstartedTickets,
                    COUNT(CASE WHEN status = 'En progreso' THEN 1 END) AS progressingTickets,
	                COUNT(CASE WHEN status = 'No resuelto' THEN 1 END) AS notSolvedTickets,
	                COUNT(CASE WHEN status = 'Resuelto' THEN 1 END) AS solvedTickets,
                    COUNT(CASE WHEN status = 'Eliminado' THEN 1 END) AS eliminatedTickets
                FROM tickets;`;
    
    pool.query(sql, (err, results) => {
        if (err) {
            console.error("Error fetching statistics:", err);
            return res.status(500).json({ error: "Error fetching statistics" });
        }
        return res.status(200).json(results);
    });
};



module.exports = { 
    getAreas,
    getExtensions,
    getProblemTypes,
    getDeviceTypes,
    getComponents,
    postTicket,
    getAllTickets,
    updateTicketStatus,
    getAllOtherTickets,
    getAllMyTickets,
    getTicketDetails,
    postMySolvedTicket,
    postMyNotSolvedTicket,
    getTicketsStats
};