package com.taoyuanx.ca.web;

import com.taoyuanx.ca.config.LittleTsaConfig;
import com.taoyuanx.ca.tsa.TsaTranserConstant;
import com.taoyuanx.ca.tsa.TimeStampService;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dushitaoyuan
 * @desc tsa服务
 * @date 2019/7/10
 */
@Controller
public class LittleTsaService {
    Logger LOG = LoggerFactory.getLogger(LittleTsaService.class);

    @Autowired
    TimeStampService timeStampService;
    @Autowired
    LittleTsaConfig littleTsaConfig;

    @RequestMapping(value = "tsa", method = RequestMethod.POST)
    public void tsa(HttpServletRequest req, HttpServletResponse resp) {
        String remoteIp = req.getRemoteAddr();
        try {
            if (!TsaTranserConstant.TIMESTAMP_QUERY_CONTENT_TYPE.equalsIgnoreCase(req.getContentType())) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "content type should be " + TsaTranserConstant.TIMESTAMP_QUERY_CONTENT_TYPE);
                return;
            }
            if (!TsaTranserConstant.TIMESTAMP_QUERY_CONTENT_TYPE.equalsIgnoreCase(req.getContentType())) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "content type should be " + TsaTranserConstant.TIMESTAMP_QUERY_CONTENT_TYPE);
                return;
            }
            if (littleTsaConfig.isUsernameEnabled()) {
                if (!littleTsaConfig.getTsaUsername().equals(req.getHeader("username")) || !littleTsaConfig.getTsaPassword().equals(req.getHeader("password"))) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "username and password must match");
                    return;
                }
            }
            if (req.getContentLength() == 0) {
                LOG.error("timestamp request is empty");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "timestamp request is empty");
                return;
            }
            boolean isBase64 = TsaTranserConstant.isBase64(req.getHeader(TsaTranserConstant.TRANSFER_ENCODING_HEADER));
            TimeStampRequest timeStampRequest = readRequest(req, isBase64);

            LOG.debug("TS Request received from {}", remoteIp);
            TimeStampResponse stampResponse = timeStampService.timestamp(timeStampRequest);
            if (stampResponse == null) {
                LOG.warn("TS Request received from {} is not acceptable", remoteIp);
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Could not generate timestamp response");
                return;
            }
            if (stampResponse.getTimeStampToken() == null) {
                LOG.warn("TS Request received from {} is not acceptable", remoteIp);
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Could not generate timestamp response");
                return;
            }
            byte[] response = stampResponse.getEncoded();
            if (isBase64) {
                resp.setHeader(TsaTranserConstant.TRANSFER_ENCODING_HEADER, TsaTranserConstant.TRANSFER_ENCODING_BASE64);
                response = Base64.encode(response);
                LOG.debug("Responding to {} is in base64", remoteIp);
            } else {
                resp.setHeader(TsaTranserConstant.TRANSFER_ENCODING_HEADER, TsaTranserConstant.TRANSFER_ENCODING_BINARY);
                LOG.debug("Responding to %s is in binary mode", remoteIp);
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType(TsaTranserConstant.TIMESTAMP_REPLY_CONTENT_TYPE);
            resp.setContentLength(response.length);
            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(response);
            outputStream.close();
        } catch (Exception e) {
            LOG.warn("TS Request received from {} is not acceptable", remoteIp);
            try {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Could not generate timestamp response");
            } catch (IOException ex) {
                LOG.error("error,{}", ex);
            }
        }
    }

    private TimeStampRequest readRequest(HttpServletRequest req, boolean isBase64) throws Exception {
        byte[] requestBytes = new byte[req.getContentLength()];
        ServletInputStream inputStream = req.getInputStream();
        IOUtils.read(inputStream, requestBytes);
        if (isBase64) {
            return new TimeStampRequest(Base64.decode(requestBytes));
        } else {
            return new TimeStampRequest(requestBytes);
        }
    }


}
