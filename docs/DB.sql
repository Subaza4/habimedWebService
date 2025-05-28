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
-- Table "medic"."Persona"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."TipoUsuario" ;
CREATE TABLE IF NOT EXISTS "medic"."TipoUsuario" (
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "nombre" VARCHAR(45) NOT NULL,
    "descripcion" VARCHAR(250) NOT NULL,
PRIMARY KEY ("dni"));

-- -----------------------------------------------------
-- Table "medic"."Persona"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Persona" ;
CREATE TABLE IF NOT EXISTS "medic"."Persona" (
    "dni" INT NOT NULL,
    "nombres" VARCHAR(45) NOT NULL,
    "apellidos" VARCHAR(45) NOT NULL,
    "correo" VARCHAR(45) NOT NULL,
    "celular" VARCHAR(9) NULL,
    "direccion" VARCHAR(45) NULL,
    "fecha_nacimiento" DATE NOT NULL,
PRIMARY KEY ("dni"));

-- -----------------------------------------------------
-- Table "medic"."Usuario"

-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Usuario" ;
CREATE TABLE IF NOT EXISTS "medic"."Usuario" (
    "dniPersona" INT NOT NULL,
    "tipoUsuario" INT NOT NULL,
    "usuario" VARCHAR(50) NOT NULL,
    "contrasenia" VARCHAR(255) NOT NULL, -- Aumentado a 255 para almacenar hashes seguros (ej. SHA256)
    "token" VARCHAR(255) NOT NULL,       -- Nuevo campo para el token
    PRIMARY KEY ("dniPersona"),
    CONSTRAINT "dniPersona_fk" -- Renombrado para evitar conflictos si ya existe
    FOREIGN KEY ("dniPersona")
    REFERENCES "medic"."Persona" ("dni")
);

-- -----------------------------------------------------
-- Table "medic"."Consultorio"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Consultorio" ;
CREATE TABLE IF NOT EXISTS "medic"."Consultorio" (
    "idConsultorio" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Esta es la modificación clave
    "ruc" VARCHAR(11),
    "nombre" VARCHAR(45) NOT NULL,
    "ubicacion" VARCHAR(45) NOT NULL,
    "direccion" VARCHAR(45) NULL,
    "telefono" VARCHAR(8) NULL
);


-- -----------------------------------------------------
-- Table "medic"."Doctor_Trabaja_Consultorio"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Doctor_Trabaja_Consultorio" ;
CREATE TABLE IF NOT EXISTS "medic"."Doctor_Trabaja_Consultorio" (
    "idDoctor" INT NOT NULL,
    "idConsultorio" INT NOT NULL,
    PRIMARY KEY ("idDoctor", "idConsultorio"), -- Clave primaria compuesta
    CONSTRAINT "fk_doctor_usuario" -- Nombre más descriptivo para la FK
        FOREIGN KEY ("idDoctor")
        REFERENCES "medic"."Usuario" ("dniPersona"),
    CONSTRAINT "fk_consultorio" -- Nombre más descriptivo para la FK
        FOREIGN KEY ("idConsultorio")
        REFERENCES "medic"."Consultorio" ("idConsultorio")
);


-- -----------------------------------------------------
-- Table "medic"."Especialidad"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Especialidad" ;
CREATE TABLE IF NOT EXISTS "medic"."Especialidad" (
    "idEspecialidad" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Modificación clave aquí
    "nombre" VARCHAR(45) NOT NULL,
    "descripcion" VARCHAR(255) NULL -- Sugerencia: Aumentar longitud para descripciones más largas
);


-- -----------------------------------------------------
-- Table "medic"."Servicio"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Servicio" ;
CREATE TABLE IF NOT EXISTS "medic"."Servicio" (
    "idServicio" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- ID autoincremental
    "idEspecialidad" INT NOT NULL,
    "nombre" VARCHAR(100) NOT NULL, -- Aumentado a 100 para nombres de servicio
    "descripcion" VARCHAR(500) NULL, -- Aumentado para descripciones detalladas
    "riesgos" VARCHAR(500) NULL,     -- Aumentado para descripciones de riesgos
    CONSTRAINT "fk_especialidad_servicio" -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idEspecialidad")
        REFERENCES "medic"."Especialidad" ("idEspecialidad")
);


-- -----------------------------------------------------
-- Table "medic"."Horario_Doctor"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Horario_Doctor" ;
CREATE TABLE IF NOT EXISTS "medic"."Horario_Doctor" (
    "idHorarioDoctor" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Nuevo ID autoincremental para cada horario
    "idDoctor" INT NOT NULL,
    "dia_semana" VARCHAR(15) NOT NULL, -- Nuevo campo para el día de la semana (Lunes, Martes, etc.)
    "hora_inicio" TIME NOT NULL,       -- Nuevo campo para la hora de inicio
    "hora_fin" TIME NOT NULL,         -- Nuevo campo para la hora de fin
    "duracion_minutos" INT NULL,      -- Duración en minutos, más preciso que DECIMAL
    CONSTRAINT "fk_doctor_horario"
        FOREIGN KEY ("idDoctor")
        REFERENCES "medic"."Usuario" ("dniPersona")
);


-- -----------------------------------------------------
-- Table "medic"."Consultorio_has_Servicio"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Consultorio_has_Servicio" ;
CREATE TABLE IF NOT EXISTS "medic"."Consultorio_has_Servicio" (
    "idConsultorio" INT NOT NULL, -- Renombrado para consistencia
    "idServicio" INT NOT NULL,    -- Renombrado para consistencia
    PRIMARY KEY ("idConsultorio", "idServicio"),
    CONSTRAINT "fk_consultorio_servicio_consultorio" -- Nombre más conciso
        FOREIGN KEY ("idConsultorio")
        REFERENCES "medic"."Consultorio" ("idConsultorio"),
    CONSTRAINT "fk_consultorio_servicio_servicio"    -- Nombre más conciso
        FOREIGN KEY ("idServicio")
        REFERENCES "medic"."Servicio" ("idServicio")
);


-- -----------------------------------------------------
-- Table "medic"."Cita"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Cita" ;
CREATE TABLE IF NOT EXISTS "medic"."Cita" (
    "idCita" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- ID autoincremental
    "idPaciente" INT NOT NULL,
    "idDoctor" INT NOT NULL,
    "motivo" VARCHAR(255) NOT NULL,    -- Aumentado y hecho NOT NULL
    "fecha_hora_inicio" TIMESTAMP NOT NULL, -- Fecha y hora de inicio de la cita
    "fecha_hora_fin" TIMESTAMP NULL,    -- Fecha y hora de fin de la cita (puede ser calculable)
    "estado" VARCHAR(50) NOT NULL DEFAULT 'Pendiente', -- Estado de la cita (ej. Pendiente, Confirmada, Cancelada, Completada)
    "descripcion" VARCHAR(500) NULL,   -- Para notas adicionales o detalles del encuentro
    CONSTRAINT "fk_cita_paciente_usuario" -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idPaciente")
        REFERENCES "medic"."Usuario" ("dniPersona"),
    CONSTRAINT "fk_cita_doctor_usuario" -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idDoctor")
        REFERENCES "medic"."Usuario" ("dniPersona")
);


-- -----------------------------------------------------
-- Table "medic"."Resenia"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Resenia" ;
CREATE TABLE IF NOT EXISTS "medic"."Resenia" (
    "idResenia" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- ID autoincremental
    "idDoctor" INT NOT NULL,                                 -- Cambiado a idDoctor y hecho NOT NULL
    "calificacion" DECIMAL(2,1) NOT NULL,                    -- Calificación (ej. 4.5), hecha NOT NULL
    "comentario" VARCHAR(1000) NULL,                         -- Comentario más largo
    "fecha_resenia" DATE NOT NULL DEFAULT CURRENT_DATE,      -- Fecha de la reseña, autocompletada
    CONSTRAINT "fk_resenia_doctor_usuario"                   -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idDoctor")
        REFERENCES "medic"."Usuario" ("dniPersona")
);


-- -----------------------------------------------------
-- Table "medic"."Diagnostico"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Diagnostico" ;
CREATE TABLE IF NOT EXISTS "medic"."Diagnostico" (
    "idDiagnostico" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Nuevo ID autoincremental para el diagnóstico
    "idCita" INT NOT NULL,                                       -- Clave foránea a la cita
    "descripcion" VARCHAR(1000) NOT NULL,                        -- Descripción detallada del diagnóstico
    "fecha_diagnostico" DATE NOT NULL DEFAULT CURRENT_DATE,      -- Fecha en que se realizó el diagnóstico
    CONSTRAINT "fk_diagnostico_cita"                             -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idCita")
        REFERENCES "medic"."Cita" ("idCita")
);


-- -----------------------------------------------------
-- Table "medic"."Receta"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Receta" ;
CREATE TABLE IF NOT EXISTS "medic"."Receta" (
    "idReceta" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- New auto-incrementing ID for each recipe
    "idCita" INT NOT NULL,                                   -- Foreign key to the Cita
    "descripcion" VARCHAR(1000) NOT NULL,                    -- Detailed description of the prescription
    "fecha_receta" DATE NOT NULL DEFAULT CURRENT_DATE,       -- Date the prescription was issued
    CONSTRAINT "fk_receta_cita"                               -- More descriptive FK name
        FOREIGN KEY ("idCita")
        REFERENCES medic."Cita" ("idCita")
        ON DELETE NO ACTION
        ON UPDATE NO ACTION                                 -- Standard PostgreSQL syntax for FK actions
);


-- -----------------------------------------------------
-- Table "medic"."Recomendaciones"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Recomendaciones" ;
CREATE TABLE IF NOT EXISTS "medic"."Recomendaciones" (
    "idRecomendacion" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Nuevo ID autoincremental para cada recomendación
    "idCita" INT NOT NULL,                                          -- Clave foránea a la cita
    "descripcion" VARCHAR(1000) NOT NULL,                           -- Descripción detallada de la recomendación
    "fecha_recomendacion" DATE NOT NULL DEFAULT CURRENT_DATE,       -- Fecha en que se emitió la recomendación
    CONSTRAINT "fk_recomendacion_cita"                              -- Nombre de la FK más descriptivo
        FOREIGN KEY ("idCita")
        REFERENCES "medic"."Cita" ("idCita")
);


-- -----------------------------------------------------
-- Table "medic"."Detalle_Pago"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Detalle_Pago" ;
DROP TABLE IF EXISTS medic."Detalle_Pago";

CREATE TABLE IF NOT EXISTS medic."Detalle_Pago" (
    "idDetallePago" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- New auto-incrementing ID for each payment detail
    "idCita" INT NOT NULL,                                       -- Foreign key to the Cita
    "monto" DECIMAL(10, 2) NOT NULL,                             -- Amount of the payment (e.g., 1234.56)
    "metodo_pago" VARCHAR(50) NOT NULL,                          -- Payment method (e.g., 'Tarjeta', 'Efectivo', 'Transferencia')
    "estado_pago" VARCHAR(50) NOT NULL DEFAULT 'Pendiente',      -- Status of the payment (e.g., 'Pendiente', 'Pagado', 'Reembolsado')
    "fecha_pago" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,   -- Date and time of the payment
    CONSTRAINT "fk_detalle_pago_cita"                            -- More descriptive FK name
        FOREIGN KEY ("idCita")
        REFERENCES medic."Cita" ("idCita")
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table "medic"."Permisos_Historial"
-- -----------------------------------------------------
DROP TABLE IF EXISTS "medic"."Permisos_Historial" ;
CREATE TABLE IF NOT EXISTS "medic"."Permisos_Historial" (
    "idPermisoHistorial" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- Auto-incrementing primary key
    "idDoctor" INT NOT NULL,                                          -- Doctor granting/receiving permission
    "idPaciente" INT NOT NULL,                                        -- Patient whose history is involved
    "fechaOtorgaPermiso" DATE NOT NULL DEFAULT CURRENT_DATE,          -- Date permission was granted (default to current date)
    "fechaDeniegaPermiso" DATE NULL,                                  -- Date permission was revoked/denied
    "estado" VARCHAR(50) NOT NULL DEFAULT 'Otorgado',                 -- Current status of the permission (e.g., 'Otorgado', 'Denegado', 'Expirado')
    CONSTRAINT "uq_doctor_paciente_permiso" UNIQUE ("idDoctor", "idPaciente"), -- Optional: Ensure only one active permission record per doctor-patient pair
    CONSTRAINT "fk_permisos_historial_doctor"
        FOREIGN KEY ("idDoctor")
        REFERENCES "medic"."Usuario" ("dniPersona"),
    CONSTRAINT "fk_permisos_historial_paciente"
        FOREIGN KEY ("idPaciente")
        REFERENCES "medic"."Usuario" ("dniPersona")
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
    SELECT "dniPersona", "token"
    INTO v_dni, v_token
    FROM medic."Usuario"
    WHERE "usuario" = p_usuario;

    IF NOT FOUND THEN
        mensaje_out := 'ERROR: USUARIO NO ENCONTRADO';
        resultado_out := NULL;
        RETURN;
    END IF;

    -- Verificar si la contraseña es válida
    SELECT "contrasenia"
    INTO v_contrasenia_hash
    FROM medic."Usuario"
    WHERE "dniPersona" = v_dni;

    IF NOT (crypt(p_contrasenia || v_token, v_contrasenia_hash) = v_contrasenia_hash) THEN
        mensaje_out := 'ERROR: CONTRASENIA INCORRECTA';
        resultado_out := NULL;
        RETURN;
    END IF;

    -- Login correcto, retornar el usuario con datos de Persona
    mensaje_out := 'OK';
    OPEN resultado_out FOR
        SELECT u.*, p.*
        FROM medic."Usuario" u
        JOIN medic."Persona" p ON u."dniPersona" = p."dni"
        WHERE u."dniPersona" = v_dni;
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
    IF EXISTS (SELECT 1 FROM medic."Persona" WHERE "dni" = p_dni) THEN
        UPDATE medic."Persona"
        SET "nombres" = p_nombres,
            "apellidos" = p_apellidos,
            "correo" = p_correo,
            "celular" = p_celular,
            "direccion" = p_direccion,
            "fecha_nacimiento" = p_fecha_nacimiento
        WHERE "dni" = p_dni;
    ELSE
        INSERT INTO medic."Persona"(
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
    IF EXISTS (SELECT 1 FROM medic."TipoUsuario" WHERE "id" = p_tipoUsuario) THEN
        -- Si el usuario ya existe y la contraseña no será actualizada
        IF EXISTS (SELECT 1 FROM medic."Usuario" WHERE "dniPersona" = p_dniPersona) THEN
            IF p_actualizar_contrasenia THEN
                -- Generar nuevo token
                v_token := gen_random_uuid();
                -- Hash de la contraseña + token
                v_contrasenia_hashed := crypt(p_contrasenia_plain || v_token, gen_salt('bf'));
    
                UPDATE medic."Usuario"
                SET "tipoUsuario" = p_tipoUsuario,
                    "usuario" = p_usuario,
                    "token" = v_token::text,
                    "contrasenia" = v_contrasenia_hashed
                WHERE "dniPersona" = p_dniPersona;
            ELSE
                -- Actualización normal sin modificar la contraseña ni el token
                UPDATE medic."Usuario"
                SET "tipoUsuario" = p_tipoUsuario,
                    "usuario" = p_usuario
                WHERE "dniPersona" = p_dniPersona;
            END IF;
        ELSE
            -- Insertar nuevo usuario
            v_token := gen_random_uuid();
            v_contrasenia_hashed := crypt(p_contrasenia_plain || v_token, gen_salt('bf'));
    
            INSERT INTO medic."Usuario" (
                "dniPersona", "tipoUsuario", "usuario", "contrasenia", "token"
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
        SELECT 1 FROM medic."Consultorio" WHERE "idConsultorio" = p_idConsultorio
    ) THEN
        UPDATE medic."Consultorio"
        SET "nombre" = p_nombre,
            "ubicacion" = p_ubicacion,
            "direccion" = p_direccion,
            "telefono" = p_telefono,
            --"ruc" = p_ruc
        WHERE "idConsultorio" = p_idConsultorio;
    ELSE
        INSERT INTO medic."Consultorio"(
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
        SELECT 1 FROM medic."Doctor_Trabaja_Consultorio"
        WHERE "idDoctor" = p_idDoctor AND "idConsultorio" = p_idConsultorio
    ) THEN
        -- En este caso, como la tabla solo tiene PKs y no más datos, no hay nada que actualizar
        -- Pero podrías lanzar un NOTICE o simplemente no hacer nada
        RAISE NOTICE 'La relación ya existe. No se requiere actualización.';
    ELSE
        INSERT INTO medic."Doctor_Trabaja_Consultorio"(
            "idDoctor", "idConsultorio"
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
        SELECT 1 FROM medic."Especialidad" WHERE "idEspecialidad" = p_idEspecialidad
    ) THEN
        UPDATE medic."Especialidad"
        SET "nombre" = p_nombre,
            "descripcion" = p_descripcion
        WHERE "idEspecialidad" = p_idEspecialidad;
    ELSE
        INSERT INTO medic."Especialidad"(
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
        SELECT 1 FROM medic."Servicio" WHERE "idServicio" = p_idServicio
    ) THEN
        UPDATE medic."Servicio"
        SET "idEspecialidad" = p_idEspecialidad,
            "nombre" = p_nombre,
            "descripcion" = p_descripcion,
            "riesgos" = p_riesgos
        WHERE "idServicio" = p_idServicio;
    ELSE
        INSERT INTO medic."Servicio"(
            "idEspecialidad", "nombre", "descripcion", "riesgos"
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
        SELECT 1 FROM medic."Horario_Doctor" WHERE "idHorarioDoctor" = p_idHorarioDoctor
    ) THEN
        UPDATE medic."Horario_Doctor"
        SET "idDoctor" = p_idDoctor,
            "dia_semana" = p_dia_semana,
            "hora_inicio" = p_hora_inicio,
            "hora_fin" = p_hora_fin,
            "duracion_minutos" = p_duracion_minutos
        WHERE "idHorarioDoctor" = p_idHorarioDoctor;
    ELSE
        INSERT INTO medic."Horario_Doctor"(
            "idDoctor", "dia_semana", "hora_inicio", "hora_fin", "duracion_minutos"
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
        SELECT 1 FROM medic."Consultorio_has_Servicio"
        WHERE "idConsultorio" = p_idConsultorio AND "idServicio" = p_idServicio
    ) THEN
        RAISE NOTICE 'La relación ya existe. No se requiere actualización.';
    ELSE
        INSERT INTO medic."Consultorio_has_Servicio"(
            "idConsultorio", "idServicio"
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
        SELECT 1 FROM medic."Cita" WHERE "idCita" = p_idCita
    ) THEN
        UPDATE medic."Cita"
        SET "idPaciente" = p_idPaciente,
            "idDoctor" = p_idDoctor,
            "motivo" = p_motivo,
            "fecha_hora_inicio" = p_fecha_hora_inicio,
            "fecha_hora_fin" = p_fecha_hora_fin,
            "estado" = p_estado,
            "descripcion" = p_descripcion
        WHERE "idCita" = p_idCita;
    ELSE
        INSERT INTO medic."Cita"(
            "idPaciente", "idDoctor", "motivo", "fecha_hora_inicio", "fecha_hora_fin", "estado", "descripcion"
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
        SELECT 1 FROM medic."Resenia" WHERE "idResenia" = p_idResenia
    ) THEN
        UPDATE medic."Resenia"
        SET "idDoctor" = p_idDoctor,
            "calificacion" = p_calificacion,
            "comentario" = p_comentario,
            "fecha_resenia" = p_fecha_resenia
        WHERE "idResenia" = p_idResenia;
    ELSE
        INSERT INTO medic."Resenia"(
            "idDoctor", "calificacion", "comentario", "fecha_resenia"
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
        SELECT 1 FROM medic."Diagnostico" WHERE "idDiagnostico" = p_idDiagnostico
    ) THEN
        UPDATE medic."Diagnostico"
        SET "idCita" = p_idCita,
            "descripcion" = p_descripcion,
            "fecha_diagnostico" = p_fecha_diagnostico
        WHERE "idDiagnostico" = p_idDiagnostico;
    ELSE
        INSERT INTO medic."Diagnostico"(
            "idCita", "descripcion", "fecha_diagnostico"
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
        SELECT 1 FROM medic."Receta" WHERE "idReceta" = p_idReceta
    ) THEN
        UPDATE medic."Receta"
        SET "idCita" = p_idCita,
            "descripcion" = p_descripcion,
            "fecha_receta" = p_fecha_receta
        WHERE "idReceta" = p_idReceta;
    ELSE
        INSERT INTO medic."Receta"(
            "idCita", "descripcion", "fecha_receta"
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
        SELECT 1 FROM medic."Recomendaciones" WHERE "idRecomendacion" = p_idRecomendacion
    ) THEN
        UPDATE medic."Recomendaciones"
        SET "idCita" = p_idCita,
            "descripcion" = p_descripcion,
            "fecha_recomendacion" = p_fecha_recomendacion
        WHERE "idRecomendacion" = p_idRecomendacion;
    ELSE
        INSERT INTO medic."Recomendaciones"(
            "idCita", "descripcion", "fecha_recomendacion"
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
        SELECT 1 FROM medic."Permisos_Historial" WHERE "idPermisoHistorial" = p_idPermisoHistorial
    ) THEN
        UPDATE medic."Permisos_Historial"
        SET "idDoctor" = p_idDoctor,
            "idPaciente" = p_idPaciente,
            "fechaOtorgaPermiso" = p_fechaOtorgaPermiso,
            "fechaDeniegaPermiso" = p_fechaDeniegaPermiso,
            "estado" = p_estado
        WHERE "idPermisoHistorial" = p_idPermisoHistorial;
    ELSE
        INSERT INTO medic."Permisos_Historial"(
            "idDoctor", "idPaciente", "fechaOtorgaPermiso", "fechaDeniegaPermiso", "estado"
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
    INSERT INTO medic."Detalle_Pago" (
        "idCita", "monto", "metodo_pago", "estado_pago", "fecha_pago"
    ) VALUES (
        p_idCita, p_monto, p_metodo_pago, p_estado_pago, p_fecha_pago
    );
END;
$$;
