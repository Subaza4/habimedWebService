/*
 1. **Códigos de retorno**:
    - 1: Inserción exitosa
    - 2: Actualización exitosa
    - 3: Usuario no existe
    - 4: Parámetros nulos
    - 5: DNI nulo
    - 6: DNI no existe
    - 7: Tipo usuario no existe
 */
\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
CREATE OR REPLACE FUNCTION medic.upsert_usuario(
    p_idusuario INT DEFAULT NULL,           -- ID del usuario (NULL para inserción)
    p_dnipersona BIGINT DEFAULT NULL,      -- DNI de la persona
    p_tipousuario INT DEFAULT NULL,        -- Tipo de usuario
    p_usuario VARCHAR(50) DEFAULT NULL,     -- Nombre de usuario
    p_contrasenia_plain VARCHAR DEFAULT NULL, -- Contraseña sin procesar
    p_actualizar_contrasenia BOOLEAN DEFAULT FALSE, -- Flag para actualizar contraseña
    p_estado BOOLEAN DEFAULT TRUE           -- Estado del usuario
)
    RETURNS INT AS $$
DECLARE
    v_registro_existe BOOLEAN;
    v_dni_persona_existe BOOLEAN;
    v_token UUID;
    v_contrasenia_hashed TEXT;
BEGIN
    -- Validación de parámetros nulos
    IF p_dnipersona IS NULL AND
       p_tipousuario IS NULL AND
       p_usuario IS NULL AND
       p_contrasenia_plain IS NULL AND
       NOT p_actualizar_contrasenia THEN
        RETURN 4; -- Todos los parámetros son nulos
    END IF;

    -- Validación del tipo de usuario
    IF NOT EXISTS (SELECT 1 FROM medic.tipousuario WHERE id = p_tipousuario) THEN
        RETURN 7; -- Tipo de usuario no existe
    END IF;

    -- Proceso de inserción
    IF p_idusuario IS NULL THEN
        -- Validar DNI
        IF p_dnipersona IS NULL THEN
            RETURN 5; -- DNI es nulo
        END IF;

        -- Validar existencia de persona
        SELECT EXISTS(SELECT 1 FROM medic.persona WHERE dni = p_dnipersona)
        INTO v_dni_persona_existe;

        IF NOT v_dni_persona_existe THEN
            RETURN 6; -- DNI no existe en tabla persona
        END IF;

        -- Generar token y hash de contraseña
        v_token := gen_random_uuid();
        v_contrasenia_hashed := crypt(p_contrasenia_plain || v_token::text, gen_salt('bf'));

        -- Insertar nuevo usuario
        INSERT INTO medic.usuario (
            dnipersona,
            tipousuario,
            usuario,
            contrasenia,
            token,
            estado
        ) VALUES (
                     p_dnipersona,
                     p_tipousuario,
                     p_usuario,
                     v_contrasenia_hashed,
                     v_token::text,
                     COALESCE(p_estado, TRUE)
                 );
        RETURN 1; -- Inserción exitosa

    -- Proceso de actualización
    ELSE
        -- Verificar existencia del usuario
        SELECT EXISTS(SELECT 1 FROM medic.usuario WHERE idusuario = p_idusuario)
        INTO v_registro_existe;

        IF NOT v_registro_existe THEN
            RETURN 3; -- Usuario no existe
        END IF;

        -- Actualización con posible cambio de contraseña
        IF p_actualizar_contrasenia THEN
            v_token := gen_random_uuid();
            v_contrasenia_hashed := crypt(p_contrasenia_plain || v_token::text, gen_salt('bf'));

            UPDATE medic.usuario
            SET tipousuario = COALESCE(p_tipousuario, tipousuario),
                usuario = COALESCE(p_usuario, usuario),
                contrasenia = v_contrasenia_hashed,
                token = v_token::text,
                estado = COALESCE(p_estado, estado)
            WHERE idusuario = p_idusuario;
        ELSE
            -- Actualización sin cambio de contraseña
            UPDATE medic.usuario
            SET tipousuario = COALESCE(p_tipousuario, tipousuario),
                usuario = COALESCE(p_usuario, usuario),
                estado = COALESCE(p_estado, estado)
            WHERE idusuario = p_idusuario;
        END IF;
        RETURN 2; -- Actualización exitosa
    END IF;
END;
$$ LANGUAGE plpgsql;
-- Este procedimiento almacenado, implementado como una función, gestiona la inserción
-- y actualización de registros en la tabla "medic.usuario".
--
-- Parámetros:
-- p_idusuario:       INT       - Identificador único del usuario.
--                                  - Si es NULL, se intenta una inserción.
--                                  - Si se proporciona, se intenta una actualización.
-- p_dnipersona:      BIGINT    - DNI de la persona asociada al usuario.
--                                  - Requerido y validado para inserción.
--                                  - No se puede actualizar una vez insertado.
-- p_tipousuario:     INT       - Tipo de usuario (referencia a medic.tipousuario).
-- p_usuario:         VARCHAR(50)- Nombre de usuario para login.
-- p_contrasenia:     VARCHAR(255)- Contraseña del usuario.
-- p_token:           VARCHAR(255)- Token de autenticación/sesión.
-- p_estado:          BOOLEAN   - Estado del usuario (TRUE/FALSE).
--
-- Códigos de Retorno:
-- 1:  Inserción exitosa del nuevo usuario.
-- 2:  Actualización exitosa del usuario existente.
-- 3:  El 'idusuario' proporcionado para actualizar no existe.
-- 4:  Todos los parámetros de datos son nulos (excluyendo 'p_idusuario').
-- 5:  'dnipersona' es nulo o no proporcionado durante un intento de inserción.
-- 6:  'dnipersona' proporcionado para la inserción no existe en la tabla 'medic.persona'.
--
-- Dependencias:
--   - La tabla "medic.persona" debe existir en el esquema "medic".
--   - La tabla "medic.tipousuario" debe existir en el esquema "medic".
--
-- Uso:
-- SELECT medic.manage_usuario(NULL, 1234567890, 1, 'nuevo.usuario', 'hashedpass', 'token123', TRUE); -- Inserción
-- SELECT medic.manage_usuario(1, 1234567890, 2, 'user.update', 'newhashedpass', NULL, FALSE); -- Actualización
-- SELECT medic.manage_usuario(100, NULL, NULL, NULL, NULL, NULL, NULL); -- Intento de actualización con ID inexistente
-- SELECT medic.manage_usuario(NULL, NULL, NULL, NULL, NULL, NULL, NULL); -- Intento de inserción con todos los campos nulos
-- SELECT medic.manage_usuario(NULL, 9999999999, 1, 'test', 'pass', 'tok', TRUE); -- Intento de inserción con DNI no existente
-- SELECT medic.manage_usuario(NULL, NULL, 1, 'test', 'pass', 'tok', TRUE); -- Intento de inserción con DNI nulo

CREATE OR REPLACE FUNCTION medic.manage_usuario(
    p_idusuario INT DEFAULT NULL,         -- Identificador único del usuario (NULL para inserción)
    p_dnipersona BIGINT DEFAULT NULL,     -- DNI de la persona asociada al usuario
    p_tipousuario INT DEFAULT NULL,       -- Tipo de usuario
    p_usuario VARCHAR(50) DEFAULT NULL,   -- Nombre de usuario
    p_contrasenia VARCHAR(255) DEFAULT NULL, -- Contraseña del usuario
    p_token VARCHAR(255) DEFAULT NULL,    -- Token de sesión/autenticación
    p_estado BOOLEAN DEFAULT NULL         -- Estado del usuario (activo/inactivo)
)
RETURNS INT AS $$
DECLARE
v_registro_existe BOOLEAN;    -- Variable para verificar la existencia de un registro de usuario
    v_dni_persona_existe BOOLEAN; -- Variable para verificar la existencia del DNI en la tabla persona
BEGIN
    -- 4. En caso de ingresar solo valores nulos (excluyendo p_idusuario para la verificación)
    -- Si todos los parámetros de datos son nulos, se considera una operación sin información útil.
    IF p_dnipersona IS NULL AND
       p_tipousuario IS NULL AND
       p_usuario IS NULL AND
       p_contrasenia IS NULL AND
       p_token IS NULL AND
       p_estado IS NULL THEN
        RETURN 4; -- Retorna 4: Todos los valores de datos son nulos
END IF;

    -- Si p_idusuario es nulo, se procede con la lógica de inserción (Requisito 1)
    IF p_idusuario IS NULL THEN
        -- Validar que 'p_dnipersona' no sea nulo para la inserción
        IF p_dnipersona IS NULL THEN
            RETURN 5; -- Retorna 5: DNI es nulo o no proporcionado para la inserción
END IF;

        -- Validar que el 'p_dnipersona' exista en la tabla "medic.persona"
SELECT EXISTS(SELECT 1 FROM medic.persona WHERE dni = p_dnipersona)
INTO v_dni_persona_existe;

IF NOT v_dni_persona_existe THEN
            RETURN 6; -- Retorna 6: El DNI proporcionado no existe en la tabla "medic.persona"
END IF;

        -- Realizar la inserción del nuevo usuario.
        -- 'idusuario' es GENERATED ALWAYS AS IDENTITY, por lo que la base de datos lo generará.
        -- Se usan COALESCE para proporcionar valores por defecto si los parámetros son NULL,
        -- especialmente para columnas NOT NULL que no tienen un valor DEFAULT directo definido o deseado.
INSERT INTO medic.usuario (
    dnipersona,
    tipousuario,
    usuario,
    contrasenia,
    token,
    estado
) VALUES (
             p_dnipersona,
             COALESCE(p_tipousuario, (SELECT id FROM medic.tipousuario LIMIT 1)), -- Asume el primer tipo de usuario si p_tipousuario es NULL
             p_usuario, -- Si p_usuario es NULL, se insertará NULL
             p_contrasenia, -- Si p_contrasenia es NULL, se insertará NULL
             p_token, -- Si p_token es NULL, se insertará NULL
             COALESCE(p_estado, FALSE) -- Si p_estado es NULL, usará FALSE (valor por defecto de la tabla)
         );
RETURN 1; -- Retorna 1: Inserción exitosa
ELSE
        -- Si p_idusuario no es nulo, se procede con la lógica de actualización (Requisitos 2 y 3)

        -- 3. En caso de ingresar un 'idusuario' inexistente
SELECT EXISTS(SELECT 1 FROM medic.usuario WHERE idusuario = p_idusuario)
INTO v_registro_existe;

IF NOT v_registro_existe THEN
            RETURN 3; -- Retorna 3: El 'idusuario' proporcionado no existe
END IF;

        -- 2. Actualizar el registro existente con los nuevos valores.
        -- Regla 5: Nunca se podrá actualizar el parámetro 'dnipersona'.
        -- COALESCE(nuevo_valor, valor_actual): Si el nuevo valor es NULL, mantiene el valor actual de la columna.
UPDATE medic.usuario
SET
    tipousuario = COALESCE(p_tipousuario, tipousuario),
    usuario = COALESCE(p_usuario, usuario),
    contrasenia = COALESCE(p_contrasenia, contrasenia),
    token = COALESCE(p_token, token),
    estado = COALESCE(p_estado, estado)
WHERE
    idusuario = p_idusuario;

RETURN 2; -- Retorna 2: Actualización exitosa
END IF;

END;
$$ LANGUAGE plpgsql;
