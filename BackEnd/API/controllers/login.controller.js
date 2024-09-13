const pool = require('../helpers/mysql-config');
const jwt = require('jsonwebtoken');

const doLogin = (req, res) => {
    let token = '';
    let result = {};

    const username = req.body.username;
    const password = req.body.password;

    const sql = `SELECT COUNT(*) AS cantidad FROM users WHERE username = ? AND password = SHA2(?, 256)`;
    pool.query(sql, [username, password], (err, results, fields) => {

        if (err) 
            res.json(err);
        if (results[0].cantidad == 1) {
            // Si son correctos, devolver el token
            token = jwt.sign({ username: username }, process.env.KEYPHRASE);
            result = { token: token, mensaje: 'Usuario autenticado correctamente' }
        } else {
            result = { mensaje: 'Usuario o contraseña incorrectos' }
        }
        res.json(result);
        
    });
}

module.exports = { doLogin }