const express = require('express');
const router = express.Router();
const {middleware, authorizeRole} = require('../middleware/jwt.middleware');
const { postTicket,
        getAllTickets,
        updateTicketStatus,
        getAreas,
        getExtensions,
        getProblemTypes,
        getDeviceTypes,
        getComponents,
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
router.get('/tickets/components', middleware, authorizeRole(['technician']), getComponents);
router.get('/tickets/statistics', middleware, authorizeRole(['admin']), getTicketsStats);
router.get('/tickets', middleware, authorizeRole(['admin']), getAllTickets);
router.put('/tickets/:id', middleware, authorizeRole(['technician']), updateTicketStatus);
router.get('/tickets/:idTechnician/not-my-tickets/summary', middleware, authorizeRole(['technician']), getAllOtherTickets);
router.get('/tickets/:idTechnician/my-tickets/summary', middleware, authorizeRole(['technician']), getAllMyTickets);
router.get('/tickets/:id', middleware, authorizeRole(['technician','admin']), getTicketDetails);
router.post('/tickets/:id/solved', middleware, authorizeRole(['technician']), postMySolvedTicket);
router.post('/tickets/:id/not-solved', middleware, authorizeRole(['technician']), postMyNotSolvedTicket);



module.exports = router