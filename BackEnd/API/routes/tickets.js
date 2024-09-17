const express = require('express');
const router = express.Router();
const { postTicket,
        getAllTickets,
        updateTicketStatus,
        getAreas,
        getExtensions,
        getProblemTypes,
        getDeviceTypes
      } = require('../controllers/tickets.controller');

router.post('/tickets', postTicket);
router.get('/tickets/areas', getAreas);
router.get('/tickets/extensions', getExtensions);
router.get('/tickets/problemTypes', getProblemTypes);
router.get('/tickets/deviceTypes', getDeviceTypes);
router.get('/tickets', getAllTickets);
router.put('/tickets/:id', updateTicketStatus);


module.exports = router