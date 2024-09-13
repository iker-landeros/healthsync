const pool = require('../helpers/mysql-config');
const jwt = require('jsonwebtoken');

const doLogin = (req, res) => {
    let token = '';
    let result = {};

    const username = req.body.username;
    const password = req.body.password;

    const sql = `SELECT COUNT(*) AS cantidad, userType FROM users WHERE username = ? AND password = SHA2(?, 256)`;
    pool.query(sql, [username, password], (err, results, fields) => {

        if (err) 
            res.json(err);
        if (results[0].cantidad == 1) {
            // Si son correctos, devolver el token
            token = jwt.sign({ username: username }, process.env.KEYPHRASE);
            // Resultado exitoso
            result = { token: token, message: 'Usuario autenticado correctamente', userType: results[0].userType }
        } else {
            result = { message: 'Usuario o contraseña incorrectos' }
        }
        res.json(result);
        
    });
}

module.exports = { doLogin }