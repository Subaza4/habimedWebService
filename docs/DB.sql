/* SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE,
SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DAT
E,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION'; */
-- -----------------------------------------------------
-- Schema medic
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS "medic" ;
-- -----------------------------------------------------
-- Schema medic
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS "medic";-- DEFAULT CHARACTER SET utf8 ;
--USE "medic" ;

-- -----------------------------------------------------
-- Table "medic"."persona"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."tipousuario" ;
CREATE TABLE IF NOT EXISTS "medic"."tipousuario" (
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "nombre" VARCHAR(45) NOT NULL,
    "descripcion" VARCHAR(250) NOT NULL,
PRIMARY KEY ("id"));

-- -----------------------------------------------------
-- Table "medic"."persona"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."persona" ;
CREATE TABLE IF NOT EXISTS "medic"."persona" (
    "dni" INT NOT NULL,
    "nombres" VARCHAR(45) NOT NULL,
    "apellidos" VARCHAR(45) NOT NULL,
    "correo" VARCHAR(45) NOT NULL,
    "celular" VARCHAR(9) NULL,
    "direccion" VARCHAR(45) NULL,
    "fecha_nacimiento" DATE NOT NULL,
PRIMARY KEY ("dni"));

-- -----------------------------------------------------
-- Table "medic"."usuario"

-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."usuario" ;
CREATE TABLE IF NOT EXISTS "medic"."usuario" (
    "dnipersona" INT NOT NULL,
    "tipousuario" INT NOT NULL,
    "usuario" VARCHAR(50) NOT NULL,
    "contrasenia" VARCHAR(255) NOT NULL, -- Aumentado a 255 para almacenar hashes seguros (ej. SHA256)
    "token" VARCHAR(255) NOT NULL,       -- Nuevo campo para el token
    "estado" boolean default false,      -- False por defecto (usuario inactivo)
    PRIMARY KEY ("dnipersona"),
    CONSTRAINT "dnipersona_fk" -- Renombrado para evitar conflictos si ya existe
    FOREIGN KEY ("dnipersona")
    REFERENCES "medic"."persona" ("dni")
);

-- -----------------------------------------------------
-- Table "medic"."consultorio"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."consultorio" ;
CREATE TABLE IF NOT EXISTS "medic"."consultorio" (
    "idconsultorio" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Esta es la modificación clave
    "ruc" VARCHAR(11),
    "nombre" VARCHAR(45) NOT NULL,
    "ubicacion" VARCHAR(45) NOT NULL,
    "direccion" VARCHAR(45) NULL,
    "telefono" VARCHAR(8) NULL
);

-- -----------------------------------------------------
-- Table "medic"."doctor_trabaja_consultorio"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."doctor_trabaja_consultorio" ;
CREATE TABLE IF NOT EXISTS "medic"."doctor_trabaja_consultorio" (
    "iddoctor" INT NOT NULL,
    "idconsultorio" INT NOT NULL,
    PRIMARY KEY ("iddoctor", "idconsultorio"), -- Clave primaria compuesta
    CONSTRAINT "fk_doctor_usuario" -- Nombre más descriptivo para la FK
        FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("dnipersona"),
    CONSTRAINT "fk_consultorio" -- Nombre más descriptivo para la FK
        FOREIGN KEY ("idconsultorio")
        REFERENCES "medic"."consultorio" ("idconsultorio")
);

-- -----------------------------------------------------
-- Table "medic"."especialidad"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."especialidad" ;
CREATE TABLE IF NOT EXISTS "medic"."especialidad" (
    "idespecialidad" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Modificación clave aquí
    "nombre" VARCHAR(45) NOT NULL,
    "descripcion" VARCHAR(255) NULL -- Sugerencia: Aumentar longitud para descripciones más largas
);

-- -----------------------------------------------------
-- Table "medic"."servicio"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."servicio" ;
CREATE TABLE IF NOT EXISTS "medic"."servicio" (
    "idservicio" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- ID autoincremental
    "idespecialidad" INT NOT NULL,
    "nombre" VARCHAR(100) NOT NULL, -- Aumentado a 100 para nombres de servicio
    "descripcion" VARCHAR(500) NULL, -- Aumentado para descripciones detalladas
    "riesgos" VARCHAR(500) NULL,     -- Aumentado para descripciones de riesgos
    CONSTRAINT "fk_especialidad_servicio" -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idespecialidad")
        REFERENCES "medic"."especialidad" ("idespecialidad")
);

-- -----------------------------------------------------
-- Table "medic"."horario_doctor"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."horario_doctor" ;
CREATE TABLE IF NOT EXISTS "medic"."horario_doctor" (
    "idhorariodoctor" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Nuevo ID autoincremental para cada horario
    "iddoctor" INT NOT NULL,
    "dia_semana" VARCHAR(15) NOT NULL, -- Nuevo campo para el día de la semana (Lunes, Martes, etc.)
    "hora_inicio" TIME NOT NULL,       -- Nuevo campo para la hora de inicio
    "hora_fin" TIME NOT NULL,         -- Nuevo campo para la hora de fin
    "duracion_minutos" INT NULL,      -- Duración en minutos, más preciso que DECIMAL
    CONSTRAINT "fk_doctor_horario"
        FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("dnipersona")
);

-- -----------------------------------------------------
-- Table "medic"."consultorio_has_servicio"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."consultorio_has_servicio" ;
CREATE TABLE IF NOT EXISTS "medic"."consultorio_has_servicio" (
    "idconsultorio" INT NOT NULL, -- Renombrado para consistencia
    "idservicio" INT NOT NULL,    -- Renombrado para consistencia
    PRIMARY KEY ("idconsultorio", "idservicio"),
    CONSTRAINT "fk_consultorio_servicio_consultorio" -- Nombre más conciso
        FOREIGN KEY ("idconsultorio")
        REFERENCES "medic"."consultorio" ("idconsultorio"),
    CONSTRAINT "fk_consultorio_servicio_servicio"    -- Nombre más conciso
        FOREIGN KEY ("idservicio")
        REFERENCES "medic"."servicio" ("idservicio")
);

-- -----------------------------------------------------
-- Table "medic"."cita"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."cita" ;
CREATE TABLE IF NOT EXISTS "medic"."cita" (
    "idcita" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- ID autoincremental
    "idpaciente" INT NOT NULL,
    "iddoctor" INT NOT NULL,
    "motivo" VARCHAR(255) NOT NULL,    -- Aumentado y hecho NOT NULL
    "fecha_hora_inicio" TIMESTAMP NOT NULL, -- Fecha y hora de inicio de la cita
    "fecha_hora_fin" TIMESTAMP NULL,    -- Fecha y hora de fin de la cita (puede ser calculable)
    "estado" VARCHAR(50) NOT NULL DEFAULT 'Pendiente', -- Estado de la cita (ej. Pendiente, Confirmada, Cancelada, Completada)
    "descripcion" VARCHAR(500) NULL,   -- Para notas adicionales o detalles del encuentro
    CONSTRAINT "fk_cita_paciente_usuario" -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idpaciente")
        REFERENCES "medic"."usuario" ("dnipersona"),
    CONSTRAINT "fk_cita_doctor_usuario" -- Nombre de la FK más descriptivo
        FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("dnipersona")
);

-- -----------------------------------------------------
-- Table "medic"."resenia"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."resenia" ;
CREATE TABLE IF NOT EXISTS "medic"."resenia" (
    "idresenia" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- ID autoincremental
    "iddoctor" INT NOT NULL,                                 -- Cambiado a idDoctor y hecho NOT NULL
    "calificacion" DECIMAL(2,1) NOT NULL,                    -- Calificación (ej. 4.5), hecha NOT NULL
    "comentario" VARCHAR(1000) NULL,                         -- Comentario más largo
    "fecha_resenia" DATE NOT NULL DEFAULT CURRENT_DATE,      -- Fecha de la reseña, autocompletada
    CONSTRAINT "fk_resenia_doctor_usuario"                   -- Nombre de la FK más descriptivo
        FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("dnipersona")
);

-- -----------------------------------------------------
-- Table "medic"."diagnostico"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."diagnostico" ;
CREATE TABLE IF NOT EXISTS "medic"."diagnostico" (
    "iddiagnostico" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Nuevo ID autoincremental para el diagnóstico
    "idcita" INT NOT NULL,                                       -- Clave foránea a la cita
    "descripcion" VARCHAR(1000) NOT NULL,                        -- Descripción detallada del diagnóstico
    "fecha_diagnostico" DATE NOT NULL DEFAULT CURRENT_DATE,      -- Fecha en que se realizó el diagnóstico
    CONSTRAINT "fk_diagnostico_cita"                             -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idcita")
        REFERENCES "medic"."cita" ("idcita")
);

-- -----------------------------------------------------
-- Table "medic"."receta"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."receta" ;
CREATE TABLE IF NOT EXISTS "medic"."receta" (
    "idreceta" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- New auto-incrementing ID for each recipe
    "idcita" INT NOT NULL,                                   -- Foreign key to the Cita
    "descripcion" VARCHAR(1000) NOT NULL,                    -- Detailed description of the prescription
    "fecha_receta" DATE NOT NULL DEFAULT CURRENT_DATE,       -- Date the prescription was issued
    CONSTRAINT "fk_receta_cita"                               -- More descriptive FK name
        FOREIGN KEY ("idcita")
        REFERENCES medic."cita" ("idcita")
        ON DELETE NO ACTION
        ON UPDATE NO ACTION                                 -- Standard PostgreSQL syntax for FK actions
);

-- -----------------------------------------------------
-- Table "medic"."recomendaciones"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."recomendaciones" ;
CREATE TABLE IF NOT EXISTS "medic"."recomendaciones" (
    "idrecomendacion" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Nuevo ID autoincremental para cada recomendación
    "idcita" INT NOT NULL,                                          -- Clave foránea a la cita
    "descripcion" VARCHAR(1000) NOT NULL,                           -- Descripción detallada de la recomendación
    "fecha_recomendacion" DATE NOT NULL DEFAULT CURRENT_DATE,       -- Fecha en que se emitió la recomendación
    CONSTRAINT "fk_recomendacion_cita"                              -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idcita")
        REFERENCES "medic"."cita" ("idcita")
);

-- -----------------------------------------------------
-- Table "medic"."detalle_pago"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."detalle_pago" ;
DROP TABLE IF EXISTS medic."detalle_pago";

CREATE TABLE IF NOT EXISTS medic."detalle_pago" (
    "iddetallepago" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- New auto-incrementing ID for each payment detail
    "idcita" INT NOT NULL,                                       -- Foreign key to the Cita
    "monto" DECIMAL(10, 2) NOT NULL,                             -- Amount of the payment (e.g., 1234.56)
    "metodo_pago" VARCHAR(50) NOT NULL,                          -- Payment method (e.g., 'Tarjeta', 'Efectivo', 'Transferencia')
    "estado_pago" VARCHAR(50) NOT NULL DEFAULT 'Pendiente',      -- Status of the payment (e.g., 'Pendiente', 'Pagado', 'Reembolsado')
    "fecha_pago" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,   -- Date and time of the payment
    CONSTRAINT "fk_detalle_pago_cita"                            -- More descriptive FK name
        FOREIGN KEY ("idcita")
        REFERENCES medic."cita" ("idcita")
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table "medic"."permisos_historial"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."permisos_historial" ;
CREATE TABLE IF NOT EXISTS "medic"."permisos_historial" (
    "idpermisohistorial" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Auto-incrementing primary key
    "iddoctor" INT NOT NULL,                                          -- Doctor granting/receiving permission
    "idpaciente" INT NOT NULL,                                        -- Patient whose history is involved
    "fechaotorgapermiso" DATE NOT NULL DEFAULT CURRENT_DATE,          -- Date permission was granted (default to current date)
    "fechadeniegapermiso" DATE NULL,                                  -- Date permission was revoked/denied
    "estado" VARCHAR(50) NOT NULL DEFAULT 'Otorgado',                 -- Current status of the permission (e.g., 'Otorgado', 'Denegado', 'Expirado')
    CONSTRAINT "uq_doctor_paciente_permiso" UNIQUE ("iddoctor", "idpaciente"), -- Optional: Ensure only one active permission record per doctor-patient pair
    CONSTRAINT "fk_permisos_historial_doctor"
        FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("dnipersona"),
    CONSTRAINT "fk_permisos_historial_paciente"
        FOREIGN KEY ("idpaciente")
        REFERENCES "medic"."usuario" ("dnipersona")
);

/*SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;*/

--------------------------------------------------------------------------------------------------------------------------
/******** STORE PROCEDURES DE LAS TABLAS MAESTRAS **********/
--------------------------------------------------------------------------------------------------------------------------
--LOGIN
CREATE OR REPLACE PROCEDURE medic.login_usuario(
    IN p_usuario VARCHAR,
    IN p_contrasenia VARCHAR,
    OUT mensaje_out TEXT,
    OUT resultado_out REFCURSOR
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_dni INT;
    v_token TEXT;
    v_contrasenia_hash TEXT;
BEGIN
    -- Verificar si el usuario existe y obtener su token
    SELECT "dnipersona", "token"
    INTO v_dni, v_token
    FROM medic."usuario"
    WHERE "usuario" = p_usuario;

    IF NOT FOUND THEN
        mensaje_out := 'ERROR: USUARIO NO ENCONTRADO';
        resultado_out := NULL;
        RETURN;
    END IF;

    -- Verificar si la contraseña es válida
    SELECT "contrasenia"
    INTO v_contrasenia_hash
    FROM medic."usuario"
    WHERE "dnipersona" = v_dni;

    IF NOT (crypt(p_contrasenia || v_token, v_contrasenia_hash) = v_contrasenia_hash) THEN
        mensaje_out := 'ERROR: CONTRASENIA INCORRECTA';
        resultado_out := NULL;
        RETURN;
    END IF;

    -- Login correcto, retornar el usuario con datos de Persona
    mensaje_out := 'OK';
    OPEN resultado_out FOR
        SELECT u.*, p.*
        FROM medic."usuario" u
        JOIN medic."persona" p ON u."dnipersona" = p."dni"
        WHERE u."dnipersona" = v_dni;
END;
$$;

-- REGISTRAR PERSONAS
DROP PROCEDURE medic.sp_upsert_persona;
CREATE OR REPLACE PROCEDURE medic.upsert_persona(
    p_dni INT,
    p_nombres VARCHAR,
    p_apellidos VARCHAR,
    p_correo VARCHAR,
    p_celular VARCHAR,
    p_direccion VARCHAR,
    p_fecha_nacimiento DATE
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM medic."persona" WHERE "dni" = p_dni) THEN
        UPDATE medic."persona"
        SET "nombres" = p_nombres,
            "apellidos" = p_apellidos,
            "correo" = p_correo,
            "celular" = p_celular,
            "direccion" = p_direccion,
            "fecha_nacimiento" = p_fecha_nacimiento
        WHERE "dni" = p_dni;
    ELSE
        INSERT INTO medic."persona"(
            "dni", "nombres", "apellidos", "correo", "celular", "direccion", "fecha_nacimiento"
        ) VALUES (
            p_dni, p_nombres, p_apellidos, p_correo, p_celular, p_direccion, p_fecha_nacimiento
        );
    END IF;
END;
$$;

--REGISTRAR USUARIO
DROP PROCEDURE medic.upsert_usuario;
CREATE OR REPLACE PROCEDURE medic.upsert_usuario(
    p_dniPersona INT,
    p_tipoUsuario INT,
    p_usuario VARCHAR,
    p_contrasenia_plain VARCHAR, -- contraseña original enviada desde Java (sin el token)
    p_actualizar_contrasenia BOOLEAN DEFAULT FALSE
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_token UUID;
    v_contrasenia_hashed TEXT;
BEGIN
    --Si el tipo de usuario existe 
    IF EXISTS (SELECT 1 FROM medic."tipousuario" WHERE "id" = p_tipoUsuario) THEN
        -- Si el usuario ya existe y la contraseña no será actualizada
        IF EXISTS (SELECT 1 FROM medic."usuario" WHERE "dnipersona" = p_dniPersona) THEN
            IF p_actualizar_contrasenia THEN
                -- Generar nuevo token
                v_token := gen_random_uuid();
                -- Hash de la contraseña + token
                v_contrasenia_hashed := crypt(p_contrasenia_plain || v_token, gen_salt('bf'));
    
                UPDATE medic."usuario"
                SET "tipousuario" = p_tipoUsuario,
                    "usuario" = p_usuario,
                    "token" = v_token::text,
                    "contrasenia" = v_contrasenia_hashed
                WHERE "dnipersona" = p_dniPersona;
            ELSE
                -- Actualización normal sin modificar la contraseña ni el token
                UPDATE medic."usuario"
                SET "tipousuario" = p_tipoUsuario,
                    "usuario" = p_usuario
                WHERE "dnipersona" = p_dniPersona;
            END IF;
        ELSE
            -- Insertar nuevo usuario
            v_token := gen_random_uuid();
            v_contrasenia_hashed := crypt(p_contrasenia_plain || v_token, gen_salt('bf'));
    
            INSERT INTO medic."usuario" (
                "dnipersona", "tipousuario", "usuario", "contrasenia", "token"
            ) VALUES (
                p_dniPersona, p_tipoUsuario, p_usuario, v_contrasenia_hashed, v_token::text
            );
        END IF;
    END IF;
END;
$$;

--REGISTRAR CONSULTORIOS
DROP PROCEDURE medic.upsert_consultorio;
CREATE OR REPLACE PROCEDURE medic.upsert_consultorio(
    p_idConsultorio INT DEFAULT NULL,
    p_nombre VARCHAR,
    p_ubicacion VARCHAR,
    p_direccion VARCHAR,
    p_telefono VARCHAR,
    p_ruc VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idConsultorio IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."consultorio" WHERE "idconsultorio" = p_idConsultorio
    ) THEN
        UPDATE medic."consultorio"
        SET "nombre" = p_nombre,
            "ubicacion" = p_ubicacion,
            "direccion" = p_direccion,
            "telefono" = p_telefono,
            --"ruc" = p_ruc
        WHERE "idconsultorio" = p_idConsultorio;
    ELSE
        INSERT INTO medic."consultorio"(
            "nombre", "ubicacion", "direccion", "telefono", "ruc"
        ) VALUES (
            p_nombre, p_ubicacion, p_direccion, p_telefono, p_ruc
        );
    END IF;
END;
$$;

--TRABAJA EN UN CONSULTORIO
DROP PROCEDURE medic.upsert_doctor_trabaja_consultorio;
CREATE OR REPLACE PROCEDURE medic.upsert_doctor_trabaja_consultorio(
    p_idDoctor INT,
    p_idConsultorio INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM medic."doctor_trabaja_consultorio"
        WHERE "iddoctor" = p_idDoctor AND "idconsultorio" = p_idConsultorio
    ) THEN
        -- En este caso, como la tabla solo tiene PKs y no más datos, no hay nada que actualizar
        -- Pero podrías lanzar un NOTICE o simplemente no hacer nada
        RAISE NOTICE 'La relación ya existe. No se requiere actualización.';
    ELSE
        INSERT INTO medic."doctor_trabaja_consultorio"(
            "iddoctor", "idconsultorio"
        ) VALUES (
            p_idDoctor, p_idConsultorio
        );
    END IF;
END;
$$;

--REGISTRAR ESPECIALIDADES
DROP PROCEDURE medic.upsert_especialidad;
CREATE OR REPLACE PROCEDURE medic.upsert_especialidad(
    p_idEspecialidad INT DEFAULT NULL,
    p_nombre VARCHAR,
    p_descripcion VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idEspecialidad IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."especialidad" WHERE "idespecialidad" = p_idEspecialidad
    ) THEN
        UPDATE medic."especialidad"
        SET "nombre" = p_nombre,
            "descripcion" = p_descripcion
        WHERE "idespecialidad" = p_idEspecialidad;
    ELSE
        INSERT INTO medic."especialidad"(
            "nombre", "descripcion"
        ) VALUES (
            p_nombre, p_descripcion
        );
    END IF;
END;
$$;

--REGISTRAR SERVICIOS
DROP PROCEDURE medic.upsert_servicio;
CREATE OR REPLACE PROCEDURE medic.upsert_servicio(
    p_idServicio INT DEFAULT NULL,
    p_idEspecialidad INT,
    p_nombre VARCHAR,
    p_descripcion VARCHAR,
    p_riesgos VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idServicio IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."servicio" WHERE "idservicio" = p_idServicio
    ) THEN
        UPDATE medic."servicio"
        SET "idespecialidad" = p_idEspecialidad,
            "nombre" = p_nombre,
            "descripcion" = p_descripcion,
            "riesgos" = p_riesgos
        WHERE "idservicio" = p_idServicio;
    ELSE
        INSERT INTO medic."servicio"(
            "idespecialidad", "nombre", "descripcion", "riesgos"
        ) VALUES (
            p_idEspecialidad, p_nombre, p_descripcion, p_riesgos
        );
    END IF;
END;
$$;

--REGISTRAR HORARIO DEL DOCTOR
DROP PROCEDURE medic.upsert_horario_doctor;
CREATE OR REPLACE PROCEDURE medic.upsert_horario_doctor(
    p_idHorarioDoctor INT DEFAULT NULL,
    p_idDoctor INT,
    p_dia_semana VARCHAR,
    p_hora_inicio TIME,
    p_hora_fin TIME,
    p_duracion_minutos INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idHorarioDoctor IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."horario_doctor" WHERE "idhorariodoctor" = p_idHorarioDoctor
    ) THEN
        UPDATE medic."horario_doctor"
        SET "iddoctor" = p_idDoctor,
            "dia_semana" = p_dia_semana,
            "hora_inicio" = p_hora_inicio,
            "hora_fin" = p_hora_fin,
            "duracion_minutos" = p_duracion_minutos
        WHERE "idhorariodoctor" = p_idHorarioDoctor;
    ELSE
        INSERT INTO medic."horario_doctor"(
            "iddoctor", "dia_semana", "hora_inicio", "hora_fin", "duracion_minutos"
        ) VALUES (
            p_idDoctor, p_dia_semana, p_hora_inicio, p_hora_fin, p_duracion_minutos
        );
    END IF;
END;
$$;

--REGISTRAR CONSULTORIO TIENE SERVICIOS
DROP PROCEDURE medic.upsert_consultorio_has_servicio;
CREATE OR REPLACE PROCEDURE medic.upsert_consultorio_has_servicio(
    p_idConsultorio INT,
    p_idServicio INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM medic."consultorio_has_servicio"
        WHERE "idconsultorio" = p_idConsultorio AND "idservicio" = p_idServicio
    ) THEN
        RAISE NOTICE 'La relación ya existe. No se requiere actualización.';
    ELSE
        INSERT INTO medic."consultorio_has_servicio"(
            "idconsultorio", "idservicio"
        ) VALUES (
            p_idConsultorio, p_idServicio
        );
    END IF;
END;
$$;

--REGISTRA UNA CITA
DROP PROCEDURE medic.upsert_cita;
CREATE OR REPLACE PROCEDURE medic.upsert_cita(
    p_idCita INT DEFAULT NULL,
    p_idPaciente INT,
    p_idDoctor INT,
    p_motivo VARCHAR,
    p_fecha_hora_inicio TIMESTAMP,
    p_fecha_hora_fin TIMESTAMP,
    p_estado VARCHAR DEFAULT 'Pendiente',
    p_descripcion VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idCita IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."cita" WHERE "idcita" = p_idCita
    ) THEN
        UPDATE medic."cita"
        SET "idpaciente" = p_idPaciente,
            "iddoctor" = p_idDoctor,
            "motivo" = p_motivo,
            "fecha_hora_inicio" = p_fecha_hora_inicio,
            "fecha_hora_fin" = p_fecha_hora_fin,
            "estado" = p_estado,
            "descripcion" = p_descripcion
        WHERE "idcita" = p_idCita;
    ELSE
        INSERT INTO medic."cita"(
            "idpaciente", "iddoctor", "motivo", "fecha_hora_inicio", "fecha_hora_fin", "estado", "descripcion"
        ) VALUES (
            p_idPaciente, p_idDoctor, p_motivo, p_fecha_hora_inicio, p_fecha_hora_fin, p_estado, p_descripcion
        );
    END IF;
END;
$$;

--REGISTRAR RESEÑA
DROP PROCEDURE medic.upsert_resenia;
CREATE OR REPLACE PROCEDURE medic.upsert_resenia(
    p_idResenia INT DEFAULT NULL,
    p_idDoctor INT,
    p_calificacion DECIMAL,
    p_comentario VARCHAR,
    p_fecha_resenia DATE DEFAULT CURRENT_DATE
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idResenia IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."resenia" WHERE "idresenia" = p_idResenia
    ) THEN
        UPDATE medic."resenia"
        SET "iddoctor" = p_idDoctor,
            "calificacion" = p_calificacion,
            "comentario" = p_comentario,
            "fecha_resenia" = p_fecha_resenia
        WHERE "idresenia" = p_idResenia;
    ELSE
        INSERT INTO medic."resenia"(
            "iddoctor", "calificacion", "comentario", "fecha_resenia"
        ) VALUES (
            p_idDoctor, p_calificacion, p_comentario, p_fecha_resenia
        );
    END IF;
END;
$$;

--REGISTRA DIAGNOSTICO
DROP PROCEDURE medic.upsert_diagnostico;
CREATE OR REPLACE PROCEDURE medic.upsert_diagnostico(
    p_idDiagnostico INT DEFAULT NULL,
    p_idCita INT,
    p_descripcion VARCHAR,
    p_fecha_diagnostico DATE DEFAULT CURRENT_DATE
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idDiagnostico IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."diagnostico" WHERE "iddiagnostico" = p_idDiagnostico
    ) THEN
        UPDATE medic."diagnostico"
        SET "idcita" = p_idCita,
            "descripcion" = p_descripcion,
            "fecha_diagnostico" = p_fecha_diagnostico
        WHERE "iddiagnostico" = p_idDiagnostico;
    ELSE
        INSERT INTO medic."diagnostico"(
            "idcita", "descripcion", "fecha_diagnostico"
        ) VALUES (
            p_idCita, p_descripcion, p_fecha_diagnostico
        );
    END IF;
END;
$$;

--REGISTRA RECETA
DROP PROCEDURE medic.upsert_receta;
CREATE OR REPLACE PROCEDURE medic.upsert_receta(
    p_idReceta INT DEFAULT NULL,
    p_idCita INT,
    p_descripcion VARCHAR,
    p_fecha_receta DATE DEFAULT CURRENT_DATE
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idReceta IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."receta" WHERE "idreceta" = p_idReceta
    ) THEN
        UPDATE medic."receta"
        SET "idcita" = p_idCita,
            "descripcion" = p_descripcion,
            "fecha_receta" = p_fecha_receta
        WHERE "idreceta" = p_idReceta;
    ELSE
        INSERT INTO medic."receta"(
            "idcita", "descripcion", "fecha_receta"
        ) VALUES (
            p_idCita, p_descripcion, p_fecha_receta
        );
    END IF;
END;
$$;

--REGISTRA RECOMENDACIONES
DROP PROCEDURE medic.upsert_recomendacion;
CREATE OR REPLACE PROCEDURE medic.upsert_recomendacion(
    p_idRecomendacion INT DEFAULT NULL,
    p_idCita INT,
    p_descripcion VARCHAR,
    p_fecha_recomendacion DATE DEFAULT CURRENT_DATE
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idRecomendacion IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."recomendaciones" WHERE "idrecomendacion" = p_idRecomendacion
    ) THEN
        UPDATE medic."recomendaciones"
        SET "idcita" = p_idCita,
            "descripcion" = p_descripcion,
            "fecha_recomendacion" = p_fecha_recomendacion
        WHERE "idrecomendacion" = p_idRecomendacion;
    ELSE
        INSERT INTO medic."recomendaciones"(
            "idcita", "descripcion", "fecha_recomendacion"
        ) VALUES (
            p_idCita, p_descripcion, p_fecha_recomendacion
        );
    END IF;
END;
$$;

--REGISTRA PERMISO HISTORIAL
DROP PROCEDURE medic.upsert_permiso_historial;
CREATE OR REPLACE PROCEDURE medic.upsert_permiso_historial(
    p_idPermisoHistorial INT DEFAULT NULL,
    p_idDoctor INT,
    p_idPaciente INT,
    p_fechaOtorgaPermiso DATE DEFAULT CURRENT_DATE,
    p_fechaDeniegaPermiso DATE DEFAULT NULL,
    p_estado VARCHAR DEFAULT 'Otorgado'
)
LANGUAGE plpgsql
AS $$
BEGIN
    IF p_idPermisoHistorial IS NOT NULL AND EXISTS (
        SELECT 1 FROM medic."permisos_historial" WHERE "idpermisohistorial" = p_idPermisoHistorial
    ) THEN
        UPDATE medic."permisos_historial"
        SET "iddoctor" = p_idDoctor,
            "idpaciente" = p_idPaciente,
            "fechaotorgapermiso" = p_fechaOtorgaPermiso,
            "fechadeniegapermiso" = p_fechaDeniegaPermiso,
            "estado" = p_estado
        WHERE "idpermisohistorial" = p_idPermisoHistorial;
    ELSE
        INSERT INTO medic."permisos_historial"(
            "iddoctor", "idpaciente", "fechaotorgapermiso", "fechadeniegapermiso", "estado"
        ) VALUES (
            p_idDoctor, p_idPaciente, p_fechaOtorgaPermiso, p_fechaDeniegaPermiso, p_estado
        );
    END IF;
END;
$$;

--REGISTRA DETALLE PAGO
DROP PROCEDURE medic.insert_detalle_pago;
CREATE OR REPLACE PROCEDURE medic.insert_detalle_pago(
    p_idCita INT,
    p_monto DECIMAL,
    p_metodo_pago VARCHAR,
    p_estado_pago VARCHAR DEFAULT 'Pendiente',
    p_fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO medic."detalle_pago" (
        "idcita", "monto", "metodo_pago", "estado_pago", "fecha_pago"
    ) VALUES (
        p_idCita, p_monto, p_metodo_pago, p_estado_pago, p_fecha_pago
    );
END;
$$;
