package com.drug.service;

import com.drug.entity.dto.PageQuery;
import com.drug.entity.dto.SupplyCount;
import com.drug.entity.pojo.Supply;

import java.util.List;

public interface SupplyService {

    List<Supply> getAllSupplyByUserIdDesc(Integer start,Integer end,Integer userid);
    List<Supply> getAllSupplyByUserId(Integer start,Integer end,Integer userid);
    List<Supply> getAllSupply(Integer start,Integer end);
    List<Supply> getAllSupplyDesc(Integer start,Integer end);
    Integer getAllSupplyCount();
    void InsertSupply(Supply supply);
    void updateStatus(Integer supplyid);
    void setStatus(Integer supplyid);
    void updateSupply(Supply supply);
    void deleteSupply(Integer supplyid);
    Integer getBuyerDataClassCount();
    Integer getBuyerDiffDataClassCount(String jointime);
    Integer getBuyerDiffDataDayAdd(String jointime);
    Integer getBuyerDataToDayNewAdd(String jointime);
    Integer getAllExpiredSupply();
    Integer getAllDiffExpiredSupply(String jointime);
    String[] getRadder();
    Integer getEveryExp(String dealername);
    List<Supply> getEveryDrug(String dealername);
    Integer getEverySupply(String dealername);

    String[] getPieDataString();
    List<Supply> getPieDataQuantity(String drugname);
    Supply getSupplyById(Integer supplyid);

    int getAllSupplyCountByid(Integer userid);

    List<Supply> getSearch(PageQuery pq);
    List<Supply> getSearchDesc(PageQuery pq);

    int getSearchCount(PageQuery pq);
    List<SupplyCount> getCount();
    Supply getSupply();

    List<Double> getSupplyTP(String jointime);

    double getunivalentBydrugnameandexp(String drugname,String exp);
}
