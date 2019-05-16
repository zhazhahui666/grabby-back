package com.zzh.grabby.config.validator;

import com.zzh.grabby.exception.GrabbyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * @author zzh
 * @date 2019/1/10
 */
public class StringToDateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        Date date = null;
            if (StringUtils.isNotBlank(source)) {
                try {
                    date = new Date( Long.valueOf(source));
                }catch (Exception e){
                    throw new GrabbyException("日期格式有误");
                }
            }
            return date;
    }
}
