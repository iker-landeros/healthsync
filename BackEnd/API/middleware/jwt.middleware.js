const jwt = require('jsonwebtoken');
const express = require('express');
const middleware = express.Router();

const verifyJWT = (req, res, next) => {
    let token = req.headers['authorization'];

    if (token) {
        token = token.split(' ')[1];
        jwt.verify(token, process.env.KEYPHRASE, (err, decoded) => {
            if (err) {
                res.status(403).json({ mensaje: 'Token inválido' });
            } else {
                // Guardar el token decodificado (información del usuario) en req.user
                req.user = decoded;
                next();
            }
        });
    } else {
        res.status(401).json({ mensaje: 'Token no proporcionado' });
    }
};

// Middleware para verificar el rol del usuario
const authorizeRole = (roles) => {
    return (req, res, next) => {
        // Verificar que el userType del token esté en la lista de roles permitidos
        if (!roles.includes(req.user.userType)) {
            return res.status(403).json({ mensaje: 'No tienes permiso para realizar esta acción' });
        }
        next();
    };
};

middleware.use(verifyJWT);  // Verifica el token para todas las rutas

module.exports = { middleware, authorizeRole };