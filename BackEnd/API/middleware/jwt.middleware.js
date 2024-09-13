const jwt = require('jsonwebtoken');
const express = require('express');
const middleware = express.Router();

const verifyJWT = (req, res, next) => {
let token = req.headers['authorization'];

    //Verificar si el token es válido
    // si el token es válido, se ejecuta next()
    // si el token no es válido, se responde con un mensaje de error
    
    if (token) {
        token = token.split(' ')[1];
        jwt.verify(token,process.env.KEYPHRASE, (err, decoded) => {
            if (err) {
                //Si el token no es válido, se responde con un mensaje de error
                res.status(403).json({ mensaje: 'Token inválido' });
            } else {
                //Si el token es válido, se ejecuta next()
                next();
            }
        });
    } else {
        res.status(401).json({ mensaje: 'Token no proporcionado' });
    }
};

middleware.use(verifyJWT);

module.exports = middleware;