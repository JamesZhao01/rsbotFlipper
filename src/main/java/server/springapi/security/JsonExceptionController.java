package server.springapi.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class JsonExceptionController {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Json Parsing Exception")
    @ExceptionHandler(RuntimeException.class)
    public String handleJsonParseException(HttpServletRequest request, Exception e) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
        return obj.writeValueAsString(new ErrorMessage(e.getMessage()));
    }

    class ErrorMessage {
        String message;
        public ErrorMessage(String message) {
            this.message = message;
        }
    }
}
