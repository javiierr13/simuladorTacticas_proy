drop database if exists tfg_simuladorTactico;
create database tfg_simuladorTactico;
use tfg_simuladorTactico;

CREATE TABLE entrenador (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    correo      VARCHAR(150) NOT NULL UNIQUE,
    contrasena  VARCHAR(255) NOT NULL
);

CREATE TABLE jugador (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    dorsal           INT NOT NULL,
    posicion         VARCHAR(50) NOT NULL,
    pierna_dominante ENUM('IZQUIERDA','DERECHA','AMBAS')
);

CREATE TABLE alineacion (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    tipo_formacion VARCHAR(20) NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Relación 1:N ENTRENADOR–JUGADOR (entrena)
ALTER TABLE jugador
ADD COLUMN id_entrenador INT NOT NULL,
ADD CONSTRAINT fk_jugador_entrenador
    FOREIGN KEY (id_entrenador) REFERENCES entrenador(id);

-- Relación 1:N ENTRENADOR–ALINEACION (planea)
ALTER TABLE alineacion
ADD COLUMN id_entrenador INT NOT NULL,
ADD CONSTRAINT fk_alineacion_entrenador
    FOREIGN KEY (id_entrenador) REFERENCES entrenador(id);

-- Relación N:M JUGADOR–ALINEACION (participa) con posX, posY
CREATE TABLE participa (
    id_jugador   INT NOT NULL,
    id_alineacion INT NOT NULL,
    posX         INT NOT NULL,
    posY         INT NOT NULL,
    PRIMARY KEY (id_jugador, id_alineacion),
    CONSTRAINT fk_participa_jugador
        FOREIGN KEY (id_jugador) REFERENCES jugador(id),
    CONSTRAINT fk_participa_alineacion
        FOREIGN KEY (id_alineacion) REFERENCES alineacion(id)
);
