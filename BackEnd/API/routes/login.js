const express = require('express');
const router = express.Router();
const {doLogin} = require('../controllers/login.controller')

router.post('/login', doLogin);

module.exports = router