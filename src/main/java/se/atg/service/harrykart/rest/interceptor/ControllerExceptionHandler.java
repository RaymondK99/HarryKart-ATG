package se.atg.service.harrykart.rest.interceptor;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler  {

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);



    @ExceptionHandler(value
            = { HttpMessageNotReadableException.class })
    protected ResponseEntity<String> handleException(
            HttpMessageNotReadableException ex, WebRequest request) {

        String bodyOfResponse = "Unable to parse input data.";
        log.warn(bodyOfResponse + ":" + ex.getClass().getName() + "," + ex.getMessage());
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }

    @ExceptionHandler(value
            = { IllegalArgumentException.class })
    protected ResponseEntity<String> handleException(
            IllegalArgumentException ex, WebRequest request) {

        String bodyOfResponse = "Unable to parse input data.";
        log.warn(bodyOfResponse + ":" + ex.getClass().getName() + "," + ex.getMessage());
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }


    @ExceptionHandler(value
            = { Exception.class })
    protected ResponseEntity<String> handleException(
            Exception ex, WebRequest request) {

        String bodyOfResponse = "Internal error.";
        log.warn(bodyOfResponse + ":" + ex.getClass().getName() + "," + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bodyOfResponse);
    }
}
