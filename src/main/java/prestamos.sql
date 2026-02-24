-- ============================================================
-- TABLA: prestamos
-- Relaciona un usuario con un dispositivo y controla fechas y retrasos.
-- ============================================================
CREATE TABLE IF NOT EXISTS prestamos (
    id                          INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario                  INT     NOT NULL,
    id_dispositivo              INT     NOT NULL,
    fecha_prestamo              DATE    NOT NULL,
    fecha_devolucion_prevista   DATE    NOT NULL,
    fecha_devolucion_real       DATE,               -- NULL si aún no se ha devuelto
    estado                      ENUM('activo', 'devuelto') NOT NULL DEFAULT 'activo',
    -- Claves foráneas
    FOREIGN KEY (id_usuario)     REFERENCES usuarios(id),
    FOREIGN KEY (id_dispositivo) REFERENCES dispositivos(id)
);