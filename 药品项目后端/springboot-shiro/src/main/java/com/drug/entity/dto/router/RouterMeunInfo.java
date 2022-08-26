package com.drug.entity.dto.router;
//这是设计好的类，可以直接使用
import lombok.Data;

import java.util.List;


@Data
public class RouterMeunInfo implements Comparable<RouterMeunInfo>{
    private int id;
    private boolean hidden;
    private String name;
    private String path;
    private String url;
    private boolean alwaysShow;
    private String component;
    private String redirect;
    private int parentid;
    private Integer ordernum;
    //涉及meta内部集合
    private Meta meta;
    //设计chilren
    private List<Children> children;


    @Override
    public int compareTo(RouterMeunInfo rm) {
        return this.ordernum.compareTo(rm.getOrdernum());
    }
}


