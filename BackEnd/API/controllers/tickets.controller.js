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
    Función que publica un ticket a la base de datos
    Función para usuarios clientes
    Recibe el nombre del usuario, la extensión de su área, el nombre de su departamento, el tipo de equipo de cómputo, el tipo de problema que presenta y la descripción del problema
*/
const postTicket = (req, res) => {
    const { senderName, idExtension, idArea, idDeviceType, idProblemType, description } = req.body;
    const sql = `INSERT INTO tickets (senderName, idExtension, idArea, idDeviceType, idProblemType, description) VALUES (?,?,?,?,?,?)`;
    
    pool.query(sql, [senderName, idExtension, idArea, idDeviceType, idProblemType, description], (err, results) => {
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
    const sql = `SELECT * FROM tickets ORDER BY dateOpened DESC`;  // Fixed WHERE clause (it was incomplete)
    
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

    const sql = `SELECT * FROM tickets
                 WHERE (idTechnician != ? OR idTechnician IS NULL) 
                    AND (status = 'Not started' OR status = 'In progress')
                 ORDER BY 
                    CASE
                        WHEN status = 'Not started' THEN 1 
                        WHEN status = 'In progress' THEN 2 
                    END,
                 dateOpened DESC;`; 
    
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

    const sql = `SELECT * FROM tickets
                 WHERE idTechnician = ? AND status = 'In progress'
                 ORDER BY dateOpened DESC;`; 
    
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
    const sql = `SELECT * FROM tickets WHERE idTicket = ?`;
    
    pool.query(sql, [id], (err, results) => {
        if (err) {
            console.error("Error fetching ticket:", err);
            return res.status(500).json({ error: "Error fetching ticket" });
        }
        return res.status(200).json(results);
    });
}

/*
    Función para resolver un ticket
    Función para usuarios técnicos
*/
const postMySolvedTicket = (req, res) => {
    const {id} = req.params;
    const {revisionProcess, diagnosis, solutionProcess} = req.body; 

    sql = `UPDATE tickets SET status = "Resuelto", dateClosed = CURRENT_TIMESTAMP, revisionProcess = ?, diagnosis = ?, solutionProcess = ? WHERE idTicket = ?;`;

    pool.query(sql, [revisionProcess, diagnosis, solutionProcess, id], (err, results) => {
        if (err) {
            console.error("Error updating ticket:", err);
            return res.status(500).json({ error: 'Error al actualizar el ticket' });
        }

        if (results.affectedRows > 0) {
            return res.status(200).json({ message: 'Ticket resuelto', idTicket: id });
        } else {
            return res.status(404).json({ message: 'No se encontraron tickets' });
        }
    });
}

/*
    Función para publicar un ticket no resuelto
    Función para usuarios técnicos
*/
const postMyNotSolvedTicket = (req, res) => {
    const {id} = req.params;
    const {failedReason} = req.body; 

    sql = `UPDATE tickets SET status = "No resuelto", dateClosed = CURRENT_TIMESTAMP, failedReason = ? WHERE idTicket = ?;`;

    pool.query(sql, [failedReason, id], (err, results) => {
        if (err) {
            console.error("Error updating ticket:", err);
            return res.status(500).json({ error: 'Error al actualizar el ticket' });
        }

        if (results.affectedRows > 0) {
            return res.status(200).json({ message: 'Ticket no resuelto', idTicket: id });
        } else {
            return res.status(404).json({ message: 'No se encontraron tickets' });
        }
    });
}

/*
    Función para obtener las estadísticas de los tickets
*/
const getTicketsStats = (req, res) => {
    const sql = `SELECT 
                    COUNT(idTicket) AS totalTickets,
                    COUNT(CASE WHEN status = 'En progreso' THEN 1 END) AS ticketsInProgress,
                    COUNT(CASE WHEN status = 'No resuelto' THEN 1 END) AS notSolvedTickets,
                    COUNT(CASE WHEN status = 'Resuelto' THEN 1 END) AS solvedTickets
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