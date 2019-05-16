package com.zzh.grabby.exception;

import com.zzh.grabby.common.dto.ResultDto;
import com.zzh.grabby.common.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * @author zzh
 * @date 2018/10/26
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(GrabbyException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResultDto handleGrabbyException(GrabbyException e) {
        String msg = "grabby exception";
        if (e != null) {
            msg = e.getMsg();
            log.error(e.toString());
        }
        return ResultUtil.setErrorMsg(e.getCode() == null ? 500 : e.getCode(), msg);
    }

    /**
     * 类参数校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResultDto handleBindException(BindException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        //Lambda的本质实际上是匿名内部类，所以str必须是final类型（不过代码中的final可以省略），是不可以重新赋值的。
        StringBuilder msg = new StringBuilder();
        fieldErrors.forEach(item -> {
            msg.append(item.getDefaultMessage() + "；");
        });
        return ResultUtil.setErrorMsg(500, msg.toString());
    }


    /**
     * 方法级别参数校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResultDto handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        //Lambda的本质实际上是匿名内部类，所以str必须是final类型（不过代码中的final可以省略），是不可以重新赋值的。
        StringBuilder msg = new StringBuilder();
        constraintViolations.forEach(item -> {
            msg.append(item.getMessageTemplate() + "；");
        });
        return ResultUtil.setErrorMsg(500, e.getMessage());
    }


    /**
     * 其他异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResultDto handleException(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return ResultUtil.setErrorMsg(500, e.getMessage());
    }

}
