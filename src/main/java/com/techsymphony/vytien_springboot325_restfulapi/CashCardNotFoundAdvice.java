package com.techsymphony.vytien_springboot325_restfulapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CashCardNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CashCardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String cashCardNotFoundHandler(CashCardNotFoundException ex) {
        return ex.getMessage();
    }
}
