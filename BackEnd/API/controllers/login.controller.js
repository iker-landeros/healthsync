const pool = require('../helpers/mysql-config');
const jwt = require('jsonwebtoken');


/*
    Función que permite autenticar a un usuario con username y password
    Recibe username y password en el body
    Retorna un token, identificador (idUser) y nombre del usuario (username), y el tipo de usuario (userType) si el usuario es correcto
*/
const doLogin = (req, res) => {
    let token = '';
    let result = {};

    const username = req.body.username;
    const password = req.body.password;

    const sql = `SELECT COUNT(*) AS cantidad, name, idUser, userType FROM users WHERE username = ? AND password = SHA2(?, 256)`;
    pool.query(sql, [username, password], (err, results, fields) => {

        if (err) 
            res.json(err);
        if (results[0].cantidad == 1) {
            // Si son correctos, devolver el token
            token = jwt.sign({ username: username }, process.env.KEYPHRASE);
            // Resultado exitoso
            result = { token: token, message: 'ok',
                       idUser: results[0].idUser,
                       username: username,
                       name: results[0].name,
                       userType: results[0].userType }

            res.status(200).json(result);
        } else {
            result = { message: 'Usuario o contraseña inválidos' };
            res.status(401).json(result);
        }
    });
}

module.exports = { doLogin }