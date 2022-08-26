package com.drug.service.Impl;

import com.drug.dao.RbacDao;
import com.drug.entity.pojo.Role;
import com.drug.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RbacServiceImpl implements RbacService {

    @Autowired
    RbacDao rbacDao;

    @Override
    public Role getUserRole(int userid) {
        return rbacDao.getUserRole(userid);
    }
}
