-- Es necesario habilitar la extensión pgcrypto para las funciones de hashing.
-- Si no está instalada, puedes ejecutar: CREATE EXTENSION IF NOT EXISTS pgcrypto;
-- REGISTRAR PERSONAS
CREATE OR REPLACE PROCEDURE medic.upsert_persona(
    IN p_dni BIGINT,
    IN p_nombres VARCHAR,
    IN p_apellidos VARCHAR,
    IN p_correo VARCHAR,
    IN p_celular VARCHAR,
    IN p_direccion VARCHAR,
    IN p_fecha_nacimiento DATE,
    OUT p_resultado INT -- Parámetro de salida para el resultado (0, 1, 2)
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- 4. En caso de no venir el dni como parámetro (p_dni es NULL) se devolverá un valor 0
    IF p_dni IS NULL THEN
        p_resultado := 0;
        RETURN;
    END IF;

    -- 2. En caso de venir con su primary key (dni) se deberá validar que exista un registro con ese numero de dni
    IF EXISTS (SELECT 1 FROM medic."persona" WHERE "dni" = p_dni) THEN
        -- Retornará el valor de 2, de ser así se procederá con su actualización
        UPDATE medic."persona"
        SET "nombres" = p_nombres,
            "apellidos" = p_apellidos,
            "correo" = p_correo,
            "celular" = p_celular,
            "direccion" = p_direccion,
            "fecha_nacimiento" = p_fecha_nacimiento
        WHERE "dni" = p_dni;
        p_resultado := 2; -- Registro actualizado
    ELSE
        -- Caso contrario, se insertará un nuevo registro en la tabla y se retornará el valor de 1
        INSERT INTO medic."persona"(
            "dni", "nombres", "apellidos", "correo", "celular", "direccion", "fecha_nacimiento"
        ) VALUES (
            p_dni, p_nombres, p_apellidos, p_correo, p_celular, p_direccion, p_fecha_nacimiento
        );
        p_resultado := 1; -- Nuevo registro insertado
    END IF;
END;
$$;

-- Es necesario habilitar la extensión pgcrypto para las funciones de hashing.
-- Si no está instalada, puedes ejecutar: CREATE EXTENSION IF NOT EXISTS pgcrypto;
-- LOGIN
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

--REGISTRAR USUARIO
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
    "ruc" = p_ruc
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