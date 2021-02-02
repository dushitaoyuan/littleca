package com.taoyuanx.ca.shellui.controller;

import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taoyuanx.ca.shell.excutors.ShellExecutor;
import com.taoyuanx.ca.shell.params.ShellParam;
import com.taoyuanx.ca.shell.params.ShellType;
import com.taoyuanx.ca.shellui.anno.NeedToken;
import com.taoyuanx.ca.shellui.common.Result;
import com.taoyuanx.ca.shellui.common.ResultBuilder;
import com.taoyuanx.ca.shellui.config.AppConfig;
import com.taoyuanx.ca.shellui.dto.CertReq;
import com.taoyuanx.ca.shellui.service.CertService;
import com.taoyuanx.ca.shellui.util.CommonUtil;
import com.taoyuanx.ca.shellui.util.SimpleTokenManager;
import com.taoyuanx.ca.shellui.util.Validator;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "api")
@NeedToken(isNeed = true)
public class ApiController {
    static Logger LOG = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    SimpleTokenManager tokenManager;
    @Autowired
    ShellExecutor shellExecutor;
    @Autowired
    AppConfig appConfig;

    /**
     * 登录验证
     *
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @NeedToken(isNeed = false)
    public Result<JSONObject> login(@RequestBody JSONObject param) throws Exception {
        String username = param.getString("username");
        String password = param.getString("password");
        Validator.validator()
                .isEqueal(appConfig.getUsername(), username, "账户信息不匹配")
                .isEqueal(appConfig.getPassword(), password, "账户信息不匹配");
        String token = tokenManager.createToken(new HashMap<String, String>(), 30L, TimeUnit.MINUTES);
        JSONObject result = new JSONObject();
        result.put("token", token);
        result.put("username", username);
        return ResultBuilder.buildOkResult(result);
    }


    /**
     * 公私钥对生成
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "cert", method = RequestMethod.POST)
    public Result<JSONObject> cert(@RequestBody CertReq certReq) throws Exception {
        String serialNumber = System.currentTimeMillis() + "";
        shellExecutor.execute(newShellParam(certReq, serialNumber));
        return ResultBuilder.buildOkStringResult(serialNumber);
    }

    @RequestMapping(value = "downCertZip", method = RequestMethod.GET)
    public void downCertZip(String serialNumber, String password, HttpServletResponse resp, HttpServletRequest req) throws SerialException {
        try {
            File zipFile = new File(appConfig.getCertBaseDir(), Math.abs(HashUtil.bkdrHash(serialNumber)) + "_cert.zip");
            String destPath = zipFile.getAbsolutePath();
            if (!zipFile.exists()) {
                String certDir = appConfig.getCertBaseDir() + File.separator + serialNumber;
                String readmeDoc = certDir + File.separator + "readme.txt";
                String readmeTxt = "p12 alias:lient\r\n p12 密码:" + password;
                FileUtils.writeStringToFile(new File(readmeDoc), readmeTxt, "UTF-8");
                ZipUtil.zip(certDir, destPath);
            }
            // 下载
            resp.setContentType(req.getServletContext().getMimeType(zipFile.getName()));
            resp.setHeader("Content-type", "application/octet-stream");
            resp.setHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode(serialNumber + "_cert.zip", "UTF-8"));
            resp.getOutputStream().write(FileUtils.readFileToByteArray(zipFile));
        } catch (Exception e) {
            LOG.error("下载异常", e);
            throw new SerialException("下载失败");
        }
    }

    private ShellParam newShellParam(CertReq certReq, String serialNumber) {
        ShellParam shellParam = new ShellParam();
        if (StringUtils.hasText(certReq.getCertExpireDay())) {
            shellParam.setCertExpireDay(certReq.getCertExpireDay());
        }

        if (StringUtils.hasText(certReq.getCertPassword())) {
            shellParam.setCertPassword(certReq.getCertPassword());
        }

        if (StringUtils.hasText(certReq.getKeySize())) {
            shellParam.setRsaBitNum(certReq.getKeySize());
        }
        shellParam.setCertSubect(certReq.getSubject());
        String path = appConfig.getCertBaseDir() + serialNumber;
        shellParam.setCreateCertDir(path);
        CommonUtil.forceBuildPath(path);
        shellParam.setOpensslConfPath(appConfig.getOpensslConfPath());
        shellParam.setOpensslCaPrivateKeyPath(appConfig.getOpensslCaPrivateKeyPath());
        shellParam.setShellType(ShellType.valueOf(appConfig.getShellType()));
        shellParam.setShellPath(appConfig.getShellPath());
        return shellParam;
    }

}
