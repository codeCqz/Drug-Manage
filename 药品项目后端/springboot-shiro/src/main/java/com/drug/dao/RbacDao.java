package com.drug.dao;

import com.drug.entity.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RbacDao {
    Role getUserRole(int userid);
}
