CREATE TABLE IF NOT EXISTS usuarios (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100)        NOT NULL,
    apellido1   VARCHAR(100)        NOT NULL,
    apellido2   VARCHAR(100),
    correo      VARCHAR(255)        NOT NULL UNIQUE,
    dni         INT                 NOT NULL UNIQUE,
    tipo        ENUM('alumno', 'profesor') NOT NULL,
    estado      ENUM('alta', 'baja', 'sancionado') NOT NULL DEFAULT 'alta',
    -- Campos exclusivos de alumno (pueden ser NULL si es profesor)
    curso       VARCHAR(50),
    tutor       VARCHAR(150),
    edad        INT,
    -- Campos exclusivos de profesor (pueden ser NULL si es alumno)
    materia     VARCHAR(100)
);

-- Usuarios
INSERT INTO usuarios (nombre, apellido1, apellido2, correo, dni, tipo, estado, curso, tutor, edad)
VALUES ('Carlos',  'García',  'López',   'carlos@instituto.es',  12345678, 'alumno',   'alta', '1DAW', 'Prof. Martínez', 18);

INSERT INTO usuarios (nombre, apellido1, apellido2, correo, dni, tipo, estado, curso, tutor, edad)
VALUES ('Lucía',   'Martínez','Ruiz',    'lucia@instituto.es',   87654321, 'alumno',   'alta', '1DAW', 'Prof. Martínez', 17);

INSERT INTO usuarios (nombre, apellido1, apellido2, correo, dni, tipo, estado, materia)
VALUES ('Pedro',   'Sánchez', 'Pérez',   'pedro@instituto.es',   11223344, 'profesor', 'alta', 'Programación');