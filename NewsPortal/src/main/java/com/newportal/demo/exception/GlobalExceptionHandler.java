package com.newportal.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NewsNotFoundExc.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<NewsAppErrorResponse> handleException(NewsNotFoundExc exc){
        NewsAppErrorResponse response = this.setNewsAppErrorResponse(HttpStatus.NOT_FOUND,exc.getMessage());

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({NewsAlreadyExistsExc.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<NewsAppErrorResponse> handleException(NewsAlreadyExistsExc exc){
        NewsAppErrorResponse response = this.setNewsAppErrorResponse(HttpStatus.NOT_FOUND,exc.getMessage());

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({UserNameAlreadyExistsExc.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<NewsAppErrorResponse> handleException(UserNameAlreadyExistsExc exc){
        NewsAppErrorResponse response = this.setNewsAppErrorResponse(HttpStatus.NOT_FOUND,exc.getMessage());

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler({UserNotFoundExc.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<NewsAppErrorResponse> handleException(UserNotFoundExc exc){
        NewsAppErrorResponse response = this.setNewsAppErrorResponse(HttpStatus.NOT_FOUND,exc.getMessage());

        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

    }


    public NewsAppErrorResponse setNewsAppErrorResponse(HttpStatus httpStatus,String msg){
        NewsAppErrorResponse newsAppErrorResponse = new NewsAppErrorResponse();

        newsAppErrorResponse.setStatus(httpStatus.value());
        newsAppErrorResponse.setMessage(msg);
        return newsAppErrorResponse;

    }
}
