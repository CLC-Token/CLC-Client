package com.btd.library.base.util;


import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

/**
 * double除去整数后小数点
 * Created by yzy on 2019/1/5 10:53
 */

public class JsonHelpUtil {
    public static GsonBuilder formatGson(){
        return new GsonBuilder()
                .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
                    if(src == null){
                        return new JsonPrimitive(0);
                    }
                    if(src == src.longValue())
                        return new JsonPrimitive(src.longValue());
                    return new JsonPrimitive(src);
                })
                .registerTypeAdapter(Integer.class, (JsonSerializer<Integer>) (src, typeOfSrc, context) -> {
                    if(src == null){
                        return new JsonPrimitive(0);
                    }
                    if(src == src.longValue())
                        return new JsonPrimitive(src.longValue());
                    return new JsonPrimitive(src);
                })
                .registerTypeAdapter(Long.class, (JsonSerializer<Long>) (src, typeOfSrc, context) -> {
                    if(src == null){
                        return new JsonPrimitive(0);
                    }
                    return new JsonPrimitive(src);
                })
                .registerTypeAdapter(String.class, new StringConverter());
    }
}
