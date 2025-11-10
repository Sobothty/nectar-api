package istad.co.nectarapi.exception;

import istad.co.nectarapi.base.BasedError;
import istad.co.nectarapi.base.BasedErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class HandleServiceException {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleServiceException(ResponseStatusException exception){
        BasedError<String> basedError = new BasedError<>();
        basedError.setCode(exception.getStatusCode().toString());
        basedError.setDescription(exception.getReason());

        BasedErrorResponse basedErrorResponse = new BasedErrorResponse();
        basedErrorResponse.setError(basedError);
        return new ResponseEntity<>(basedErrorResponse,exception.getStatusCode());
    }
}
