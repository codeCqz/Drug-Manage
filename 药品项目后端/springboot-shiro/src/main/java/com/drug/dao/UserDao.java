package com.drug.dao;


import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Role;
import com.drug.entity.pojo.Supply;
import com.drug.entity.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    User queryUserByName(String username);
    void insertUserAvatar(Integer userid ,String avatar);
    void updateUser(User user);
    User queryUserByUserId(Integer userid);
    List<User> getUserByRoleId(Integer start,Integer end,Integer roleid);
    List<User> queryUserByRoleIdDesc(Integer start,Integer end,Integer roleid);
    void insertUser(User user);
    User queryUser(String username,String password,String realname);
    void insertUserRole(int userid,int roleid);
    List<Role> getAllRole();
    void updateUserInfo(User user);
    void updateUserRole(int userid,int roleid);
    void delUser(int userid);
    List<User> getSearch(PageQuery pq);
    List<User> getSearchDesc(PageQuery pq);
    int getSearchCount(PageQuery pq);
}
