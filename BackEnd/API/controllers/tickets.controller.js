const pool = require('../helpers/mysql-config')
const { get } = require('../middleware/jwt.middleware')

/*
    Función que publica un ticket a la base de datos
    Función para usuarios clientes
    Recibe el nombre del usuario, la extensión de su área, el nombre de su departamento, el tipo de equipo de cómputo, el tipo de problema que presenta y la descripción del problema
*/
const postTicket = (req, res) => {
    const { senderName, idExtension, idArea, idDeviceType, idProblemType, description } = req.body
    const sql = `INSERT INTO tickets (senderName, idExtension, idArea, idDeviceType, idProblemType, description) VALUES (?,?,?,?,?,?)`
    pool.query(sql, [senderName, idExtension, idArea, idDeviceType, idProblemType, description], (err, results, fields) => {
        if (err) res.json(err)
        res.json({ message: 'Ticket creado exitosamente' })
    })
}

/*
    Función que obtiene todos los tickets, sin importar su estado.
    Función para usuarios administradores
*/
const getAllTickets = (req, res) => {
    const sql = `SELECT idTicket, status, dateOpened, idProblemType FROM tickets WHERE idTicket ORDER BY dateOpened DESC`
    pool.query(sql, (err, results, fields) => {
        if (err) res.json(err)
        res.json(results)
    })
}


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
            console.error(err);
            return res.status(500).json({ error: 'Error al actualizar el ticket' });
        }
        if (results.affectedRows > 0) {
            return res.json({ message: 'Estado de ticket actualizado', idTicket: id, status: status });
        } else {
            return res.status(405).json({ message: 'Ticket no encontrado' });
        }
    });
};


module.exports = { 
    postTicket,
    getAllTickets,
    updateTicketStatus
}