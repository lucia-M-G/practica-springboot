CREATE DATABASE IF NOT EXISTS co2_by_ai
DEFAULT CHARACTER SET 'utf8'
DEFAULT COLLATE 'utf8_bin';

USE co2_by_ai;

CREATE TABLE IF NOT EXISTS ai_model (
    nombre VARCHAR(50) NOT NULL,
    tipo_tarea VARCHAR(50) NOT NULL,
    co2_consulta DOUBLE NOT NULL,
    consumo DOUBLE NOT NULL,
    PRIMARY KEY (nombre)
);

CREATE TABLE IF NOT EXISTS sesions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    tipo_tarea VARCHAR(50) NOT NULL,
    num_consultas INT NOT NULL,
    fecha DATE NOT NULL,
    descripcion TEXT,
    co2_estimado DOUBLE,
    kwh_estimado DOUBLE
);

INSERT INTO ai_model (nombre, tipo_tarea, co2_consulta, consumo) VALUES
('GPT-4',            'texto',    6.5,   0.005),
('GPT-3.5',          'texto',    0.65,  0.001),
('Stable Diffusion', 'imagen',   8.75,  0.012),
('GitHub Copilot',   'codigo',   0.075, 0.0001),
('Cerca Google',     'busqueda', 0.1,   0.0003);