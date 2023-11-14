package br.com.rsanme.fullreference.exceptions;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 22:50
 */
public class CustomEntityAlreadyExists extends BusinessException {

    public CustomEntityAlreadyExists(String message) {
        super(message);
    }
}
