package com.drug.dao;

import com.drug.entity.dto.router.Children;
import com.drug.entity.dto.router.Meta;
import com.drug.entity.dto.router.RouterMeunInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RouterDao {
    List<RouterMeunInfo> getUserRouter(int roleid);
    List<Meta> getUserMeta(int roleid);

    List<Children> getChildrenRouter(int roleid);
    List<Meta> getChildrenMeta(int roleid);
}
