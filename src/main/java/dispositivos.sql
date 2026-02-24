-- ============================================================
-- TABLA: dispositivos
-- Almacena ordenadores y tablets prestables.
-- El campo 'tipo' indica si es 'Ordenador' o 'Tablet'.
-- El campo 'estado' indica si está disponible, prestado o averiado.
-- ============================================================
CREATE TABLE IF NOT EXISTS dispositivos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    marca       VARCHAR(100)        NOT NULL,
    modelo      VARCHAR(100)        NOT NULL,
    tipo        ENUM('Ordenador', 'Tablet') NOT NULL,
    estado      ENUM('disponible', 'prestado', 'averiado') NOT NULL DEFAULT 'disponible'
);

--dispositivos
INSERT INTO dispositivos (marca, modelo, tipo, estado)
VALUES ('Lenovo', 'IdeaPad 3',  'Ordenador', 'disponible'),
       ('HP',     'Pavilion 14','Ordenador', 'disponible'),
       ('Samsung','Galaxy Tab A','Tablet',   'disponible');