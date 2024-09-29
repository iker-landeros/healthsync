const express = require('express');
const cors = require('cors');
const multer = require('multer');
const app = express();
const login = require('./routes/login')
const tickets = require('./routes/tickets')
const technicians = require('./routes/technicians')
const port = process.env.PORT || 3001;

// Para que se puedan enviar evidencias de más de 1MB
app.use(express.json({ limit: '50mb' }));

app.use(cors()); // Para que la api pueda ser consumida por cualquier servidor
app.use(multer().array()); // Para que la api pueda recibir formularios
app.use(express.json()); // Para que la api sepa que puede recibir JSON
app.use('/',login)
app.use('/',tickets)
app.use('/',technicians)


//Función callback
app.listen(port, () => {
    console.log(`Connected to port ${port}`);
    
});