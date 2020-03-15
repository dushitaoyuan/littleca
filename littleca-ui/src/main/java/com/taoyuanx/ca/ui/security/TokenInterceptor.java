package com.taoyuanx.ca.ui.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taoyuanx.ca.ui.util.CookieUtil;
import com.taoyuanx.ca.ui.util.SimpleTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taoyuanx.ca.ui.anno.NeedToken;

/**
 * @author 都市桃源
 * token校验
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    SimpleTokenManager tokenManager;

    @Override
    public void afterCompletion(HttpServletRequest req,
                                HttpServletResponse resp, Object handler, Exception e)
            throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp,
                           Object handler, ModelAndView view) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
                             Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        NeedToken methodTokenAnno = handlerMethod.getMethod().getAnnotation(NeedToken.class);
        if (null != methodTokenAnno) {
            if (methodTokenAnno.isNeed()) {
                String token = getToken(req);
                tokenManager.vafy(token);
            } else {
                return true;
            }
        }
        NeedToken classTokenAnno = handlerMethod.getBeanType().getAnnotation(NeedToken.class);
        if (null != classTokenAnno) {
            if (classTokenAnno.isNeed()) {
                String token = getToken(req);
                tokenManager.vafy(token);
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * 获取方式:cookie header 参数
     *
     * @param req
     * @return
     */
    public String getToken(HttpServletRequest req) {
        String token = CookieUtil.getCookieValue(req, "token");
        if (StringUtils.isEmpty(token)) {
            token = req.getParameter("token");
        } else {
            return token;
        }
        if (StringUtils.isEmpty(token)) {
            token = req.getHeader("token");
        } else {
            return token;
        }
        return token;

    }

}
