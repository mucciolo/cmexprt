package br.net.comexport.api.core.controller;

import br.net.comexport.api.core.util.StringUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public final class ExceptionHandlerController {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    protected String noSuchElement(final Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    protected List<FieldValidationError> methodArgumentNotValid(final MethodArgumentNotValidException e) {

        return e.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldValidationError(err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
                .collect(toList());
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<Object> invalidFormat(final InvalidFormatException e) {
        return new ResponseEntity<>(StringUtils.extractFirstLine(e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class,
                       MissingServletRequestParameterException.class,
                       ConstraintViolationException.class})
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    protected String illegalArgument(final Exception e) {
        return e.getMessage();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    protected List<String> bindException(final BindException e) {
        return e.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(toList());
    }

    @Value
    public static class FieldValidationError {
        String field;
        Object rejectedValue;
        String message;
    }
}