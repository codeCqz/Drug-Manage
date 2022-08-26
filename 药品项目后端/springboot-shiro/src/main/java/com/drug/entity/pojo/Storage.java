package com.drug.entity.pojo;

import lombok.Data;

@Data
public class Storage {
    private Integer storageid;
    private String drugname;
    private Integer quantity;
    private String exp;
    private Double price;
    private Integer status;
}
