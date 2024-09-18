const express = require('express');
const router = express.Router();
const { postTicket,
        getAllTickets,
        updateTicketStatus,
        getAreas,
        getExtensions,
        getProblemTypes,
        getDeviceTypes,
        getAllOtherTickets,
        getAllMyTickets,
        getTicketDetails
      } = require('../controllers/tickets.controller');

router.post('/tickets', postTicket);
router.get('/tickets/areas', getAreas);
router.get('/tickets/extensions', getExtensions);
router.get('/tickets/problemTypes', getProblemTypes);
router.get('/tickets/deviceTypes', getDeviceTypes);
router.get('/tickets', getAllTickets);
router.put('/tickets/:id', updateTicketStatus);
router.get('/tickets/:idTechnician/not-my-tickets/summary', getAllOtherTickets);
router.get('/tickets/:idTechnician/my-tickets/summary', getAllMyTickets);
router.get('/tickets/:id', getTicketDetails);


module.exports = router