package com.drug.utils;

import com.drug.entity.dto.router.Children;
import com.drug.entity.dto.router.Meta;
import com.drug.entity.dto.router.RouterMeunInfo;

import java.util.*;

public class RouterUtils {


    public static List  getAllRouter(List<RouterMeunInfo> userRouter,List<Meta> metaInfo,
                                   List<Children> childrenRouter,List<Meta> childrenMetaInfo){
        List routerList = new ArrayList();
        List childrenList = new ArrayList();
        for (RouterMeunInfo routerMeunInfo : userRouter) {
            int routerid = routerMeunInfo.getId();
            for (Meta meta : metaInfo) {
                int metaid = meta.getId();
                if (metaid == routerid) {
                    routerMeunInfo.setMeta(meta);
                }
            }
            routerList.add(routerMeunInfo);
        }
        for (Children childrenMeunInfo : childrenRouter) {
            int routerid = childrenMeunInfo.getId();
            for (Meta meta : childrenMetaInfo) {
                int metaid = meta.getId();
                if(metaid==routerid){
                    childrenMeunInfo.setMeta(meta);
                }
            }
            childrenList.add(childrenMeunInfo);
        }
        List treeRouter = getSortTreeRouter(routerList  ,childrenList);
        return treeRouter;
    }

    public static  List getSortTreeRouter( List routerList , List childrenList) {


        Collections.sort(routerList);

        Collections.sort(childrenList);


        List treeRouter = getTreeRouter(routerList  ,childrenList);

        return treeRouter;
    }




    public static  List getTreeRouter(List routerList , List childrenList) {


        for (Object routerObj : routerList ) {
            RouterMeunInfo rm = (RouterMeunInfo)routerObj;

            int rmid = rm.getId();

            List<Children> list = new ArrayList<Children>();
            for (Object childObj : childrenList) {
                Children children = (Children) childObj;
                int parentid = children.getParentid();
                if(rmid==parentid) {
                    list.add(children);
                }
            }
            rm.setChildren(list);
        }

        
        return  routerList;

    }

}
