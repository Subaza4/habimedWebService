package com.habimed.habimedWebService.cita.repository;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.domain.model.EstadoCita;
import com.habimed.habimedWebService.cita.dto.CitaDTO;
import com.habimed.habimedWebService.cita.dto.CitaRepositoryDTO;
import com.habimed.habimedWebService.cita.dto.CitaRequest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.CallableStatement;
import java.sql.Types;

@Repository
public class CitaRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final CitaDTO dto = new CitaDTO();

    @Autowired
    public CitaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cita> findAllCita(){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM medic.\"cita\" ");
        //concatenar condiciones
        //sql.append(request.getConditions());
        sql.append("ORDER BY idcita DESC");
        List<Cita> citas = jdbcTemplate.query(sql.toString(), dto.getCitaRowMapper());
        return citas;
    }

    public Cita getCitaById(Integer idCita) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM medic.\"cita\" ");
        sql.append("WHERE idcita = ? ");

        return jdbcTemplate.queryForObject(sql.toString(), dto.getCitaRowMapper(), idCita);
    }

    public List<Cita> findAllCitaByConditions(CitaRepositoryDTO request) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM medic.\"cita\" ");
        //concatenar condiciones
        sql.append(request.getConditions());
        sql.append("ORDER BY idcita DESC");
        sql.append(" LIMIT ? OFFSET ?");
        Integer tamanioPagina = request.getTamanioPagina();
        Integer pagina = (request.getPagina() -1 ) * tamanioPagina;
        List<Cita> citas = jdbcTemplate.query(sql.toString(), dto.getCitaRowMapper(), pagina, tamanioPagina);
        return citas;
    }

    /**
     * Inserta una nueva cita en la base de datos
     * @param request Objeto CitaRequest con los datos de la cita a insertar
     * @return La cita creada con su ID generado
     */
    public Cita insertCita(CitaRequest request) {
        // Validar que los campos requeridos no sean nulos
        if (request.getIdpaciente() == null || request.getIddoctorconsultorio() == null ||
                request.getFechainicio() == null) {
            throw new IllegalArgumentException("Los campos idpaciente, iddoctorconsultorio, motivo y fechainicio son obligatorios");
        }
        
        String sql = "INSERT INTO medic.\"cita\" (idpaciente, iddoctor, motivo, fecha_hora_inicio, " +
                     "fecha_hora_fin, estado, descripcion) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING idcita, idpaciente, iddoctor, motivo, " +
                     "fecha_hora_inicio, fecha_hora_fin, estado, descripcion";
        
        // Utilizamos queryForObject para obtener la cita recién creada
        return jdbcTemplate.queryForObject(sql, dto.getCitaRowMapper(),
                request.getIdusuario(),
                request.getIddoctorconsultorio(),
                request.getMotivo(),
                request.getFechainicio(),
                request.getFechafin(),
                request.getEstado() != null ? request.getEstado() : EstadoCita.PRE_RESERVA, // Valor por defecto si es nulo
                request.getDescripcion()
        );
    }

    /**
     * Actualiza una cita existente en la base de datos
     * @param request Objeto CitaRequest con los datos actualizados de la cita
     * @return La cita actualizada
     */
    public Cita updateCita(CitaRequest request) {
        if(request.getIdcita() == null || request.getIddoctorconsultorio() == null ||
            request.getIdusuario() == null || request.getFechainicio() == null){
        throw new IllegalArgumentException("Los campos identificadores de la cita, doctor, paciente y fecha de inicio son obligatorios");
        }

        String sql;
        Object[] params;

        if(request.getEstado() != null){
            sql = "UPDATE medic.\"cita\" SET idpaciente = ?, iddoctor = ?, motivo = ?, fecha_hora_inicio = ?, " +
                    "fecha_hora_fin = ?, estado = ?, descripcion = ? WHERE idcita = ? RETURNING idcita, idpaciente, " +
                    "iddoctor, motivo, fecha_hora_inicio, fecha_hora_fin, estado, descripcion";

            params = new Object[]{
                request.getIdusuario(),
                request.getIddoctorconsultorio(),
                request.getMotivo(),
                request.getFechainicio(),
                request.getFechafin(),
                request.getEstado(),
                request.getDescripcion(),
                request.getIdcita()
            };
        } else {
            sql = "UPDATE medic.\"cita\" SET idpaciente = ?, iddoctor = ?, motivo = ?, fecha_hora_inicio = ?, " +
                    "fecha_hora_fin = ?, descripcion = ? WHERE idcita = ? RETURNING idcita, idpaciente, iddoctor, " +
                    "motivo, fecha_hora_inicio, fecha_hora_fin, estado, descripcion";

            params = new Object[]{
                request.getIdusuario(),
                request.getIddoctorconsultorio(),
                request.getMotivo(),
                request.getFechainicio(),
                request.getFechafin(),
                request.getDescripcion(),
                request.getIdcita()
            };
        }

        // Ejecutar la actualización y obtener la cita actualizada
        try {
            return jdbcTemplate.queryForObject(sql, dto.getCitaRowMapper(), params);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la cita: " + e.getMessage(), e);
        }
    }

    //delete
    public void deleteCitaById(Integer id) {
        String sql = "DELETE FROM medic.\"cita\" WHERE idcita = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<CitaDTO> getCitas(CitaRequest request) {
        String sql = "SELECT " +
                "c.\"idcita\", " +
                "c.\"fecha_hora_inicio\" AS \"fechacita\", " +
                "c.\"estado\", " +
                "dp.\"estado_pago\" AS \"estadopago\", " +
                "u.\"dnipersona\" AS \"idusuario\", " +
                "CONCAT(p.\"nombres\", ' ', p.\"apellidos\") AS \"nombreusuario\", " +
                "dp.\"iddetallepago\", " +
                "dp.\"monto\" AS \"montopago\", " +
                "cons.\"nombre\" AS \"nombreconsultorio\", " +
                "cons.\"direccion\" AS \"direccionconsultorio\", " +
                "d.\"nombres\" AS \"nombredoctor\" " +
            "FROM medic.\"cita\" c " +
            "JOIN medic.\"usuario\" u ON c.\"idpaciente\" = u.\"dnipersona\" " +
            "JOIN medic.\"persona\" p ON u.\"dnipersona\" = p.\"dni\" " +
            "LEFT JOIN medic.\"detalle_pago\" dp ON c.\"idcita\" = dp.\"idcita\" " +
            "LEFT JOIN medic.\"doctor_trabaja_consultorio\" dtc ON c.\"iddoctor\" = dtc.\"iddoctor\" " +
            "LEFT JOIN medic.\"consultorio\" cons ON dtc.\"idconsultorio\" = cons.\"idconsultorio\" " +
            "LEFT JOIN medic.\"persona\" d ON c.\"iddoctor\" = d.\"dni\" ";
        sql += request.getValuesOfConditions();
        // paginacion
        List<CitaDTO> citas = jdbcTemplate.query(sql, dto.getCitaDTORowMapper());
        return citas;
    }

    public CitaDTO getCita(Integer idCita) {
        String sql = "SELECT " +
                "c.\"idcita\", " +
                "c.\"fecha_hora_inicio\" AS \"fechacita\", " +
                "c.\"estado\", " +
                "dp.\"estado_pago\" AS \"estadopago\", " +
                "u.\"dnipersona\" AS \"idusuario\", " +
                "CONCAT(p.\"nombres\", ' ', p.\"apellidos\") AS \"nombreusuario\", " +
                "dp.\"iddetallepago\", " +
                "dp.\"monto\" AS \"montopago\", " +
                "cons.\"nombre\" AS \"nombreconsultorio\", " +
                "cons.\"direccion\" AS \"direccionconsultorio\", " +
                "d.\"nombres\" AS \"nombredoctor\" " +
            "FROM medic.\"cita\" c " +
            "JOIN medic.\"usuario\" u ON c.\"idpaciente\" = u.\"dnipersona\" " +
            "JOIN medic.\"persona\" p ON u.\"dnipersona\" = p.\"dni\" " +
            "LEFT JOIN medic.\"detalle_pago\" dp ON c.\"idcita\" = dp.\"idcita\" " +
            "LEFT JOIN medic.\"doctor_trabaja_consultorio\" dtc ON c.\"iddoctor\" = dtc.\"iddoctor\" " +
            "LEFT JOIN medic.\"consultorio\" cons ON dtc.\"idconsultorio\" = cons.\"idconsultorio\" " +
            "LEFT JOIN medic.\"persona\" d ON c.\"iddoctor\" = d.\"dni\" WHERE c.idcita = ?";

        CitaDTO cita = jdbcTemplate.queryForObject(sql, dto.getCitaDTORowMapper(), idCita);
        return cita;
    }

    public Integer setCita(CitaRequest cita) {
        String sql = "CALL medic.\"upsert_cita\"(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int[] resultado = new int[1];
        
        try {
            jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
                try (CallableStatement cs = connection.prepareCall(sql)) {
                    // Validar cita
                    if (cita == null) {
                        throw new IllegalArgumentException("La cita no puede ser null");
                    }
                    // Establecer parámetros de entrada
                    cs.setObject(1, cita.getIdcita(), Types.INTEGER);
                    cs.setInt(2, cita.getIdusuario());
                    cs.setInt(3, cita.getIddoctorconsultorio());
                    cs.setString(4, cita.getMotivo());
                    cs.setTimestamp(5, cita.getFechainicio());
                    cs.setTimestamp(6, cita.getFechafin());
                    cs.setString(7, cita.getEstado());
                    cs.setString(8, cita.getDescripcion());
                    // Registrar el parámetro de salida
                    cs.registerOutParameter(9, Types.INTEGER);
                    cs.execute();
                    // Obtener el resultado
                    resultado[0] = cs.getInt(9);
                    return null;
                }
            });
            return resultado[0];
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la cita", e);
        }
    }

    public Boolean deleteCita(Integer idCita) {
        String sql = "DELETE FROM medic.\"cita\" WHERE idcita = ?";
        int rowsAffected = jdbcTemplate.update(sql, idCita);
        return rowsAffected > 0;
    }
}