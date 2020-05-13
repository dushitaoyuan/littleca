package com.taoyuanx.auth.client.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.taoyuanx.auth.client.Result;

import java.io.IOException;

/**
 * 字段string 处理
 */
public class ResultFiledStringDeSerializer extends JsonDeserializer<Result> {
    @Override
    public Result deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Result result = new Result();
        if (node.has("code")) {
            result.setCode(node.get("code").asInt());
        }
        if (node.has("msg")) {
            result.setMsg(node.get("msg").asText());
        }
        if (node.has("success")) {
            result.setSuccess(node.get("success").asInt());
        }
        if (node.has("data")) {
            result.setData(node.get("data").toString());
        }
        if (node.has("ext")) {
            result.setExt(node.get("ext").toString());
        }
        return result;
    }
}