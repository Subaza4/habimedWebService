-- -----------------------------------------------------
-- Schema medic
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS "medic";

-- -----------------------------------------------------
-- Table "medic"."tipousuario"
-- 1 paciente/cliente
-- 2 doctor
-- 3 consultor
-- 4 administrador
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."tipousuario" (
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "nombre" VARCHAR(45) NOT NULL,
    "descripcion" VARCHAR(250) NOT NULL
);

-- -----------------------------------------------------
-- Table "medic"."persona"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."persona" (
    "dni" BIGINT NOT NULL,
    "nombres" VARCHAR(45) NOT NULL,
    "apellidos" VARCHAR(45) NOT NULL,
    --"correo" VARCHAR(45),
    "celular" VARCHAR(9) NULL,
    "direccion" VARCHAR(45) NULL,
    "fecha_nacimiento" DATE NULL,
    PRIMARY KEY ("dni")
);

-- Tabla usuario con el nombre de columna actualizado
CREATE TABLE IF NOT EXISTS "medic"."usuario" (
    "idusuario" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "dnipersona" BIGINT NOT NULL,
    "tipousuario" INT NOT NULL,
    "usuario" VARCHAR(50) NOT NULL,
    "contrasenia" VARCHAR(255) NOT NULL,
    "token" VARCHAR(255) NOT NULL,
    "estado" BOOLEAN DEFAULT FALSE,
    CONSTRAINT "dnipersona_fk" FOREIGN KEY ("dnipersona")
        REFERENCES "medic"."persona" ("dni"),
    CONSTRAINT "fk_tipousuario" FOREIGN KEY ("tipousuario")
        REFERENCES "medic"."tipousuario" ("id")
);

-- -----------------------------------------------------
-- Table "medic"."consultorio"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."login"(
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "idusuario" INT NOT NULL,
    "token" VARCHAR(255) NOT NULL,
    "estado" BOOLEAN DEFAULT TRUE,
    "fecha_creacion" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "fecha_expiracion" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "fk_usuario" FOREIGN KEY ("idusuario")
        REFERENCES "medic"."usuario" ("idusuario")
);

-- -----------------------------------------------------
-- Table "medic"."consultorio"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."consultorio" (
    "idconsultorio" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "ruc" VARCHAR(11),
    "nombre" VARCHAR(45) NOT NULL,
    "ubicacion" VARCHAR(45) NOT NULL,
    "direccion" VARCHAR(45) NULL,
    "telefono" VARCHAR(9) NULL
);

-- Tabla doctor_trabaja_consultorio
CREATE TABLE IF NOT EXISTS "medic"."doctor_trabaja_consultorio" (
    "iddoctor" INT NOT NULL,
    "idconsultorio" INT NOT NULL,
    PRIMARY KEY ("iddoctor", "idconsultorio"),
    CONSTRAINT "fk_doctor_usuario" FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("idusuario"),
    CONSTRAINT "fk_consultorio" FOREIGN KEY ("idconsultorio")
        REFERENCES "medic"."consultorio" ("idconsultorio")
);

-- -----------------------------------------------------
-- Table "medic"."especialidad"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."especialidad" (
    "idespecialidad" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "nombre" VARCHAR(45) NOT NULL,
    "descripcion" VARCHAR(255) NULL
);

-- -----------------------------------------------------
-- Table "medic"."servicio"
-- Depende de "medic"."especialidad"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."servicio" (
    "idservicio" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "idespecialidad" INT NOT NULL,
    "nombre" VARCHAR(100) NOT NULL,
    "descripcion" VARCHAR(500) NULL,
    "riesgos" VARCHAR(500) NULL,
    CONSTRAINT "fk_especialidad_servicio" FOREIGN KEY ("idespecialidad")
        REFERENCES "medic"."especialidad" ("idespecialidad")
);

-- Tabla horario_doctor
CREATE TABLE IF NOT EXISTS "medic"."horario_doctor" (
    "idhorariodoctor" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "iddoctor" INT NOT NULL,
    "dia_semana" VARCHAR(15) NOT NULL,
    "hora_inicio" TIME NOT NULL,
    "hora_fin" TIME NOT NULL,
    "duracion_minutos" INT NULL,
    CONSTRAINT "fk_doctor_horario" FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("idusuario")
);

-- -----------------------------------------------------
-- Table "medic"."consultorio_has_servicio"
-- Depende de "medic"."consultorio" y "medic"."servicio"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."consultorio_has_servicio" (
    "idconsultorio" INT NOT NULL,
    "idservicio" INT NOT NULL,
    PRIMARY KEY ("idconsultorio", "idservicio"),
    CONSTRAINT "fk_consultorio_servicio_consultorio" FOREIGN KEY ("idconsultorio")
        REFERENCES "medic"."consultorio" ("idconsultorio"),
    CONSTRAINT "fk_consultorio_servicio_servicio" FOREIGN KEY ("idservicio")
        REFERENCES "medic"."servicio" ("idservicio")
);

-- Tabla cita
CREATE TABLE IF NOT EXISTS "medic"."cita" (
    "idcita" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "idpaciente" INT NOT NULL,
    "iddoctor" INT NOT NULL,
    "motivo" VARCHAR(255) NOT NULL,
    "fecha_hora_inicio" TIMESTAMP NOT NULL,
    "fecha_hora_fin" TIMESTAMP NULL,
    "estado" VARCHAR(50) NOT NULL DEFAULT 'Pendiente',
    "descripcion" VARCHAR(500) NULL,
    CONSTRAINT "fk_cita_paciente_usuario" FOREIGN KEY ("idpaciente")
        REFERENCES "medic"."usuario" ("idusuario"),
    CONSTRAINT "fk_cita_doctor_usuario" FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("idusuario")
);

-- Tabla resenia
CREATE TABLE IF NOT EXISTS "medic"."resenia" (
    "idresenia" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "iddoctor" INT NOT NULL,
    "calificacion" DECIMAL(2,1) NOT NULL,
    "comentario" VARCHAR(1000) NULL,
    "fecha_resenia" DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT "fk_resenia_doctor_usuario" FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("idusuario")
);

-- -----------------------------------------------------
-- Table "medic"."diagnostico"
-- Depende de "medic"."cita"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."diagnostico" (
    "iddiagnostico" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "idcita" INT NOT NULL,
    "descripcion" VARCHAR(1000) NOT NULL,
    "fecha_diagnostico" DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT "fk_diagnostico_cita" FOREIGN KEY ("idcita")
        REFERENCES "medic"."cita" ("idcita")
);

-- -----------------------------------------------------
-- Table "medic"."receta"
-- Depende de "medic"."cita"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."receta" (
    "idreceta" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "idcita" INT NOT NULL,
    "descripcion" VARCHAR(1000) NOT NULL,
    "fecha_receta" DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT "fk_receta_cita" FOREIGN KEY ("idcita")
        REFERENCES "medic"."cita" ("idcita")
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- -----------------------------------------------------
-- Table "medic"."recomendaciones"
-- Depende de "medic"."cita"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."recomendaciones" (
    "idrecomendacion" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "idcita" INT NOT NULL,
    "descripcion" VARCHAR(1000) NOT NULL,
    "fecha_recomendacion" DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT "fk_recomendacion_cita" FOREIGN KEY ("idcita")
        REFERENCES "medic"."cita" ("idcita")
);

-- -----------------------------------------------------
-- Table "medic"."detalle_pago"
-- Depende de "medic"."cita"
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS "medic"."detalle_pago" (
    "iddetallepago" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "idcita" INT NOT NULL,
    "monto" DECIMAL(10, 2) NOT NULL,
    "metodo_pago" INT NOT NULL,
    "estado_pago" INT NOT NULL DEFAULT 'PENDIENTE',
    "fecha_pago" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT "fk_detalle_pago_cita" FOREIGN KEY ("idcita")
        REFERENCES "medic"."cita" ("idcita")
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
);

-- Tabla permisos_historial
CREATE TABLE IF NOT EXISTS "medic"."permisos_historial" (
    "idpermisohistorial" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "iddoctor" INT NOT NULL,
    "idpaciente" INT NOT NULL,
    "fechaotorgapermiso" DATE NOT NULL DEFAULT CURRENT_DATE,
    "fechadeniegapermiso" DATE NULL,
    "estado" VARCHAR(50) NOT NULL DEFAULT 'Otorgado',
    CONSTRAINT "uq_doctor_paciente_permiso" UNIQUE ("iddoctor", "idpaciente"),
    CONSTRAINT "fk_permisos_historial_doctor" FOREIGN KEY ("iddoctor")
        REFERENCES "medic"."usuario" ("idusuario"),
    CONSTRAINT "fk_permisos_historial_paciente" FOREIGN KEY ("idpaciente")
        REFERENCES "medic"."usuario" ("idusuario")
);