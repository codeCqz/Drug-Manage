package com.drug.filter;

import com.drug.entity.dto.ReturnMessage;
import com.drug.shiro.JwtToken;
import com.drug.shiro.uitls.JwtUtil;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * Description:继承官方的BasicHttpAuthenticationFilter，并且重写鉴权的方法
 * 代码的执行流程preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 */
//@Component
public class JWTFilter extends BasicHttpAuthenticationFilter {
    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest hsr = (HttpServletRequest) request;
        String authorization = hsr.getHeader("Authorization");
        return authorization!=null;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                //如果有token
                boolean b = executeLogin(request, response);
                if(b==false){
                    tokenendtime(request, response);
                }
            } catch (Exception e) {
                //如果请求头没有带Authorization属性的token 就到这里咯
                System.out.println("错误请求，Authorization内token错误=======>");
                response500(request, response);
            }
        }
        return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //从请求头中获取token
        String authorization = httpServletRequest.getHeader("Authorization");
        String[] TOKEN = authorization.split(" ");
        authorization=TOKEN[1];
        Long endtime = JwtUtil.getExpiresAt(authorization);
        //放入对象中, 数据库查询  等同于之前的UsernamePasswordToken
        JwtToken token = new JwtToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获

        //这个等同于直接的 subject.login(token)
        Date enddate = new Date(endtime);
        Date nowdate = new Date();
        long nowtime = nowdate.getTime();
        if(nowtime>endtime){
            System.out.println("token过期");
            return false;
        }else{
            getSubject(request, response).login(token);

        }
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }
    /**
     * 将非法请求返回错误
     */
    private ReturnMessage response500(ServletRequest req, ServletResponse resp) {
        return new ReturnMessage().failWithMsg(500,"非法请求!!！");
    }

    private ReturnMessage tokenendtime(ServletRequest req, ServletResponse resp){
        return new ReturnMessage().failWithMsg(401,"token过期,请重新登录!");
    }
}
