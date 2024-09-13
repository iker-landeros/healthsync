const pool = require('../helpers/mysql-config')

// Info de todos los cursos
const getCursos = (req, res) => {
    const sql = `SELECT * FROM Cursos`
    pool.query(sql, (err, results, fields) => {
        if (err) res.json(err)
        res.json(results)
    })
}