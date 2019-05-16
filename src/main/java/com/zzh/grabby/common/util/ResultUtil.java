package com.zzh.grabby.common.util;


import com.zzh.grabby.common.dto.ResultDto;

/**
 * @author zzh
 */
public class ResultUtil {

    public static ResultDto setData(Object obj) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(true);
        resultDto.setData(obj);
        resultDto.setCode(200);
        return resultDto;
    }

    public static ResultDto setSuccessMsg(String msg) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(true);
        resultDto.setMessage(msg);
        resultDto.setCode(200);
        resultDto.setData(null);
        return resultDto;
    }

    public static ResultDto setSuccess() {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(true);
        resultDto.setMessage("ok");
        resultDto.setCode(200);
        resultDto.setData(null);
        return resultDto;
    }

    public static ResultDto setData(Integer code,String msg,Object data) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code);
        resultDto.setSuccess(true);
        resultDto.setMessage(msg);
        resultDto.setData(data);
        return resultDto;
    }


    public static ResultDto setData(Object data, String msg) {
        ResultDto resultDto = new ResultDto();
        resultDto.setData(data);
        resultDto.setCode(200);
        resultDto.setMessage(msg);
        return resultDto;
    }




    public static ResultDto setErrorMsg(String msg) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        resultDto.setMessage(msg);
        resultDto.setCode(500);
        return resultDto;
    }

    public static ResultDto setErrorMsg(Integer code, String msg) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        resultDto.setMessage(msg);
        resultDto.setCode(code);
        return resultDto;
    }
}
