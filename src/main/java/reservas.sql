
-- TABLA: reservas
-- Relaciona un usuario con un puesto en una fecha y franja horaria.

CREATE TABLE IF NOT EXISTS reservas (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT             NOT NULL,
    id_puesto       INT             NOT NULL,
    fecha           DATE            NOT NULL,
    hora_inicio     TIME            NOT NULL,
    hora_fin        TIME            NOT NULL,
    estado          ENUM('activa', 'cancelada') NOT NULL DEFAULT 'activa',
    -- FK--
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_puesto)  REFERENCES puestos(id),
    -- Un puesto no puede estar reservado dos veces en el mismo día y hora --
    UNIQUE (id_puesto, fecha, hora_inicio)
);