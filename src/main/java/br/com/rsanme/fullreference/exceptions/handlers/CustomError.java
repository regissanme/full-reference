package br.com.rsanme.fullreference.exceptions.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 22:54
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomError {

    private Integer status;
    private OffsetDateTime timestamp;
    private String title;
    private List<FieldWithError> fields;


    @Getter
    @AllArgsConstructor
    public static class FieldWithError {

        private String fieldName;
        private String message;
    }
}
