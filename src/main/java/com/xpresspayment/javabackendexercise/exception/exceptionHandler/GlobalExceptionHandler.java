package com.xpresspayment.javabackendexercise.exception.exceptionHandler;

import com.xpresspayment.javabackendexercise.exception.customException.ResourceNotFoundException;
import com.xpresspayment.javabackendexercise.exception.customException.ServersideErrorException;
import com.xpresspayment.javabackendexercise.exception.customException.UnauthorizedException;
import com.xpresspayment.javabackendexercise.payload.response.BaseResponsePayload;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponsePayload> resourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        return new ResponseEntity<>(new BaseResponsePayload(false, resourceNotFoundException.getMessage(), HttpStatus.NOT_FOUND.value(),
                LocalDate.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponsePayload> constraintViolationExceptionHandler(ConstraintViolationException exception){
        String message = exception.getConstraintViolations().iterator().next().getMessage();
        return new ResponseEntity<>(new BaseResponsePayload(false, message,
                HttpStatus.BAD_REQUEST.value(), LocalDate.now()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        String message = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(new BaseResponsePayload(false, message,
                HttpStatus.BAD_REQUEST.value(), LocalDate.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> expiredjwtExceptionHandler(ExpiredJwtException exception){
        return new ResponseEntity<>(new BaseResponsePayload(false, exception.getMessage(),
                HttpStatus.UNAUTHORIZED.value(), LocalDate.now()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> unauthorizedExceptionHandler(UnauthorizedException exception){
        return new ResponseEntity<>(new BaseResponsePayload(false, exception.getMessage(),
                HttpStatus.FORBIDDEN.value(), LocalDate.now()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ServersideErrorException.class)
    public ResponseEntity<Object> serversideErrorExceptionHandler(ServersideErrorException exception){
        return new ResponseEntity<>(new BaseResponsePayload(false, exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDate.now()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
