package com.ote.file.service.controller.exceptionhandler;

import com.ote.file.api.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class FileServiceExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(UserNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(ApplicationNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(PerimeterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(PerimeterNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(FolderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(FolderNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handle(FileNotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Error handle(UnauthorizedException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(LockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Error handle(LockException exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handle(Throwable exception) {
        log.info(exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }
}
