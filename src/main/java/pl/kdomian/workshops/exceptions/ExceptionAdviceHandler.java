package pl.kdomian.workshops.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionAdviceHandler {

    @ResponseBody
    @ExceptionHandler(ElementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String getElementNotFoundException(ElementNotFoundException ex) {
        return ex.getMessage();
    }


    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getBusinessException(BusinessException ex) {
        return ex.getMessage();
    }


    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String getConstraintValidationException(ConstraintViolationException ex) {
        return ex.getMessage();
    }

}
