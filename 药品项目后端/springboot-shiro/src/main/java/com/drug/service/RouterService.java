package com.drug.service;

import com.drug.entity.dto.router.Children;
import com.drug.entity.dto.router.Meta;
import com.drug.entity.dto.router.RouterMeunInfo;

import java.util.List;

public interface RouterService {

   List<RouterMeunInfo> getUserRouter(int roleid);
   List<Meta> getUserMeta(int roleid);


   List<Children> getChildrenRouter(int roleid);
   List<Meta> getChildrenMeta(int roleid);
}
