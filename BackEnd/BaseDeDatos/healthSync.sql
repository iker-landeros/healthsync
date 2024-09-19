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
  PRIMARY KEY (idExtension)
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
  idArea INT,       
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

  CONSTRAINT fk_ticket_area
    FOREIGN KEY (idArea)
    REFERENCES areas (idArea)
    ON DELETE SET NULL,

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
('Enlace Social'),
('Sindicato'),
('Fiscalía'),
('Seguro Popular'),
('Comisaría'),
('Voluntariado'),
('Dirección Operativa'),
('Hospitalización'),
('Urgencias'),
('Cirugía'),
('Enseñanza'),
('Imagenología'),
('Calidad'),
('Enfermería'),
('Trabajo Social'),
('Servicios Jurídicos'),
('Mantenimiento'),
('Personal'),
('Finanzas'),
('Laboratorio'),
('Dirección Médica'),
('Dirección General'),
('Dirección Administrativa'),
('Otro');

-- Insert dummy data into Extensions table
INSERT INTO extensions (extensionNumber) VALUES
('1001'),
('1002'),
('1003'),
('1004'),
('1005'),
('1006'),
('1007'),
('1008'),
('1009'),
('1010'),
('Otro');

-- Insert dummy data into Problem Types table
INSERT INTO problem_types (problemName) VALUES
('Revisión de equipo'),
('Alta de correo electrónico'),
('Solicitud de capacitación'),
('Bloqueo de página web'),
('Aplicación no funciona'),
('Problema de red'),
('Equipo no prende');

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
('Cable de poder'),
('Monitor de computadora'),
('Procesador'),
('Cartucho de impresora');

-- Insert dummy data into Tickets table
INSERT INTO tickets (senderName, idArea, idExtension, description, idProblemType, idDeviceType, idTechnician, status) VALUES
('Leticia Sánchez', 1, 1, 'Mi computadora no prende', 7, 3, null, 'Sin empezar'),
('Paola Martínez', 7, 2, 'No me abre el programa que necesito', 5, 3, null, 'Sin empezar'),
('Gerardo Mendoza', 2, 5, 'Mi compu no deja de apagarse', 1, 1, 2, 'En progreso'),
('José Moya', 10, 1, 'No me deja imprimir la impresora', 1, 5, 1, 'En progreso');

-- Insert dummy data into ticket_components table
INSERT INTO ticket_components (idTicket, idComponent, quantity) VALUES
(1, 1, 2),
(2, 2, 1),
(3, 3, 1);

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