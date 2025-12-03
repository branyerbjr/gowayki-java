-- ============================================================
--  BASE DE DATOS: gowayki_db
--  Autor: AndesCode
--  Fecha: 2025
--  Descripción: BD básica funcional para GoWayki (Java Desktop)
-- ============================================================

DROP DATABASE IF EXISTS gowayki_db;
CREATE DATABASE gowayki_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE gowayki_db;

-- =========================
-- TABLA: empresa
-- =========================
CREATE TABLE empresa (
    id_empresa INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ruc VARCHAR(20),
    telefono VARCHAR(20)
);

-- =========================
-- TABLA: ruta
-- =========================
CREATE TABLE ruta (
    id_ruta INT AUTO_INCREMENT PRIMARY KEY,
    id_empresa INT NOT NULL,
    nombre_ruta VARCHAR(150) NOT NULL,
    origen VARCHAR(100) NOT NULL,
    destino VARCHAR(100) NOT NULL,
    
    FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa)
);

-- =========================
-- TABLA: paradero
-- =========================
CREATE TABLE paradero (
    id_paradero INT AUTO_INCREMENT PRIMARY KEY,
    id_ruta INT NOT NULL,
    nombre_paradero VARCHAR(150) NOT NULL,
    orden INT NOT NULL,

    FOREIGN KEY (id_ruta) REFERENCES ruta(id_ruta)
);

-- =========================
-- TABLA: horario
-- =========================
CREATE TABLE horario (
    id_horario INT AUTO_INCREMENT PRIMARY KEY,
    id_ruta INT NOT NULL,
    hora_salida TIME NOT NULL,
    hora_llegada TIME NOT NULL,

    FOREIGN KEY (id_ruta) REFERENCES ruta(id_ruta)
);

-- =========================
-- TABLA: tarifa
-- =========================
CREATE TABLE tarifa (
    id_tarifa INT AUTO_INCREMENT PRIMARY KEY,
    id_ruta INT NOT NULL,
    tarifa_dia DECIMAL(5,2) NOT NULL,
    tarifa_noche DECIMAL(5,2) NOT NULL,
    hora_inicio_noche TIME NOT NULL,
    hora_fin_noche TIME NOT NULL,

    FOREIGN KEY (id_ruta) REFERENCES ruta(id_ruta)
);

-- ============================================================
-- DATOS DE PRUEBA
-- ============================================================

INSERT INTO empresa (nombre, ruc, telefono) VALUES
('SIT - Servicios Integrados de Transporte', '20458963211', '054-400100'),
('Transportes Characato S.A.', '20452896312', '054-325896');

INSERT INTO ruta (id_empresa, nombre_ruta, origen, destino) VALUES
(1, 'AQP01 - Paucarpata - Centro - Cayma', 'Paucarpata', 'Cayma'),
(2, 'CH01 - Characato - Centro', 'Characato', 'Centro Cívico');

INSERT INTO paradero (id_ruta, nombre_paradero, orden) VALUES
(1, 'Parque Industrial', 1),
(1, 'Avenida Dolores', 2),
(1, 'Centro Cívico', 3),
(1, 'Real Plaza', 4),

(2, 'Characato Plaza', 1),
(2, 'Avenida Venezuela', 2),
(2, 'Centro Cívico', 3);

INSERT INTO horario (id_ruta, hora_salida, hora_llegada) VALUES
(1, '05:30:00', '06:10:00'),
(1, '06:10:00', '06:50:00'),
(2, '05:45:00', '06:20:00');

INSERT INTO tarifa (id_ruta, tarifa_dia, tarifa_noche, hora_inicio_noche, hora_fin_noche) VALUES
(1, 1.50, 2.00, '20:00:00', '05:00:00'),
(2, 1.30, 1.80, '20:00:00', '05:00:00');

-- ============================================================
-- STORED PROCEDURE: sp_buscar_rutas
-- ============================================================

DROP PROCEDURE IF EXISTS sp_buscar_rutas;

DELIMITER $$

CREATE PROCEDURE sp_buscar_rutas(
    IN p_origen VARCHAR(100),
    IN p_destino VARCHAR(100),
    IN p_hora TIME
)
BEGIN
    SELECT 
        r.id_ruta,
        r.nombre_ruta,
        r.origen,
        r.destino,
        h.hora_salida,
        h.hora_llegada,
        CASE 
            WHEN p_hora >= t.hora_inicio_noche OR p_hora <= t.hora_fin_noche
            THEN t.tarifa_noche
            ELSE t.tarifa_dia
        END AS tarifa_aplicable,
        e.nombre AS empresa
    FROM ruta r
    INNER JOIN empresa e ON r.id_empresa = e.id_empresa
    LEFT JOIN horario h ON r.id_ruta = h.id_ruta
    LEFT JOIN tarifa t ON r.id_ruta = t.id_ruta
    WHERE r.origen LIKE CONCAT('%', p_origen, '%')
      AND r.destino LIKE CONCAT('%', p_destino, '%');
END$$

DELIMITER ;

-- ============================================================
-- LISTO
-- BD creada y lista para conectarse desde Java
-- ============================================================
