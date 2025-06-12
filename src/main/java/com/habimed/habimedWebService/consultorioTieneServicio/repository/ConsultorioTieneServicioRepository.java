package com.habimed.habimedWebService.consultorioTieneServicio.repository;
import java.sql.SQLException;
import java.util.List;

import com.habimed.habimedWebService.consultorioTieneServicio.domain.model.ConsultorioTieneServicio;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRepositoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioDTO;
import com.habimed.habimedWebService.consultorioTieneServicio.dto.ConsultorioTieneServicioRequest;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.ConnectionCallback;
import java.sql.CallableStatement;
import java.sql.Types;

@Repository
public class ConsultorioTieneServicioRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ConsultorioTieneServicioDTO dto = new ConsultorioTieneServicioDTO();

    @Autowired
    public ConsultorioTieneServicioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ConsultorioTieneServicio> getAllConsultorios() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM medic.\"consultorio_has_servicio\" c ");

        return jdbcTemplate.query(sql.toString(), dto.consultorioTieneServicioRowMapper());
    }

    public ConsultorioTieneServicio getConsultorioByConsultorio(Integer idconsultorio) {
        String sql = "SELECT * FROM medic.\"consultorio_has_servicio\" WHERE idconsultorio = ?";
        return jdbcTemplate.queryForObject(sql, dto.consultorioTieneServicioRowMapper(), idconsultorio);
    }

    //insert return ConsultorioTieneServicio
    /**
     * Inserta una nueva relación entre consultorio y servicio
     * @param consultorioTieneServicio Objeto con la información a insertar
     * @return El objeto ConsultorioTieneServicio insertado o null si falla la inserción
     * @throws IllegalArgumentException si los datos son inválidos
     * @throws RuntimeException si ocurre un error en la base de datos
     */
    public ConsultorioTieneServicio insertConsultorioTieneServicio(ConsultorioTieneServicio consultorioTieneServicio) {
        // Validar los datos de entrada
        if (consultorioTieneServicio == null ||
                consultorioTieneServicio.getIdconsultorio() == null ||
                consultorioTieneServicio.getIdservicio() == null) {
            throw new IllegalArgumentException("Los IDs de consultorio y servicio son obligatorios");
        }

        String sql = "INSERT INTO medic.\"consultorio_has_servicio\" (\"idconsultorio\", \"idservicio\") VALUES (?, ?)";

        try {
            int rowsAffected = jdbcTemplate.update(
                    sql,
                    consultorioTieneServicio.getIdconsultorio(),
                    consultorioTieneServicio.getIdservicio()
            );

            if (rowsAffected > 0) {
                // Confirmar que se insertó correctamente consultando la base de datos
                String querySql = "SELECT * FROM medic.\"consultorio_has_servicio\" WHERE \"idconsultorio\" = ? AND \"idservicio\" = ?";
                return jdbcTemplate.queryForObject(
                        querySql,
                        dto.consultorioTieneServicioRowMapper(),
                        consultorioTieneServicio.getIdconsultorio(),
                        consultorioTieneServicio.getIdservicio()
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al insertar la relación consultorio-servicio", e);
        }
    }

    /**
     * Actualiza una relación existente entre consultorio y servicio
     * @param oldIdConsultorio ID del consultorio actual
     * @param oldIdServicio ID del servicio actual
     * @param newConsultorioTieneServicio Objeto con los nuevos valores
     * @return El objeto ConsultorioTieneServicio actualizado o null si falla la actualización
     * @throws IllegalArgumentException si los datos son inválidos
     * @throws RuntimeException si ocurre un error en la base de datos
     */
    public ConsultorioTieneServicio updateConsultorioTieneServicio(
            Integer oldIdConsultorio,
            Integer oldIdServicio,
            ConsultorioTieneServicio newConsultorioTieneServicio) {

        // Validar los datos de entrada
        if (oldIdConsultorio == null || oldIdServicio == null ||
                newConsultorioTieneServicio == null ||
                newConsultorioTieneServicio.getIdconsultorio() == null ||
                newConsultorioTieneServicio.getIdservicio() == null) {
            throw new IllegalArgumentException("Todos los IDs son obligatorios para la actualización");
        }

        // Si no hay cambios, simplemente devolver el objeto actual
        if (oldIdConsultorio.equals(newConsultorioTieneServicio.getIdconsultorio()) &&
                oldIdServicio.equals(newConsultorioTieneServicio.getIdservicio())) {
            return newConsultorioTieneServicio;
        }

        // Comenzamos una transacción para asegurar atomicidad
        return jdbcTemplate.execute((ConnectionCallback<ConsultorioTieneServicio>) connection -> {
            boolean originalAutoCommit = false;
            try {
                originalAutoCommit = connection.getAutoCommit();
                connection.setAutoCommit(false);

                // Primero verificamos si existe la relación a actualizar
                String checkSql = "SELECT COUNT(*) FROM medic.\"consultorio_has_servicio\" WHERE \"idconsultorio\" = ? AND \"idservicio\" = ?";
                Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, oldIdConsultorio, oldIdServicio);

                if (count == null || count == 0) {
                    connection.rollback();
                    return null; // No existe la relación a actualizar
                }

                // Verificamos si ya existe la nueva relación para evitar duplicados
                if (!(oldIdConsultorio.equals(newConsultorioTieneServicio.getIdconsultorio()) &&
                        oldIdServicio.equals(newConsultorioTieneServicio.getIdservicio()))) {

                    Integer newCount = jdbcTemplate.queryForObject(checkSql, Integer.class,
                            newConsultorioTieneServicio.getIdconsultorio(),
                            newConsultorioTieneServicio.getIdservicio());

                    if (newCount != null && newCount > 0) {
                        connection.rollback();
                        throw new RuntimeException("Ya existe una relación con los nuevos IDs");
                    }
                }

                // Eliminamos la relación existente
                String deleteSql = "DELETE FROM medic.\"consultorio_has_servicio\" WHERE \"idconsultorio\" = ? AND \"idservicio\" = ?";
                jdbcTemplate.update(deleteSql, oldIdConsultorio, oldIdServicio);

                // Insertamos la nueva relación
                String insertSql = "INSERT INTO medic.\"consultorio_has_servicio\" (\"idconsultorio\", \"idservicio\") VALUES (?, ?)";
                int insertResult = jdbcTemplate.update(
                        insertSql,
                        newConsultorioTieneServicio.getIdconsultorio(),
                        newConsultorioTieneServicio.getIdservicio()
                );

                if (insertResult > 0) {
                    connection.commit();
                    return newConsultorioTieneServicio;
                } else {
                    connection.rollback();
                    return null;
                }
            } catch (Exception e) {
                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex) {
                        throw new RuntimeException("Error al realizar rollback", ex);
                    }
                }
                throw new RuntimeException("Error al actualizar la relación consultorio-servicio", e);
            } finally {
                if (connection != null) {
                    try {
                        connection.setAutoCommit(originalAutoCommit);
                    } catch (SQLException ex) {
                        throw new RuntimeException("Error al restaurar autocommit", ex);
                    }
                }
            }
        });
    }
    //update return ConsultorioTieneServicio

    public List<ConsultorioTieneServicioDTO> getAllConsultoriosServicios(ConsultorioTieneServicioRequest request) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.\"idconsultorio\", c.\"idservicio\", e.\"idespecialidad\", ")
           .append("s.\"nombre\" as servicio_nombre, e.\"nombre\" as especialidad_nombre ")
           .append("FROM medic.\"consultorio_has_servicio\" c ")
           .append("INNER JOIN medic.\"servicio\" s ON c.\"idservicio\" = s.\"idservicio\" ")
           .append("INNER JOIN medic.\"especialidad\" e ON s.\"idespecialidad\" = e.\"idespecialidad\" ");

        if (request != null && !request.getConditions("c").isEmpty()) {
            sql.append(" WHERE ").append(request.getConditions("c"));
        }
        sql.append(" OFFSET ? LIMIT ?");
        Integer size = request.getNum_elementos();
        Integer pagina = (request.getPagina() -1 ) * size;

        return jdbcTemplate.query(sql.toString(), dto.consultorioTieneServicioDTORowMapper(), pagina, size);
    }

    /*public ConsultorioTieneServicioDTO getConsultorioServicioById(ConsultorioTieneServicioRequest request) {
        String sql = "SELECT idconsultoriotienesser, idconsultorio, idservicio FROM medic.\"consultoriotienesser\" ";
        sql += request.getValuesOfConditions();
        return jdbcTemplate.queryForObject(sql, dto.consultorioTieneServicioRowMapper());
    }*/

    public Integer setConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
        String sql = "CALL medic.\"upsert_consultorio_has_servicio\"(?, ?, ?)";
        int[] resultado = new int[1];

        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement cs = connection.prepareCall(sql)) {
                    // Validar request
                    if (request == null) {
                        throw new IllegalArgumentException("La solicitud no puede ser null");
                    }
                    // Establecer parámetros de entrada
                    cs.setInt(1, request.getIdconsultorio());
                    cs.setInt(2, request.getIdservicio());
                    // Registrar el parámetro de salida
                    cs.registerOutParameter(3, Types.INTEGER);
                    cs.execute();
                    // Obtener el resultado
                    resultado[0] = cs.getInt(3);
                    return null;
                }
            });

            return resultado[0];
        } catch (Exception e) {
            throw new RuntimeException("Error al asignar servicio al consultorio", e);
        }
    }

    public Boolean deleteConsultorioTieneServicio(ConsultorioTieneServicioRequest request) {
        String sql = "DELETE FROM medic.\"consultoriotienesser\" WHERE idconsultorio = ? AND idservicio = ?";
        int rowsAffected = jdbcTemplate.update(sql, request.getIdconsultorio(), request.getIdservicio());
        return rowsAffected > 0;
    }
}