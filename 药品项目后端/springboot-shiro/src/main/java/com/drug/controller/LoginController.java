package com.drug.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.dto.UserVerify;

import com.drug.entity.dto.router.Children;
import com.drug.entity.dto.router.Meta;
import com.drug.entity.dto.router.RouterMeunInfo;
import com.drug.entity.pojo.Role;
import com.drug.entity.pojo.User;

import com.drug.service.RbacService;
import com.drug.service.RouterService;
import com.drug.service.UserService;
import com.drug.shiro.JwtToken;
import com.drug.shiro.uitls.JwtUtil;
import com.drug.utils.RouterUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class LoginController {


    @Autowired
    UserService userService;

    @Autowired
    RbacService rbacService;

    @Autowired
    RouterService routerService;

    @PostMapping ("/login")
    public ReturnMessage login(@RequestParam("username") String username,@RequestParam("password")String password,@RequestParam("captchatext")String captcha,
                               @RequestParam("captcha")String usercaptcha) {

        UserVerify user = new UserVerify();
        ReturnMessage rm = new ReturnMessage();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return rm.failWithMsg(400,"请输入用户名和密码！");
        }
        //用户认证信息
        Subject subject = SecurityUtils.getSubject();
        String jwt = JwtUtil.sign(username, password);
        if (!subject.isAuthenticated()) {
            try {
                //进行验证，这里可以捕获异常，然后返回对应信息
                subject.login(new JwtToken(jwt));
            } catch (UnknownAccountException e) {
                return rm.failWithMsg(400, "用户名不存在！");
            } catch (AuthenticationException e) {
                return rm.failWithMsg(400, "用户名或密码错误！");
            } catch (AuthorizationException e) {
                return rm.failWithMsg(400, "没有权限！");
            }
            if (usercaptcha != null && captcha != null) {
                if (!usercaptcha.equals(captcha)) {
                    return rm.failWithMsg(400, "请输入正确的验证码！");
                }
            } else {
                return rm.failWithMsg(400, "请输入验证码！");
            }
        }
        Map returndata = new HashMap();
        returndata.put("token",jwt);
        returndata.put("uuid", UUID.randomUUID());
        return rm.successWithData(returndata);
    }

    /*
        获取用户信息
     */
    @GetMapping("/getInfo")
    @RequiresAuthentication//检查是否认证注解
    public ReturnMessage getInfo(){
        Map map = new HashMap();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Role userRole = rbacService.getUserRole(user.getUserid());

        map.put("user",user);
        map.put("roles",userRole.getRole());

        Set<String> perms = new HashSet<String>();
        if(userRole.getRole().equals("admin")){
            perms.add("*:*:*");
        }

        map.put("permissions", perms);
        map.put("avatar",user.getAvatar());
        map.put("name",userRole.getRole());
        map.put("role",userRole);
        return ReturnMessage.successWithData(map);
    }

    @GetMapping("getRouters")
    @RequiresAuthentication
    public ReturnMessage getRouters(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        Role userRole = rbacService.getUserRole(user.getUserid());
        int roleid = userRole.getRoleid();
        List<RouterMeunInfo> routerInfo = getRouterInfo(roleid);
        List<Meta> userMetaInfo = getMetaInfo(roleid);
        List<Children> childrenInfo = getChildrenRouterInfo(roleid);
        List<Meta> childrenMetaInfo = getChildrenMetaInfo(roleid);
        List  userRouter = RouterUtils.getAllRouter(routerInfo,userMetaInfo,childrenInfo,childrenMetaInfo );
        Map map = new HashMap();
        map.put("router",userRouter);
        return ReturnMessage.successWithData(map);
    }


    @GetMapping("/update")
    public ReturnMessage update(@RequestParam("data")String data){
        JSONObject jsonObject = JSON.parseObject(data);
        Integer userid = jsonObject.getInteger("userid");
        String realname = jsonObject.getString("realname");
        String tel = jsonObject.getString("tel");
        String address = jsonObject.getString("address");
        User user = new User();

        user.setUserid(userid);
        user.setRealname(realname);
        user.setTel(tel);
        user.setAddress(address);

        userService.updateUser(user);

        HashMap map = new HashMap();

        User user1 = userService.queryUserByUserId(userid);
        map.put("userInfo",user1);
        return ReturnMessage.successWithData(map);
    }

    @PostMapping("logout")
    @RequiresAuthentication
    public ReturnMessage logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ReturnMessage.success();
    }

    public List<RouterMeunInfo> getRouterInfo(int roleid){

        List<RouterMeunInfo> userRouter = routerService.getUserRouter(roleid);
        return userRouter;
    }

    public List<Meta> getMetaInfo(int roleid){
        List<Meta> userMeta =  routerService.getUserMeta(roleid);
        return userMeta;
    }

    public List<Children> getChildrenRouterInfo(int roleid){

        List<Children> userRouter = routerService.getChildrenRouter(roleid);
        return userRouter;
    }

    public List<Meta> getChildrenMetaInfo(int roleid){
        List<Meta> userMeta =  routerService.getChildrenMeta(roleid);
        return userMeta;
    }








}
