package com.taoyuanx.auth.client.impl;

import com.taoyuanx.auth.api.AuthApi;
import com.taoyuanx.auth.client.utils.ThrowUtil;
import com.taoyuanx.auth.dto.request.AuthRefreshRequestDTO;
import com.taoyuanx.auth.dto.request.AuthRequestDTO;
import com.taoyuanx.auth.dto.response.AuthResultDTO;
import com.taoyuanx.auth.dto.response.EncodeResponseDTO;
import com.taoyuanx.auth.client.core.AuthServiceApiEnum;
import com.taoyuanx.auth.client.core.ClientConfig;
import com.taoyuanx.auth.client.utils.OkHttpUtil;
import com.taoyuanx.auth.utils.JSONUtil;
import okhttp3.*;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @date 2020/5/1122:21
 * 实现自动加解密.结果验签
 */
public class AuthClient implements AuthApi {
    private ClientConfig config;
    private boolean authEncode = false;

    public AuthClient(ClientConfig config) {
        this.config = config;
        authEncode = Objects.nonNull(config.getAuthEncode());
    }

    @Override
    public AuthResultDTO auth(AuthRequestDTO authRequestDTO) {
        try {
            authRequestDTO.setSign(config.getSign().sign(authRequestDTO.toSignStr()));
            String data = "";
            if (authEncode) {
                data = JSONUtil.toJsonString(config.getAuthEncode().encode(JSONUtil.toJsonString(authRequestDTO)));
            } else {
                data = JSONUtil.toJsonString(authRequestDTO);
            }
            RequestBody postBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                    , data);
            String response = OkHttpUtil.request(config.getOkHttpClient(),
                    new Request.Builder().url(config.getApiMap().get(AuthServiceApiEnum.AUTH))
                            .post(postBody).build(), String.class);
            if (authEncode) {
                response = config.getAuthEncode().decode(JSONUtil.parseObject(response, EncodeResponseDTO.class));
            }
            return JSONUtil.parseObject(response, AuthResultDTO.class);
        } catch (Exception e) {
            throw ThrowUtil.throwException(e);

        }
    }


    @Override
    public AuthResultDTO authRefresh(AuthRefreshRequestDTO authRefreshRequestDTO) {
        try {
            authRefreshRequestDTO.setSign(config.getSign().sign(authRefreshRequestDTO.toSignStr()));
            String data = "";
            if (authEncode) {
                data = JSONUtil.toJsonString(config.getAuthEncode().encode(JSONUtil.toJsonString(authRefreshRequestDTO)));
            } else {
                data = JSONUtil.toJsonString(authRefreshRequestDTO);
            }
            RequestBody postBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                    , data);

            String response = OkHttpUtil.request(config.getOkHttpClient(),
                    new Request.Builder().url(config.getApiMap().get(AuthServiceApiEnum.AUTH_REFRESH))
                            .post(postBody).build(), String.class);
            if (authEncode) {
                response = config.getAuthEncode().decode(JSONUtil.parseObject(response, EncodeResponseDTO.class));
            }
            return JSONUtil.parseObject(response, AuthResultDTO.class);
        } catch (Exception e) {
            throw ThrowUtil.throwException(e);
        }

    }

    @Override
    public Long timestamp() {
        try (Response response = OkHttpUtil.request(config.getOkHttpClient(),
                new Request.Builder().url(config.getApiMap().get(AuthServiceApiEnum.TIMESTAMP)).get()
                        .build(), Response.class)) {
            return Long.parseLong(response.header("timestamp"));
        } catch (Exception e) {
            throw ThrowUtil.throwException(e);
        }

    }


}
