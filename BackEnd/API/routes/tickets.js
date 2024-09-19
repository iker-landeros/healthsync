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
        getTicketDetails,
        postMySolvedTicket,
        postMyNotSolvedTicket,
        getTicketsStats
      } = require('../controllers/tickets.controller');

router.post('/tickets', postTicket);
router.get('/tickets/areas', getAreas);
router.get('/tickets/extensions', getExtensions);
router.get('/tickets/problemTypes', getProblemTypes);
router.get('/tickets/deviceTypes', getDeviceTypes);
router.get('/tickets/statistics', getTicketsStats);
router.get('/tickets', getAllTickets);
router.put('/tickets/:id', updateTicketStatus);
router.get('/tickets/:idTechnician/not-my-tickets/summary', getAllOtherTickets);
router.get('/tickets/:idTechnician/my-tickets/summary', getAllMyTickets);
router.get('/tickets/:id', getTicketDetails);
router.post('/tickets/:id/solved', postMySolvedTicket);
router.post('/tickets/:id/not-solved', postMyNotSolvedTicket);



module.exports = router