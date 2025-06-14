package com.habimed.habimedWebService.config;

import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import com.habimed.habimedWebService.detallePago.domain.model.MetodoPagoEnum;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        // Conversión de Enum (Java) → Número (BD)
        /*modelMapper.createTypeMap(DetallePago.class, Map.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getMetodoPago().ordinal(),
                            (dest, v) -> ((Map<String, Object>) dest).put("metodo_pago", v));
                });
        */
        // Conversión de Número (BD) → Enum (Java)
        modelMapper.addConverter(ctx -> {
            Integer ordinal = (Integer) ctx.getSource();
            return MetodoPagoEnum.values()[ordinal];
        }, Integer.class, MetodoPagoEnum.class);

        return modelMapper;
    }
}