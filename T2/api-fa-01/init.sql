CREATE DATABASE IF NOT EXISTS crud_db;
USE crud_db;

CREATE TABLE IF NOT EXISTS items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO items (nombre, descripcion, precio) VALUES
('Laptop', 'Laptop gaming de alta gama', 1200.50),
('Mouse', 'Mouse inalámbrico ergonómico', 25.99),
('Teclado', 'Teclado mecánico RGB', 89.99),
('Monitor', 'Monitor 27 pulgadas 4K', 350.00),
('Auriculares', 'Auriculares con cancelación de ruido', 150.00);