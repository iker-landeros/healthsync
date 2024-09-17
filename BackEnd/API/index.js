const express = require('express');
const cors = require('cors');
const multer = require('multer');
const app = express();
const login = require('./routes/login')
const tickets = require('./routes/tickets')
const port = process.env.PORT || 3001;

app.use(cors()); // Para que la api pueda ser consumida por cualquier servidor
app.use(multer().array()); // Para que la api pueda recibir formularios
app.use(express.json()); // Para que la api sepa que puede recibir JSON
app.use('/',login)
app.use('/',tickets)


//Función callback
app.listen(port, () => {
    console.log(`Connected to port ${port}`);
    
});