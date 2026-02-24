-- ============================================================
-- TABLA: sanciones (opcional según el proyecto)
-- Se genera automáticamente cuando hay retraso en una devolución.
-- ============================================================
CREATE TABLE IF NOT EXISTS sanciones (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT     NOT NULL,
    id_prestamo     INT     NOT NULL,
    dias_retraso    INT     NOT NULL,
    fecha_sancion   DATE    NOT NULL,
    -- Claves foráneas
    FOREIGN KEY (id_usuario)  REFERENCES usuarios(id),
    FOREIGN KEY (id_prestamo) REFERENCES prestamos(id)
);