-- Create the Database
CREATE DATABASE healthSync;
USE healthSync;
-- DROP DATABASE healthSync;
SHOW DATABASES;

-- Table for Users (Technicians and Admins)
CREATE TABLE users (
  idUser INT AUTO_INCREMENT,
  name VARCHAR(100),
  username VARCHAR(45),
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
  status ENUM('unassigned', 'in progress', 'resolved', 'canceled') DEFAULT 'unassigned',
  dateOpened DATETIME DEFAULT CURRENT_TIMESTAMP,
  dateClosed DATETIME DEFAULT NULL,         
  idProblemType INT, 
  idDeviceType INT,  
  idTechnician INT DEFAULT NULL,
  revisionProcess TEXT DEFAULT NULL, 
  diagnosisProcess TEXT DEFAULT NULL, 
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
('John Doe', 'jdoe', SHA2('password123', 256), 'technician'),
('Jane Smith', 'jsmith', SHA2('adminpass', 256), 'admin');

-- Insert dummy data into Areas table
INSERT INTO areas (areaName) VALUES
('Radiology'),
('Cardiology'),
('Emergency Room'),
('Intensive Care Unit');

-- Insert dummy data into Extensions table
INSERT INTO extensions (extensionNumber) VALUES
('1001'),
('1002'),
('1003'),
('1004');

-- Insert dummy data into Problem Types table
INSERT INTO problem_types (problemName) VALUES
('Power failure'),
('Software crash'),
('Broken screen'),
('Printer malfunction');

-- Insert dummy data into Device Types table
INSERT INTO device_types (deviceName) VALUES
('X-Ray Machine'),
('MRI Scanner'),
('ECG Machine'),
('Ultrasound Machine');

-- Insert dummy data into Components table
INSERT INTO components (componentName) VALUES
('Power Cable'),
('Screen Replacement'),
('Software Update'),
('Printer Cartridge');

-- Insert dummy data into Tickets table
INSERT INTO tickets (senderName, idArea, idExtension, description, idProblemType, idDeviceType, idTechnician, status) VALUES
('Michael Green', 1, 1, 'X-ray machine is not turning on.', 1, 1, 1, 'unassigned'),
('Sarah Brown', 2, 2, 'ECG machine screen is cracked.', 3, 3, 1, 'unassigned'),
('Chris White', 3, 3, 'MRI scanner software keeps crashing.', 2, 2, 2, 'in progress');

-- Insert dummy data into ticket_components table
INSERT INTO ticket_components (idTicket, idComponent, quantity) VALUES
(1, 1, 2),
(2, 2, 1),
(3, 3, 1);

SHOW TABLES;

