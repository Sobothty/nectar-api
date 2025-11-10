package istad.co.nectarapi.exception;

import istad.co.nectarapi.base.BasedError;
import istad.co.nectarapi.base.BasedErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class HandleValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasedErrorResponse handleValidationException(MethodArgumentNotValidException exception) {
        BasedError<List<?>> basedError = new BasedError<>();
        List<Map<String, Object>> errors = new ArrayList<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            Map<String, Object> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("reason", fieldError.getDefaultMessage());
            errors.add(error);
        }

        // Handle global errors (object-level validation errors)
        for (ObjectError objectError : exception.getBindingResult().getGlobalErrors()) {
            Map<String, Object> error = new HashMap<>();
            error.put("object", objectError.getObjectName());
            error.put("reason", objectError.getDefaultMessage());
            errors.add(error);
        }

        basedError.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        basedError.setDescription(errors);
        return new BasedErrorResponse(basedError);
    }

    // Handle BindException (for nested validation errors)
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasedErrorResponse handleBindException(BindException exception) {
        BasedError<List<?>> basedError = new BasedError<>();
        List<Map<String, Object>> errors = new ArrayList<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            Map<String, Object> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("reason", fieldError.getDefaultMessage());
            errors.add(error);
        }

        for (ObjectError objectError : exception.getBindingResult().getGlobalErrors()) {
            Map<String, Object> error = new HashMap<>();
            error.put("object", objectError.getObjectName());
            error.put("reason", objectError.getDefaultMessage());
            errors.add(error);
        }

        basedError.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        basedError.setDescription(errors);
        return new BasedErrorResponse(basedError);
    }

    // Handle ConstraintViolationException (for @Valid on collections)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasedErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        BasedError<List<?>> basedError = new BasedError<>();
        List<Map<String, Object>> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            Map<String, Object> error = new HashMap<>();
            String fieldPath = cleanFieldPath(violation.getPropertyPath().toString());
            error.put("field", fieldPath);
            error.put("reason", violation.getMessage());
            error.put("invalidValue", violation.getInvalidValue());
            errors.add(error);
        }

        basedError.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        basedError.setDescription(errors);
        return new BasedErrorResponse(basedError);
    }

    // Handle JSON parsing errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasedErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        BasedError<String> basedError = new BasedError<>();
        basedError.setCode(HttpStatus.BAD_REQUEST.getReasonPhrase());
        basedError.setDescription("Invalid JSON format or structure: " + exception.getMostSpecificCause().getMessage());
        return new BasedErrorResponse(basedError);
    }

    // Clean the field path by removing method name and parameter name prefixes.
    private String cleanFieldPath(String fieldPath) {
        if (fieldPath == null || fieldPath.isEmpty()) {
            return fieldPath;
        }

        String[] segments = fieldPath.split("\\.");

        if (segments.length < 3) {
            return fieldPath;
        }

        List<String> cleanSegments = new ArrayList<>();
        for (int i = 2; i < segments.length; i++) {
            cleanSegments.add(segments[i]);
        }

        return String.join(".", cleanSegments);
    }
}
