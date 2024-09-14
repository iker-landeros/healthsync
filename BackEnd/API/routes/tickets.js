const express = require('express');
const router = express.Router();
const { postTicket,
        getAllTickets,
        updateTicketStatus
      } = require('../controllers/tickets.controller');

router.post('/tickets', postTicket);
router.get('/tickets', getAllTickets);
router.put('/tickets/:id', updateTicketStatus);


module.exports = router