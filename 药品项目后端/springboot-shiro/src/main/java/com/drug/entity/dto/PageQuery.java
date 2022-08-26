package com.drug.entity.dto;

import lombok.Data;

@Data
public class PageQuery {
    private int page;
    private int limit;
    private String dealerid;
    private String drugname;
    private String druguser;
    private String userid;
    private String username;
    private String realname;
    private String standardcode;
    private String dealername;
    private String area;
    private String sort;
    private Integer start;
    private Integer end;
    private Integer roleid;
}
