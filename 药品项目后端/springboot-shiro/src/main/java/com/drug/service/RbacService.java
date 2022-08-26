package com.drug.service;

import com.drug.entity.pojo.Role;

//Role-Base Access Control
public interface RbacService {
    Role  getUserRole(int userid);
}
