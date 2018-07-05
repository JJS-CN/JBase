package com.jjs.base.http;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.jjs.base.entity.JBaseEntity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 说明：解析内容
 * Created by jjs on 2018/7/2.
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            return adapter.read(jsonReader);
        } catch (final JsonParseException e) {
            e.printStackTrace();
            JBaseEntity JBaseEntity = new JBaseEntity() {
                @Override
                public boolean isSuccess() {
                    return false;
                }

                @Override
                public String message() {
                    return "json转换失败\n" + e.getMessage();
                }
            };
            return (T) JBaseEntity;
        } finally {
            value.close();
        }
    }
}
