package com.drug.shiro.realm;


import com.drug.entity.pojo.User;
import com.drug.service.UserService;
import com.drug.shiro.JwtToken;
import com.drug.shiro.uitls.JwtUtil;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;


    /**
     * Description:自定义域  自己写登录验证 和授权
     */
    public class UserRealm  extends AuthorizingRealm {

        @Autowired
        UserService userService;


//    @Autowired
//    RbacPermService rbacPermService;

        /**
         * 身份认证方法
         * 需要在用户登录系统时触发
         * 该方法将是我们主要的方法, 流程是登录后, 携带token 与权限信息 发送到 vue中 存储浏览器
         * 在页面渲染时,直接传入菜单和 页面按钮显示权限
         * @return
         * @throws AuthenticationException
         */
        @Override
        protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {

            String myJWT = (String) auth.getCredentials();
            String username = JwtUtil.getUsername(myJWT);
            if (username==null){
                throw new AuthenticationException("token无效=======>");
            }
            //通过用户名,查出当前用户密码
            User user = userService.queryUserByName(username);

            if (user==null){
                throw new AuthenticationException("User 不存在");
            }
            //通过存入的myJWT与传入的账号密码进行验证, 账号密码是否正确(类比之前手动比较密码~~~~)
            if (JwtUtil.verify(myJWT,username,user.getPassword())) {
                return new SimpleAuthenticationInfo(user,myJWT,this.getName());
            }else {
                throw new AuthenticationException("用户名或者密码错误");
            }
        }


        @Override
        public void setName(String name){
            super.setName("LoginRealm");
        }
        // 这个需要注意下, 多域模式中, 每个域里面 请自己配置自己的token来源
        // 这里的token是来自 自定义的JWTToken
        // 必须重写此方法，不然 传的jwt, shiro却使用的是UsernamePasswordToken进行解析,
        // 会报一个错, 你token格式不对(JWT的值是3部分, UsernamePasswordToken只有1节....)
        @Override
        public boolean supports(AuthenticationToken token) {
//        instanceof是Java中的二元运算符，左边是对象，右边是类；当对象是右边类或子类所创建对象时，返回true；否则，返回false。
            return token instanceof JwtToken;
        }
        /**
         * 授权信息
         * 在前后端不分离的情况下, 在html页面使用<shiro>|后端使用shiro的@checkRole,@checkPermission相关注解才能触发
         * 前后端分离的模式, 就直接用注解验证权限即可, 根据需求来
         */
        @Override
        protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

            //这里得去获取到这个token  不用token过滤器
            String userName = JwtUtil.getUsername(principals.toString());
            //去数据库查出用户信息, 含角色(也可含权限, 看你实体怎么定义咯= =)
            User user = userService.queryUserByName(userName);
            //授权信息集合
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            //1.如果需求是角色可以配置多个,则如下获取所有角色 并放入授权信息集合(模拟多角色查询,虽然目前是单的)
            //设置角色
            Set<String> roles = new HashSet<>();
//        String role = String.valueOf(user.getRoleid());
//        if (!"".equals(roles)&&roles!=null){
//            String[] roles1 = roles.split(",");
//            for (String role: roles1) {

            String role="user";
            simpleAuthorizationInfo.addRole(role);



            Set set = new HashSet();
            set.add("look");
            simpleAuthorizationInfo.addStringPermissions(set);
//            }
//        }
//        //2.目前是单个角色则直接这样用吧.....
//       simpleAuthorizationInfo.addRole(user.getRoleId()+"");
//        //3.通过角色, 去查询中间表 找到所有的权限  获取tyep字段,a接口权限link细节信息(同第1步)
//        List<RbacPerm> forRole = rbacPermService.findForRole(roles);
//        //4.转成HashSet去重存放
//        HashSet<String> permissions = new HashSet<>();
//        for (RbacPerm rbacPerm : forRole) {
//            if(rbacPerm.getCode()!=null){
//                permissions.add(rbacPerm.getCode());
//            }
//        }
//        //5.放入simpleAuthorizationInfo对象中
//        simpleAuthorizationInfo.addStringPermissions(permissions);
//        return simpleAuthorizationInfo;
            return simpleAuthorizationInfo;
        }



}
