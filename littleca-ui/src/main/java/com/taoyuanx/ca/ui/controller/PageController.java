package com.taoyuanx.ca.ui.controller;

import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;
import com.taoyuanx.ca.core.util.CertUtil;
import com.taoyuanx.ca.ui.anno.NeedToken;
import com.taoyuanx.ca.ui.common.CAConstant;
import com.taoyuanx.ca.ui.common.Result;
import com.taoyuanx.ca.ui.common.ResultBuilder;
import com.taoyuanx.ca.ui.config.AppConfig;
import com.taoyuanx.ca.ui.config.CaConfig;
import com.taoyuanx.ca.ui.dto.CertReq;
import com.taoyuanx.ca.ui.dto.CertResult;
import com.taoyuanx.ca.ui.dto.KeyPairResult;
import com.taoyuanx.ca.ui.service.CertService;
import com.taoyuanx.ca.ui.util.SimpleTokenManager;
import com.taoyuanx.ca.ui.util.Validator;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Controller
public class PageController {

    @RequestMapping(value = {"", "/", "index"})
    public String index() {
        return "redirect:login.html";
    }
}
