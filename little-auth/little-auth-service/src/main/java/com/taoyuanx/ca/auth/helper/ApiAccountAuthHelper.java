package com.taoyuanx.ca.auth.helper;

import cn.hutool.core.util.StrUtil;
import com.taoyuanx.auth.TokenManager;
import com.taoyuanx.auth.TokenTypeEnum;
import com.taoyuanx.auth.dto.ApiAccountDTO;
import com.taoyuanx.auth.exception.AuthException;
import com.taoyuanx.auth.sign.ISign;
import com.taoyuanx.auth.token.Token;
import com.taoyuanx.auth.utils.IpWhiteCheckUtil;
import com.taoyuanx.ca.auth.utils.RequestUtil;
import com.taoyuanx.ca.auth.config.AuthProperties;
import com.taoyuanx.ca.auth.constants.AuthCaheConstant;
import com.taoyuanx.ca.auth.dto.AuthRefreshRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthRequestDTO;
import com.taoyuanx.ca.auth.dto.AuthResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @author dushitaoyuan
 * @date 2020/2/25
 */
@Slf4j
@Component
public class ApiAccountAuthHelper {
    public static String AUTH_TYPE_RSA = "rsa", AUTH_TYPE_HMAC = "hmac";
    @Autowired
    AuthProperties authProperties;
    @Autowired
    TokenManager tokenManager;

    /**
     * 校验ApiAccount
     *
     * @param apiAccountDTO
     * @param type          认证类型
     */
    public void checkApiAccount(ApiAccountDTO apiAccountDTO, String type) {
        if (null == apiAccountDTO) {
            throw new AuthException("账户不存在");
        }
        String apiAccount = apiAccountDTO.getApiAccount();
        if (AUTH_TYPE_RSA.equals(type)) {
            String apiPub = apiAccountDTO.getApiPub();
            if (StrUtil.isEmpty(apiPub)) {
                throw new AuthException(StrUtil.format("apiAccount[{}],暂未配置公钥", apiAccount));
            }
        }
        if (AUTH_TYPE_HMAC.equals(type)) {
            String apiSecret = apiAccountDTO.getApiSecret();
            if (StrUtil.isEmpty(apiSecret)) {
                throw new AuthException(StrUtil.format("apiAccount[{}],暂未配置密钥", apiAccount));
            }

        }
        if (!apiAccountDTO.isValid()) {
            throw new AuthException(StrUtil.format("apiAccount[{}]的账户无效", apiAccount));
        }
        Date now = new Date();
        /**
         * api账户尚未激活
         */
        Date activeTime = apiAccountDTO.getActiveTime();
        if (activeTime != null && activeTime.after(now)) {
            throw new AuthException(StrUtil.format("apiAccount[{}],尚未激活", apiAccount));
        }
        Date endTime = apiAccountDTO.getEndTime();
        /**
         * api账户已到期
         */
        if (endTime != null && endTime.before(now)) {
            throw new AuthException(StrUtil.format("apiAccount[{}],已到期", apiAccount));
        }
        apiAccountDTO.readToPublicKey();
    }

    //请求过期时间 5分钟
    private static final Long REQUEST_EXPIRE = 5 * 60 * 1000L;

    public void checkAuthRequest(AuthRequestDTO authRequestDTO) {
        if (StringUtils.isEmpty(authRequestDTO.getApiAccount())) {
            throw new AuthException("参数为空:" + "apiAccount");
        }
        if (StringUtils.isEmpty(authRequestDTO.getRandom())) {
            throw new AuthException("参数为空:" + "random");
        }
        if (Objects.isNull(authRequestDTO.getTimestamp()) || String.valueOf(authRequestDTO.getTimestamp()).length() != 13) {
            throw new AuthException("参数非法:" + "timestamp");
        }
        if (StringUtils.isEmpty(authRequestDTO.getSign())) {
            throw new AuthException("参数为空:" + "sign");
        }
        Long now = System.currentTimeMillis();
        if (Math.abs(now - authRequestDTO.getTimestamp()) > REQUEST_EXPIRE) {
            throw new AuthException("请求已过期");
        }
    }

    public void checkAuthRequestSign(AuthRequestDTO authRequestDTO, ISign sign) {
        String signStr = authRequestDTO.getRandom() + authRequestDTO.getApiAccount() + authRequestDTO.getTimestamp();
        String signValue = authRequestDTO.getSign();
        checkAuthRequestSign(signStr, signValue, sign);

    }

    public void checkAuthRequestSign(String signStr, String signValue, ISign sign) {
        try {
            if (!sign.verifySign(signStr, signValue)) {
                throw new AuthException("签名验证失败");
            }
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            log.warn("验证签名异常", e);
            throw new AuthException("签名验证失败");
        }
    }

    public void checkAuthRefreshRequest(AuthRefreshRequestDTO authRefreshRequestDTO) {
        if (StringUtils.isEmpty(authRefreshRequestDTO.getRefreshToken())) {
            throw new AuthException("参数为空:" + "refreshToken");
        }
        if (StringUtils.isEmpty(authRefreshRequestDTO.getSign())) {
            throw new AuthException("参数为空:" + "sign");
        }
    }

    public AuthResultDTO successAuth(ApiAccountDTO apiAccountDTO, HttpServletRequest request) {
        String apiAccount = apiAccountDTO.getApiAccount();
        /**
         * 白名单校验
         */
        if (authProperties.getWhiteIpCheckSwitch()) {

            String whiteIp = apiAccountDTO.getWhiteIp();
            String remoteIp = RequestUtil.getRemoteIp(request);
            if (!IpWhiteCheckUtil.checkWhiteIP(remoteIp, whiteIp)) {
                throw new AuthException(StrUtil.format("api账户[{}],ip为[{}],不在接入白名单[{}]内,请联系管理员修改", apiAccount, remoteIp, whiteIp));

            }
        }
        Long now = System.currentTimeMillis();
        //构造token
        Token token = new Token();
        token.setApiId(apiAccountDTO.getId());
        token.setCreateTime(now);
        token.setEndTime(now + authProperties.getTokenValidTime());
        token.setType(TokenTypeEnum.BUSSINESS.code);
        token.setApiAccount(apiAccount);
        String bussinessToken = tokenManager.createToken(token);
        token.setType(TokenTypeEnum.REFRESH.code);
        token.setEndTime(token.getEndTime() + authProperties.getRefreshTokenAddTime());
        String refreshToken = tokenManager.createToken(token);
        AuthResultDTO authResultDTO = new AuthResultDTO();
        authResultDTO.setBusinessToken(bussinessToken);
        authResultDTO.setRefreshToken(refreshToken);
        authResultDTO.setExpire(authProperties.getTokenValidTime());
        log.info("api账户为:[{}],认证请求成功", apiAccount);
        return authResultDTO;


    }

    public String newRefreshTokenCacheKey(String refreshToken) {
        String refreshTokenCacheKey = AuthCaheConstant.API_ACCOUNT_CACHE_NAME + ":" + Hex.encodeHexString(DigestUtils.md5(refreshToken));
        return refreshTokenCacheKey;
    }
}
