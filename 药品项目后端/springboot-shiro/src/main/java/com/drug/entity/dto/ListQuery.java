package com.drug.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListQuery {
    private Integer page;
    private Integer limit;
    private String sort;
    private List params;
    private Boolean isAll;
}
