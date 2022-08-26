package com.drug.entity.dto.router;

import lombok.Data;

import java.util.List;

@Data
public class Children implements Comparable<Children>{
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
    public int compareTo(Children child) {
        return this.ordernum.compareTo(child.getOrdernum());
    }
}