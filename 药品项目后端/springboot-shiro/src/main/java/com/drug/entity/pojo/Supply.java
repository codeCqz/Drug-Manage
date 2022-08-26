package com.drug.entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Supply {
    private Integer supplyid;
    private Integer dealerid;
    private String dealername;
    private Integer drugid;
    private String drugname;
//    数量
    private Integer quantity;
    private Integer handler;
    private Integer status;
    private String edittime;
    private String jointime;
    private String exp;
    private Double univalent;
    private Double TP;

    public Supply() {
    }

    public Supply(Integer supplyid, Integer dealerid, String dealername, Integer drugid, String drugname, Integer quantity, Integer handler, Integer status, String edittime, String jointime, String exp, Double univalent, Double TP) {
        this.supplyid = supplyid;
        this.dealerid = dealerid;
        this.dealername = dealername;
        this.drugid = drugid;
        this.drugname = drugname;
        this.quantity = quantity;
        this.handler = handler;
        this.status = status;
        this.edittime = edittime;
        this.jointime = jointime;
        this.exp = exp;
        this.univalent = univalent;
        this.TP = TP;
    }

    public Integer getSupplyid() {
        return supplyid;
    }

    public void setSupplyid(Integer supplyid) {
        this.supplyid = supplyid;
    }

    public Integer getDealerid() {
        return dealerid;
    }

    public void setDealerid(Integer dealerid) {
        this.dealerid = dealerid;
    }

    public String getDealername() {
        return dealername;
    }

    public void setDealername(String dealername) {
        this.dealername = dealername;
    }

    public Integer getDrugid() {
        return drugid;
    }

    public void setDrugid(Integer drugid) {
        this.drugid = drugid;
    }

    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getHandler() {
        return handler;
    }

    public void setHandler(Integer handler) {
        this.handler = handler;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }

    public String getJointime() {
        return jointime;
    }

    public void setJointime(String jointime) {
        this.jointime = jointime;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public Double getUnivalent() {
        return univalent;
    }

    public void setUnivalent(Double univalent) {
        this.univalent = univalent;
    }

    public Double getTP() {
        return TP;
    }

    public void setTP(Double TP) {
        this.TP = TP;
    }
}
