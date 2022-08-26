package com.drug.entity.pojo;

import lombok.Data;

@Data
public class Pickdrug {
    private int id;
    private String drugname;
    private Integer handler;
    private Integer quantity;
    private String druguser;
    private String jointime;
    private String exp;
    private Double price;
    private Double TP;

    public Pickdrug() {
    }

    public Pickdrug(int id, String drugname, Integer handler, Integer quantity, String druguser, String jointime, String exp, Double price, Double TP) {
        this.id = id;
        this.drugname = drugname;
        this.handler = handler;
        this.quantity = quantity;
        this.druguser = druguser;
        this.jointime = jointime;
        this.exp = exp;
        this.price = price;
        this.TP = TP;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    public Integer getHandler() {
        return handler;
    }

    public void setHandler(Integer handler) {
        this.handler = handler;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDruguser() {
        return druguser;
    }

    public void setDruguser(String druguser) {
        this.druguser = druguser;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTP() {
        return TP;
    }

    public void setTP(Double TP) {
        this.TP = TP;
    }

    @Override
    public String toString() {
        return "Pickdrug{" +
                "id=" + id +
                ", drugname='" + drugname + '\'' +
                ", handler=" + handler +
                ", quantity=" + quantity +
                ", druguser='" + druguser + '\'' +
                ", jointime='" + jointime + '\'' +
                ", exp='" + exp + '\'' +
                ", price=" + price +
                ", TP=" + TP +
                '}';
    }
}
