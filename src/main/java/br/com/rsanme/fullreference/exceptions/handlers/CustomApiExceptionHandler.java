package br.com.rsanme.fullreference.exceptions.handlers;

import br.com.rsanme.fullreference.exceptions.BusinessException;
import br.com.rsanme.fullreference.exceptions.CustomEntityAlreadyExists;
import br.com.rsanme.fullreference.exceptions.CustomEntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Projeto: full-reference
 * Desenvolvedor: Reginaldo Santos de Medeiros (regissanme)
 * Data: 13/11/2023
 * Hora: 22:59
 */
@ControllerAdvice
@AllArgsConstructor
public class CustomApiExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<CustomError.FieldWithError> fieldWithErrors = new ArrayList<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String name = ((FieldError) error).getField();
            String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            fieldWithErrors.add(new CustomError.FieldWithError(name, message));
        }

        CustomError error = new CustomError();
        error.setStatus(status.value());
        error.setTimestamp(OffsetDateTime.now());
        error.setTitle("Um ou mais campos inválidos! Preencha corretamente e tente novamente!");
        error.setFields(fieldWithErrors);

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        CustomError error = new CustomError();
        error.setStatus(status.value());
        error.setTimestamp(OffsetDateTime.now());
        error.setTitle(ex.getMessage());

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(CustomEntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleCustomNotFoundException(
            CustomEntityNotFoundException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        CustomError error = new CustomError();
        error.setStatus(status.value());
        error.setTimestamp(OffsetDateTime.now());
        error.setTitle(ex.getMessage());

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(CustomEntityAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleCustomAlreadyExistsException(
            CustomEntityAlreadyExists ex, WebRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;

        CustomError error = new CustomError();
        error.setStatus(status.value());
        error.setTimestamp(OffsetDateTime.now());
        error.setTitle(ex.getMessage());

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }
}
