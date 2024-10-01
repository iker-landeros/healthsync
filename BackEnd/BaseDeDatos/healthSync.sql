-- Create the Database
CREATE DATABASE healthSync;
USE healthSync;
-- DROP DATABASE healthSync;
SHOW DATABASES;

-- Table for Users (Technicians and Admins)
CREATE TABLE users (
  idUser INT AUTO_INCREMENT,
  name VARCHAR(100),
  username VARCHAR(45) UNIQUE,
  password VARCHAR(64),
  userType ENUM('technician', 'admin') NOT NULL,
  PRIMARY KEY (idUser)
);

-- Table for Areas (independent table for area of the hospital)
CREATE TABLE areas (
  idArea INT AUTO_INCREMENT,
  areaName VARCHAR(100),
  PRIMARY KEY (idArea)
);

-- Table for Extensions (independent table for phone extensions)
CREATE TABLE extensions (
  idExtension INT AUTO_INCREMENT,
  extensionNumber VARCHAR(10),
  idArea INT,
  PRIMARY KEY (idExtension),
  
  CONSTRAINT fk_extension_area
    FOREIGN KEY (idArea)
    REFERENCES areas (idArea)
);

-- Table for Problem Types (independent table for types of problems)
CREATE TABLE problem_types (
  idProblemType INT AUTO_INCREMENT,
  problemName VARCHAR(100),
  PRIMARY KEY (idProblemType)
);

-- Table for Device Types (independent table for types of devices)
CREATE TABLE device_types (
  idDeviceType INT AUTO_INCREMENT,
  deviceName VARCHAR(45),
  PRIMARY KEY (idDeviceType)
);

-- Table for Tickets
CREATE TABLE tickets (
  idTicket INT AUTO_INCREMENT,
  senderName VARCHAR(100),
  idExtension INT,
  description TEXT,   
  status ENUM('Sin empezar', 'En progreso', 'Resuelto', 'No resuelto', 'Eliminado') DEFAULT 'Sin empezar',
  dateOpened DATETIME DEFAULT CURRENT_TIMESTAMP,
  dateClosed DATETIME DEFAULT NULL,         
  idProblemType INT, 
  idDeviceType INT,  
  idTechnician INT DEFAULT NULL,
  revisionProcess TEXT DEFAULT NULL, 
  diagnosis TEXT DEFAULT NULL, 
  solutionProcess TEXT DEFAULT NULL, 
  failedReason TEXT DEFAULT NULL,  
  PRIMARY KEY (idTicket),

  CONSTRAINT fk_ticket_extension
    FOREIGN KEY (idExtension)
    REFERENCES extensions (idExtension)
    ON DELETE SET NULL,

  CONSTRAINT fk_ticket_problem_type
    FOREIGN KEY (idProblemType)
    REFERENCES problem_types (idProblemType)
    ON DELETE SET NULL,

  CONSTRAINT fk_ticket_device_type
    FOREIGN KEY (idDeviceType)
    REFERENCES device_types (idDeviceType)
    ON DELETE SET NULL,

  CONSTRAINT fk_ticket_technician
    FOREIGN KEY (idTechnician)
    REFERENCES users (idUser)
    ON DELETE SET NULL
);

-- Table for Components (components used for repairs)
CREATE TABLE components (
  idComponent INT AUTO_INCREMENT,
  componentName VARCHAR(100),
  PRIMARY KEY (idComponent)
);

-- Table for the Many-to-Many Relationship Between Tickets and Components
CREATE TABLE ticket_components (
  idTicketComponent INT AUTO_INCREMENT,
  idTicket INT,
  idComponent INT,
  quantity INT DEFAULT 1,
  PRIMARY KEY (idTicketComponent),

  CONSTRAINT fk_ticket_component_ticket
    FOREIGN KEY (idTicket)
    REFERENCES tickets (idTicket)
    ON DELETE CASCADE,

  CONSTRAINT fk_ticket_component_component
    FOREIGN KEY (idComponent)
    REFERENCES components (idComponent)
    ON DELETE CASCADE
);

-- Table for Images (images related to the tickets)
CREATE TABLE images (
  idImage INT AUTO_INCREMENT,
  imageData LONGBLOB,
  idTicket INT,
  PRIMARY KEY (idImage),

  CONSTRAINT fk_image_ticket
    FOREIGN KEY (idTicket)
    REFERENCES tickets (idTicket)
    ON DELETE CASCADE
);

-- Insert dummy data into Users table
INSERT INTO users (name, username, password, userType) VALUES
('Juan Perez', 'jperez', SHA2('passwordJuan', 256), 'technician'),
('Diego Sahid', 'dsahid', SHA2('passwordSahid', 256), 'technician'),
('Antonio Marban', 'tmarban', SHA2('passwordTony', 256), 'technician'),
('Hugo Alejandres', 'halejandres', SHA2('passwordHugo', 256), 'technician'),
('Beatriz Amado', 'bamado', SHA2('adminpass', 256), 'admin');

-- Insert data into Areas table
INSERT INTO areas (areaName) VALUES
('Administración de Personal'),
('Calidad y Mejora Continua'),
('Cirugía y Anestesia'),
('Comisaría'),
('Conservación y Mantenimiento de Infraestructura'),
('Consulta Externa'),
('Descentralizado'),
('Dirección Operativa'),
('Enfermería'),
('Enseñanza de Enfermería'),
('Enseñanza e Investigación'),
('Finanzas y Contabilidad'),
('Hospitalización'),
('Laboratorio'),
('Medicina Crítica y Urgencias'),
('Oficina de Subdirección'),
('Programas Federales (IMSS-BIENESTAR)'),
('Programas Federales (INSABI)'),
('Recursos Materiales y Control Patrimonial'),
('Servicio Jurídico'),
('Servicios Auxiliares y de Diagnostico'),
('Servicios Generales'),
('Servicios Juridicos'),
('Sindicato'),
('Subdirección de Seguridad del Paciente'),
('Subdirección de Seguridad Hospitalaria'),
('Supervisión de Enfermería'),
('Tecnologías de la Información'),
('Trabajo Social'),
('Voluntariado'),
('Otro');

-- Insert data into Extensions table
INSERT INTO extensions (extensionNumber, idArea) VALUES
('1300', 1),
('4580', 1),
('1270', 1),
('1290', 1),
('4580', 1),
('4570', 1),
('3220', 1),
('1270', 1),
('1330', 1),
('1310', 1),
('1310', 1),
('4580', 1),
('4580', 1),
('1310', 1),
('2470', 2),
('4240', 2),
('8070', 3),
('8060', 3),
('8120', 3),
('8310', 3),
('8320', 3),
('8340', 3),
('8350', 3),
('8360', 3),
('8210', 3),
('8140', 3),
('8540', 3),
('6070', 3),
('8190', 3),
('4310', 4),
('4320', 4),
('8080', 5),
('8090', 5),
('2370', 5),
('2050', 5),
('8530', 5),
('2390', 5),
('2050', 5),
('2050', 5),
('8150', 6),
('2420', 6),
('2450', 6),
('2410', 6),
('2720', 6),
('2730', 6),
('2740', 6),
('2750', 6),
('2760', 6),
('2770', 6),
('2780', 6),
('2830', 6),
('2840', 6),
('2850', 6),
('2870', 6),
('2810', 6),
('2560', 6),
('2570', 6),
('2580', 6),
('2590', 6),
('2600', 6),
('2610', 6),
('2620', 6),
('2630', 6),
('2640', 6),
('2650', 6),
('2660', 6),
('2670', 6),
('2690', 6),
('2700', 6),
('2540', 6),
('2530', 6),
('2550', 6),
('2520', 6),
('2510', 6),
('2500', 6),
('2490', 6),
('2110', 6),
('2040', 6),
('2030', 6),
('2010', 6),
('2020', 6),
('2120', 6),
('2150', 6),
('2400', 6),
('1090', 6),
('2140', 6),
('2140', 6),
('2130', 6),
('2890', 6),
('3000', 7),
('2320', 7),
('1280', 7),
('8000', 7),
('7040', 7),
('1200', 8),
('2430', 8),
('2430', 8),
('2430', 8),
('2430', 8),
('2430', 8),
('4070', 9),
('2260', 9),
('3060', 9),
('2090', 9),
('2480', 9),
('1250', 9),
('3030', 9),
('4080', 9),
('3070', 9),
('1150', 9),
('8630', 9),
('2860', 9),
('3010', 9),
('7120', 9),
('7090', 9),
('6050', 9),
('8300', 9),
('8550', 9),
('8590', 9),
('8600', 9),
('8620', 9),
('8110', 9),
('5010', 9),
('1160', 9),
('2260', 9),
('1180', 10),
('1170', 10),
('2380', 11),
('5060', 11),
('3090', 11),
('3100', 11),
('2310', 11),
('2340', 11),
('2340', 11),
('4050', 12),
('6130', 12),
('2440', 12),
('5030', 12),
('4010', 12),
('4020', 12),
('4010', 12),
('4020', 12),
('4040', 12),
('4010', 12),
('4010', 12),
('8230', 13),
('8160', 13),
('3130', 13),
('3160', 13),
('2060', 13),
('2820', 13),
('3280', 13),
('1240', 14),
('5070', 14),
('3040', 14),
('1350', 14),
('5050', 14),
('3080', 14),
('1370', 14),
('5080', 14),
('1140', 14),
('1140', 14),
('8520', 15),
('6020', 15),
('7080', 15),
('7110', 15),
('7140', 15),
('8560', 15),
('8570', 15),
('5120', 16),
('5210', 17),
('4000', 17),
('5220', 17),
('1320', 17),
('2250', 18),
('2290', 19),
('4450', 19),
('2270', 19),
('2200', 19),
('2220', 19),
('2210', 19),
('2230', 19),
('4470', 19),
('4610', 19),
('8640', 19),
('2100', 19),
('4600', 19),
('2280', 19),
('4620', 19),
('8580', 20),
('7050', 21),
('4210', 21),
('4230', 21),
('7150', 21),
('8030', 21),
('8220', 21),
('8050', 21),
('7100', 21),
('7100', 21),
('8200', 21),
('8040', 21),
('4590', 21),
('7210', 21),
('2300', 22),
('1050', 22),
('7020', 22),
('8290', 22),
('8180', 22),
('6000', 22),
('8170', 22),
('2350', 22),
('5020', 22),
('2800', 22),
('2900', 22),
('1120', 23),
('1100', 23),
('1080', 23),
('1130', 23),
('1130', 23),
('1210', 23),
('1210', 23),
('5130', 24),
('5110', 24),
('5120', 25),
('4240', 26),
('8130', 27),
('7130', 27),
('7230', 27),
('4260', 28),
('4550', 28),
('4560', 28),
('2160', 28),
('4540', 28),
('2160', 28),
('4280', 28),
('4530', 28),
('4030', 28),
('2680', 28),
('4060', 28),
('4060', 28),
('4520', 28),
('1220', 29),
('2180', 29),
('2190', 29),
('5040', 29),
('2240', 29),
('3310', 29),
('6140', 29),
('8020', 29),
('3290', 29),
('3050', 30),
('3020', 30),
('2170', 30);

-- Insert dummy data into Problem Types table
INSERT INTO problem_types (problemName) VALUES
('Revisión de equipo'),
('Alta de correo electrónico'),
('Solicitud de capacitación'),
('Bloqueo de página web'),
('Aplicación no funciona'),
('Problema de red'),
('Equipo no prende'),
('Otro');

-- Insert dummy data into Device Types table
INSERT INTO device_types (deviceName) VALUES
('PC'),
('PC / Todo en uno'),
('Laptop'),
('Terminal'),
('Impresora'),
('Escáner'),
('Multifuncional'),
('Otro');

-- Insert dummy data into Components table
INSERT INTO components (componentName) VALUES
('Batería'),
('Monitor de computadora'),
('Procesador'),
('Cartucho de impresora'),
('Memoria RAM'),
('Disco duro'),
('Teclado'),
('Mouse'),
('Cable de red'),
('Batería de reloj BIOS'),
('Cable de corriente'),
('Cable de video HD (HDMI)'),
('Cable de video (VGA)'),
('Cable de corriente (teléfono)'),
('Batería de no breaks'),
('Otro');

-- Insert dummy data into Tickets table
INSERT INTO tickets (senderName, idExtension, description, idProblemType, idDeviceType, idTechnician, status, dateOpened) VALUES
('Leticia Sánchez', 1, 'Mi computadora no prende', 7, 3, null, 'Sin empezar', '2024-09-18 17:42:43'),
('Paola Martínez', 7, 'No me abre el programa que necesito', 5, 3, null, 'Sin empezar', '2024-09-18 17:44:43'),
('Gerardo Mendoza', 2, 'Mi compu no deja de apagarse', 1, 1, 2, 'En progreso', '2024-09-18 17:50:43'),
('José Moya', 5, 'No me deja imprimir la impresora', 1, 5, 1, 'En progreso', '2024-09-19 10:42:43'),
('José Moya', 5, 'No me deja imprimir la impresora', 1, 1, null, 'Eliminado', '2024-09-19 13:42:43');

SELECT * FROM tickets;
INSERT INTO tickets (senderName, idExtension, description, status, dateOpened, dateClosed, idProblemType, idDeviceType, idTechnician, revisionProcess, diagnosis, solutionProcess, failedReason) VALUES
('Mariana De La Rosa', 2, 'Mi compu no deja de apagarse', 'Resuelto', '2024-09-19 14:00:08', '2024-09-19 17:00:08', 7, 5, 1, 'Se revisó la fuente de poder', 'La fuente de poder estaba quemada', 'Se cambió la fuente de poder', null),
('Kirill Makienko', 2, 'La compu saca chispas', 'No resuelto', '2024-09-19 14:20:58', '2024-09-19 14:55:58', 3, 2, 1, null, null, null, 'El usuario mojó la computadora hace una semana. Todo se averió'),
('Carolina Figueroa', 2, 'Nuestra impresora no se conecta con nuestra compu para imprimir', 'Resuelto', '2024-09-19 14:25:10', '2024-09-20 11:40:08', 2, 5, 2, 'Se revisó la conexión alámbrica', 'El cable de conexión estaba mal', 'Se cambió el cable y ya quedó', null),
('Juan Pablo Bustos', 3, 'No funciona nuestro escáner', 'Resuelto', '2024-09-19 14:35:10', '2024-09-19 15:40:08', 7, 6, 2, 'Se revisó si tenía actualizado su SO', 'No estaba actualizado el SO', 'Se actualizó el SO', null),
('Miguel Ángel Ávila', 2, 'La computadora dejó de mostrar nada en la pantalla', 'Resuelto', '2024-09-19 14:55:10', '2024-09-19 18:10:08', 7, 1, 2, 'Se revisó si el monitor estaba encendido', 'El monitor estaba roto', 'Se cambió el monitor', null);

-- Insert dummy data into Users table
INSERT INTO images (imageData, idTicket) VALUES
    (LOAD_FILE('D:/SeguridadInformatica/healthsync/BackEnd/BaseDeDatos/image1.jpg'), 6),
    (LOAD_FILE('D:/SeguridadInformatica/healthsync/BackEnd/BaseDeDatos/image2.jpg'), 7),
    (LOAD_FILE('D:/SeguridadInformatica/healthsync/BackEnd/BaseDeDatos/image3.jpg'), 8),
    (LOAD_FILE('D:/SeguridadInformatica/healthsync/BackEnd/BaseDeDatos/image4.jpg'), 9),
    (LOAD_FILE('D:/SeguridadInformatica/healthsync/BackEnd/BaseDeDatos/image5.jpg'), 10);

-- Insert dummy data into ticket_components table
INSERT INTO ticket_components (idTicket, idComponent, quantity) VALUES
(6, 1, 1),
(8, 9, 1),
(10, 2, 1);

-- Stored procedure to mark ticket as not solved
DELIMITER $$
CREATE PROCEDURE markTicketAsNotSolved(
    IN ticketId INT,
    IN failReason TEXT,
    IN image LONGBLOB
)
BEGIN
    START TRANSACTION;

    UPDATE tickets
    SET status = 'No resuelto', dateClosed = CURRENT_TIMESTAMP, failedReason = failReason
    WHERE idTicket = ticketId;

    INSERT INTO images (imageData, idTicket)
    VALUES (image, ticketId);

    COMMIT;
END$$
DELIMITER ;
-- Use case:
-- CALL markTicketAsNotSolved(3,"La fuente de poder había explotado",archivo_en_base64);


-- Stored procedure to mark ticket as solved with multiple or no components
DELIMITER $$
CREATE PROCEDURE markTicketAsSolved(
    IN ticketId INT,
    IN revProcess TEXT,
    IN diagno TEXT,
    IN solProcess TEXT,
    IN image LONGBLOB,
    IN components JSON -- JSON array containing components and quantities
)
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE totalComponents INT;

    START TRANSACTION;

    UPDATE tickets
    SET status = 'Resuelto', 
        dateClosed = CURRENT_TIMESTAMP, 
        revisionProcess = revProcess, 
        diagnosis = diagno, 
        solutionProcess = solProcess
    WHERE idTicket = ticketId;

    INSERT INTO images (imageData, idTicket)
    VALUES (image, ticketId);

    -- Get the total number of components in the JSON array
    SET totalComponents = JSON_LENGTH(components);

    -- Loop through the JSON array to insert each component
    WHILE i < totalComponents DO
        -- Insert each component from the JSON
        INSERT INTO ticket_components (idTicket, idComponent, quantity)
        VALUES (
            ticketId,
            JSON_UNQUOTE(JSON_EXTRACT(components, CONCAT('$[', i, '].idComponent'))),
            JSON_UNQUOTE(JSON_EXTRACT(components, CONCAT('$[', i, '].quantity')))
        );
        
        SET i = i + 1;
    END WHILE;

    COMMIT;
END$$
DELIMITER ;
-- Use case:
-- CALL markTicketAsSolved(
--    3, -- ID del ticket
--    "Se revisó si se había prendido la compu", 
--    "No estaba prendida", 
--    "Se cambió botón de prendida", 
--    LOAD_FILE('D:/SeguridadInformatica/healthsync/BackEnd/BaseDeDatos/image1.jpg'), 
--    '[{"idComponent": 1, "quantity": 2}, {"idComponent": 2, "quantity": 1}]'
-- );



SHOW TABLES;
SELECT * FROM areas;
SELECT * FROM components;
SELECT * FROM device_types;
SELECT * FROM extensions;
SELECT * FROM images;
SELECT * FROM problem_types;
SELECT * FROM ticket_components;
SELECT * FROM tickets;
SELECT * FROM users;