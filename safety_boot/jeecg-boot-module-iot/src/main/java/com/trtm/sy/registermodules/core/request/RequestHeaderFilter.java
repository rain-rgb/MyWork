package com.trtm.sy.registermodules.core.request;//package com.suxin.core.base.request;
//
//import cn.hutool.core.text.AntPathMatcher;
//import com.suxin.core.security.SpringSecurityConstant;
//import com.suxin.core.token.LoginUserPayLoad;
//import com.suxin.core.utils.TokenUtil;
//import com.suxin.system.login.service.LoginService;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
// * 拦截器 拦截并处理请求头中的dataSourcePoolName
// * @author wh
// */
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE + 101)
//final class RequestHeaderFilter implements Filter {
//
//    @Resource
//    private LoginService loginService;
//
//    @Override
//    public void destroy() {
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        String servletPath = httpServletRequest.getServletPath();
//        AntPathMatcher matcher = new AntPathMatcher();
//        for (String path : SpringSecurityConstant.NONE_FILTER) {
//            if (matcher.match(path, servletPath)) {
//                chain.doFilter(httpServletRequest, response);
//                return;
//            }
//        }
//        RequestHeaderWrapper requestHeaderWrapper = new RequestHeaderWrapper(httpServletRequest);
//        String jwtToken = TokenUtil.getTokenByRequest(httpServletRequest);
//        LoginUserPayLoad token = loginService.getLoginUserInfoByToken(jwtToken);
//        requestHeaderWrapper.addHeader("dataSourcePoolName", token.getDataSourcePoolName());
//        chain.doFilter(requestHeaderWrapper, response);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }
//}
