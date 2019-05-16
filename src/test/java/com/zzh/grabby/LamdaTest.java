package com.zzh.grabby;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zzh
 * @date 2018/12/25
 */
public class LamdaTest {

    public static void main(String[] args) {
        Integer[] ids = {1,2,3,4,5};
        List<Map<Object, Object>> collect = Arrays.stream(ids).map(item -> {
            Map<Object, Object> map = new HashMap<>();
            map.put("name", "chenliang");
            return map;
        }).collect(Collectors.toList());

        System.out.println(collect);
    }
}
