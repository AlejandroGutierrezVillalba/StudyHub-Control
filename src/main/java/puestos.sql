-- ============================================================
-- TABLA: puestos
-- Cada puesto pertenece a un aula y puede estar reservado o libre.
-- ============================================================
CREATE TABLE IF NOT EXISTS puestos (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    numero_puesto    INT             NOT NULL,
    aula             VARCHAR(50)     NOT NULL,
    tipo_puesto      VARCHAR(50)     NOT NULL,   -- ej: 'estudio', 'ordenador', 'reunión'
    esta_reservado   BOOLEAN         NOT NULL DEFAULT FALSE,
    -- No puede haber dos puestos con el mismo número en la misma aula
    UNIQUE (numero_puesto, aula)
);
-- Puestos
INSERT INTO puestos (numero_puesto, aula, tipo_puesto, esta_reservado)
VALUES (1, 'Aula101', 'estudio',    FALSE),
       (2, 'Aula101', 'estudio',    FALSE),
       (3, 'Aula101', 'ordenador',  FALSE),
       (1, 'Biblioteca', 'estudio', FALSE),
       (2, 'Biblioteca', 'estudio', FALSE);