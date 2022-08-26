package com.drug.service.Impl;

import com.drug.dao.RouterDao;
import com.drug.entity.dto.router.Children;
import com.drug.entity.dto.router.Meta;
import com.drug.entity.dto.router.RouterMeunInfo;
import com.drug.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouterServiceImpl implements RouterService {

    @Autowired
    RouterDao routerDao;

    public List<RouterMeunInfo> getUserRouter(int roleid){
        return routerDao.getUserRouter(roleid);
    }
    public List<Meta> getUserMeta(int roleid){
        return routerDao.getUserMeta(roleid);
    }


    public List<Children> getChildrenRouter(int roleid){
        return routerDao.getChildrenRouter(roleid);
    }
    public List<Meta> getChildrenMeta(int roleid){
        return routerDao.getChildrenMeta(roleid);
    }
}
