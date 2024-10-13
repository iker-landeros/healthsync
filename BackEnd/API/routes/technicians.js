const express = require('express');
const router = express.Router();
const {middleware, authorizeRole} = require('../middleware/jwt.middleware');
const { getAllTechnicians,
        updateTechnician,
        postTechnician,
        getTechniciansStats
      } = require('../controllers/technicians.controller');

router.get('/technicians/', middleware, authorizeRole(['admin']), getAllTechnicians);
router.put('/technicians/:id', middleware, authorizeRole(['admin']), updateTechnician);
router.post('/technicians/', middleware, authorizeRole(['admin']), postTechnician);
router.get('/technicians/statistics', middleware, authorizeRole(['admin']), getTechniciansStats);


module.exports = router