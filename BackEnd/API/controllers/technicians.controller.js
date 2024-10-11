const pool = require('../helpers/mysql-config');
const { get } = require('../routes/technicians');

/*
    Función que obtiene los datos de todos los usuarios técnicos
    Función para usuarios administradores
*/
const getAllTechnicians = (req, res) => {

    sql = `SELECT * FROM users WHERE userType = 'technician'`;

    pool.query(sql, (err, results) => {
        if (err) {
            console.error("Error fetching technicians:", err);
            return res.status(500).json({ error: "Error fetching technicians" });
        }
        return res.status(200).json(results);
    });
};

/*
    Función para editar los datos de un usuario técnico
    Función para usuarios administradores
*/
const updateTechnician = (req, res) => {
    const { id } = req.params;
    const { name, username, password } = req.body;

    let sql = `UPDATE users SET name = ?, username = ? WHERE idUser = ?`;
    let values = [name, username, id];

    if (password) {
        sql = `UPDATE users SET name = ?, username = ?, password = SHA2(?, 256) WHERE idUser = ?`;
        values = [name, username, password, id];
    }

    pool.query(sql, values, (err, results) => {
        if (err) {
            console.error("Error updating technician:", err);
            return res.status(500).json({ error: "Error updating technician" });
        }
        return res.status(200).json(results);
    });
};


/*
    Función para crear un usuario técnico
    Función para usuarios administradores
*/

const postTechnician = (req, res) => {
    const { name, username, password } = req.body;

    sql = `INSERT INTO users (name, username, password, userType) VALUES (?, ?, SHA2(?, 256), 'technician')`;

    pool.query(sql, [name, username, password], (err, results) => {
        if (err) {
            console.error("Error creating technician:", err);
            return res.status(500).json({ error: "Error creating technician" });
        }
        return res.status(200).json(results);
    });
};

/*
    Función para obtener las estadísticas de todos los técnicos
    Función para usuarios administradores
*/
const getTechniciansStats = (req, res) => {
    sql = `SELECT 
                u.idUser, 
                u.name, 
                COUNT(t.idTicket) AS resolvedTicketsCount,
                AVG(TIMESTAMPDIFF(HOUR, t.dateOpened, t.dateClosed))/60 AS avgResolutionTimeHours
            FROM users u
            LEFT JOIN tickets t ON u.idUser = t.idTechnician
            WHERE u.userType = 'technician' 
                AND t.status = 'Resuelto'
            GROUP BY u.idUser, u.name;`;

    pool.query(sql, (err, results) => {
        if (err) {
            console.error("Error fetching technicians stats:", err);
            return res.status(500).json({ error: "Error fetching technicians stats" });
        }
        return res.status(200).json(results);
    });
}



module.exports = { 
    getAllTechnicians,
    updateTechnician,
    postTechnician,
    getTechniciansStats
};