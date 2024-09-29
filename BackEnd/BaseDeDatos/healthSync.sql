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
  imageData BLOB,
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



-- Insert dummy data into Areas table
INSERT INTO areas (areaName) VALUES
('Dirección Operativa'),
('Administración de Personal'),
('Hospitalización'),
('Trabajo Social'),
('Finanzas y Contabilidad'),
('Tecnologías de la Información'),
('Recursos Materiales y Control Patrimonial'),
('Servicios Auxiliares y de Diagnóstico'),
('Otro');

-- Insert dummy data into Extensions table
INSERT INTO extensions (extensionNumber, idArea) VALUES
('1200', 1),
('1290', 2),
('2060', 3),
('2180', 4),
('2190', 4),
('2240', 4),
('3290', 4),
('3310', 4),
('5040', 4),
('6140', 4),
('8020', 4),
('4010', 5),
('2160', 6),
('2680', 6),
('4030', 6),
('4060', 6),
('4280', 6),
('4520', 6),
('4530', 6),
('4540', 6),
('4560', 6),
('4620', 7),
('7210', 8),
('8050', 8),
('Otro', 9);

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
('Miguel Ángel Ávila', 2, 'La computadora dejó de mostrar nada en la pantalla', 'Resuelto', '2024-09-19 14:55:10', '2024-09-19 18:10:08', 7, 1, 2, 'Se revisó si el monitor estaba encendido', 'El monitor estaba apagado', 'Se prendió el monitor', null);

-- Insert dummy data into Users table
INSERT INTO images (imageData, idTicket) VALUES
    (0x89504E470D0A1A0A0000000D4948445200000001000000010806000000FF0000FF, 6),
    (0x89504E470D0A1A0A0000000D4948445200000002000000020806000000FF0000FF, 7),
    (0x89504E470D0A1A0A0000000D4948445200000003000000030806000000FF0000FF, 8),
    (0x89504E470D0A1A0A0000000D4948445200000004000000040806000000FF0000FF, 9),
    (0x89504E470D0A1A0A0000000D4948445200000005000000050806000000FF0000FF, 10);

-- Insert dummy data into ticket_components table
INSERT INTO ticket_components (idTicket, idComponent, quantity) VALUES
(6, 1, 2),
(8, 2, 1),
(10, 3, 1);

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