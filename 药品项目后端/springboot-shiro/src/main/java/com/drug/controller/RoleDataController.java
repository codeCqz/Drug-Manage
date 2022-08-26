package com.drug.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.pojo.Role;
import com.drug.entity.pojo.Supply;
import com.drug.entity.pojo.User;
import com.drug.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roledata")
public class RoleDataController {

    @Autowired
    UserService userService;


    @RequestMapping("/search")
    public ReturnMessage searchUser(@RequestParam("query")String pq){
        System.out.println("================================================>");
        Map map = new HashMap<>();
        List<User> allUser = new ArrayList<>();
        PageQuery pageQuery = new PageQuery();
        int total=0;
        JSONObject jsonObject = JSON.parseObject(pq);
        //获取json对象中的信息
        pageQuery.setPage(jsonObject.getInteger("page"));
        pageQuery.setLimit(jsonObject.getInteger("limit"));
        pageQuery.setSort(jsonObject.getString("sort"));
        String realname = jsonObject.getString("realname");
        String username = jsonObject.getString("username");
        pageQuery.setRoleid(jsonObject.getInteger("roleId"));
        if(realname != null && !StringUtils.isEmpty(realname)){
            pageQuery.setRealname(realname);
        }
        if(username != null && !StringUtils.isEmpty(username)){
            pageQuery.setUsername(username);
        }


        int start = (pageQuery.getPage()-1)*pageQuery.getLimit();
        int end = pageQuery.getPage()*pageQuery.getLimit();
        pageQuery.setStart(start);
        pageQuery.setEnd(end);
        if(pageQuery.getSort().equals("+userid")){
            allUser = userService.getSearch(pageQuery);
        }else if(pageQuery.getSort().equals("-userid")){
            allUser = userService.getSearchDesc(pageQuery);
        }
        total = userService.getSearchCount(pageQuery);
        map.put("items",allUser);
        map.put("total",total);
        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/getroleuser")
    public ReturnMessage getRoleUser(@RequestParam("query")String pq){
        HashMap map = new HashMap();


        JSONObject jsonObject = JSON.parseObject(pq);
        PageQuery pageQuery = new PageQuery();
        //获取json对象中的信息
        pageQuery.setPage(jsonObject.getInteger("page"));
        pageQuery.setLimit(jsonObject.getInteger("limit"));
        pageQuery.setSort(jsonObject.getString("sort"));

        Integer roleid = jsonObject.getInteger("roleId");

        int start = (pageQuery.getPage()-1)*pageQuery.getLimit();
        int end = pageQuery.getPage()*pageQuery.getLimit();


        List<User> userList = null;
        if(pageQuery.getSort().equals("+userid")){
            userList = userService.getUserByRoleId(start, end,roleid);
        }else{
            userList = userService.queryUserByRoleIdDesc(start, end,roleid);
        }

        map.put("alluser",userList);
        map.put("total",userList.size());

        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/getallrole")
    public ReturnMessage getAllRole(){
        HashMap map = new HashMap();

        List<Role> allRole = userService.getAllRole();

        allRole.remove(0);

        map.put("allrole",allRole);

        return ReturnMessage.successWithData(map);
    }


    @RequestMapping("/updateroleuser")
    public ReturnMessage updateUser(@RequestParam("query")String  query) {
        JSONObject jsonObject = JSON.parseObject(query);

        User user = new User();
        user.setUserid(jsonObject.getInteger("userid"));
        user.setUsername(jsonObject.getString("username"));
        user.setPassword(jsonObject.getString("password"));
        user.setTel(jsonObject.getString("tel"));
        user.setRealname(jsonObject.getString("realname"));
        user.setIdentitycard(jsonObject.getString("identitycard"));
        user.setAddress(jsonObject.getString("address"));
        user.setAvatar(jsonObject.getString("avatar"));
        Integer roleId = jsonObject.getInteger("roleId");
        Integer editId = jsonObject.getInteger("editId");

        if (roleId==editId){
            userService.updateUserInfo(user);
        }else{
            userService.updateUserRole(user.getUserid(),editId);
            userService.updateUserInfo(user);
        }

        return ReturnMessage.success();
    }

    @RequestMapping("/insertroleuser")
    public ReturnMessage insertUser(@RequestParam("query")String  query){

        JSONObject jsonObject = JSON.parseObject(query);

        User user = new User();
        user.setUsername(jsonObject.getString("username"));
        user.setPassword(jsonObject.getString("password"));
        user.setTel(jsonObject.getString("tel"));
        user.setRealname(jsonObject.getString("realname"));
        user.setIdentitycard(jsonObject.getString("identitycard"));
        user.setAddress(jsonObject.getString("address"));
        user.setAvatar(jsonObject.getString("avatar"));
        Integer roleId = jsonObject.getInteger("roleId");

        userService.insertUser(user);

        User user1 = userService.queryUser(user.getUsername(), user.getPassword(),user.getRealname());

        userService.insertUserRole(user1.getUserid(),roleId);

        return ReturnMessage.success();
    }

    @RequestMapping("/deleteuser")
    public ReturnMessage delUser(@RequestParam("query")Integer userid){
        userService.delUser(userid);
        return ReturnMessage.success();
    }

}
