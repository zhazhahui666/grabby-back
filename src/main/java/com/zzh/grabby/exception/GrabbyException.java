package com.zzh.grabby.exception;

import lombok.Data;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author zzh
 * @date 2018/10/13
 */

@Data
public class GrabbyException extends RuntimeException{
    private Integer code;
    private String msg;

    public GrabbyException(String msg,Integer code,Object... argArray){
        super(msg);
        this.code = code;
        this.msg = MessageFormatter.arrayFormat(msg, argArray).getMessage();
    }

    public GrabbyException(String msg,Object... argArray){
        super(msg);
        this.msg = MessageFormatter.arrayFormat(msg, argArray).getMessage();
    }

}
