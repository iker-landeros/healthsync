const express = require('express');
const router = express.Router();
const { getAllTechnicians,
        updateTechnician,
        postTechnician,
        getTechniciansStats
      } = require('../controllers/technicians.controller');

router.get('/technicians/', getAllTechnicians);
router.put('/technicians/:id', updateTechnician);
router.post('/technicians/', postTechnician);
router.get('/technicians/statistics', getTechniciansStats);


module.exports = router