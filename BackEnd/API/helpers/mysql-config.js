const mysql = require('mysql2');
require ('dotenv').config();

let pool = mysql.createPool({
    connectionLimit: 10,
    host: process.env.DBHOST,
    user: process.env.DBUSER,
    password: process.env.DBPASS,
    database: process.env.DBNAME,
    port: process.env.DBPORT,
    dateStrings: true,
    connectTimeout:60000
});

pool.getConnection((err, connection) => {
    if(err){
        throw err;
    }
    console.log('Database connected succesfully!');
    connection.release();
});

module.exports = pool;