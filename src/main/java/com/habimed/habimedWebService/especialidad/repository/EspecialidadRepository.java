package com.habimed.habimedWebService.especialidad.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.dto.EspecialidadDTO;
import com.habimed.habimedWebService.especialidad.dto.EspecialidadRequest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

}