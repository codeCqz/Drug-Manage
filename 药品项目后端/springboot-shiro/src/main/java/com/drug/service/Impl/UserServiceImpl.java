package com.drug.service.Impl;

import com.drug.dao.UserDao;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Role;
import com.drug.entity.pojo.User;
import com.drug.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userdao;

    public User queryUserByName(String username){
       return  userdao.queryUserByName(username);
    }

    public void insertUserAvatar(Integer userid ,String avatar){
        userdao.insertUserAvatar(userid,avatar);
    }
    public void updateUser(User user){
        userdao.updateUser(user);
    }
    public List<User> getUserByRoleId(Integer start,Integer end,Integer roleid){
        return userdao.getUserByRoleId(start, end, roleid);
    }
    public List<User> queryUserByRoleIdDesc(Integer start,Integer end,Integer roleid){
        return userdao.queryUserByRoleIdDesc(start, end, roleid);
    }
    public  User queryUserByUserId(Integer userid){
        return userdao.queryUserByUserId(userid);
    }

    public void insertUser(User user){
        userdao.insertUser(user);
    }
    public User queryUser(String username,String password,String realname){
        return userdao.queryUser(username, password,realname);
    }
    public void  insertUserRole(int userid,int roleid){
        userdao.insertUserRole(userid,roleid);
    }
    public List<Role> getAllRole(){
        return userdao.getAllRole();
    }
    public void updateUserInfo(User user){
        userdao.updateUserInfo(user);
    }
    public void updateUserRole(int userid,int roleid){
       userdao.updateUserRole(userid,roleid);
    }
    public void delUser(int userid){
        userdao.delUser(userid);
    }
    public List<User> getSearch(PageQuery pq){
        return userdao.getSearch(pq);
    }
    public List<User> getSearchDesc(PageQuery pq){
        return userdao.getSearchDesc(pq);
    }
    public int getSearchCount(PageQuery pq){
        return userdao.getSearchCount(pq);
    }
}
