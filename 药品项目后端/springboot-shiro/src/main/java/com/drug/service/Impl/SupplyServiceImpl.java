package com.drug.service.Impl;

import com.drug.dao.SupplyDao;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.dto.SupplyCount;
import com.drug.entity.pojo.Supply;
import com.drug.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyServiceImpl implements SupplyService {

    @Autowired
    SupplyDao supplyDao;

    public List<Supply> getAllSupplyByUserId(Integer start,Integer end,Integer userid){
        return supplyDao.getAllSupplyByUserId(start,end,userid);
    }

    public List<Supply> getAllSupplyByUserIdDesc(Integer start,Integer end,Integer userid){
        return supplyDao.getAllSupplyByUserIdDesc(start,end,userid);
    }

    public List<Supply> getAllSupply(Integer start,Integer end){
        return supplyDao.getAllSupply(start,end);
    }

    public List<Supply> getAllSupplyDesc(Integer start,Integer end){
        return supplyDao.getAllSupplyDesc(start,end);
    }

    public Integer getAllSupplyCount(){
        return supplyDao.getAllSupplyCount();
    }

    public void InsertSupply(Supply supply){
        supplyDao.InsertSupply(supply);
    }

    public void updateStatus(Integer supplyid){
        supplyDao.updateStatus(supplyid);
    }

    public void setStatus(Integer supplyid){
        supplyDao.setStatus(supplyid);
    }

    public  void updateSupply(Supply supply){
        supplyDao.updateSupply(supply);
    }
    public void deleteSupply(Integer supplyid){
        supplyDao.deleteSupply(supplyid);
    }

    public Integer getBuyerDataClassCount(){
        return supplyDao.getBuyerDataClassCount();
    }
    public Integer getBuyerDataToDayNewAdd(String jointime){
        return supplyDao.getBuyerDataToDayNewAdd(jointime);
    }

    public Integer getAllExpiredSupply(){
        return supplyDao.getAllExpiredSupply();
    }

    public Integer getBuyerDiffDataClassCount(String jointime){
        return supplyDao.getBuyerDiffDataClassCount(jointime);
    }
    public Integer getBuyerDiffDataDayAdd(String jointime){
        return supplyDao.getBuyerDiffDataDayAdd(jointime);
    }
    public Integer getAllDiffExpiredSupply(String jointime){
        return supplyDao.getAllDiffExpiredSupply(jointime);
    }
    public String[]  getRadder(){
        return supplyDao.getRadder();
    }

    public Integer getEveryExp(String dealername){
        return supplyDao.getEveryExp(dealername);
    }
    public List<Supply> getEveryDrug(String dealername){
        return supplyDao.getEveryDrug(dealername);
    }
    public Integer getEverySupply(String dealername){
        return supplyDao.getEverySupply(dealername);
    }
    public String[] getPieDataString(){
        return supplyDao.getPieDataString();
    }
    public List<Supply> getPieDataQuantity(String drugname){
        return supplyDao.getPieDataQuantity(drugname);
    }
    public Supply getSupplyById(Integer supplyid){
        return supplyDao.getSupplyById(supplyid);
    }
    public int getAllSupplyCountByid(Integer userid){
        return supplyDao.getAllSupplyCountByid(userid);
    }
    public List<Supply> getSearch(PageQuery pq){
        return supplyDao.getSearch(pq);
    }
    public  List<Supply> getSearchDesc(PageQuery pq){
        return supplyDao.getSearchDesc(pq);
    }
    public int getSearchCount(PageQuery pq){
        return supplyDao.getSearchCount(pq);
    }
    public List<SupplyCount> getCount(){
        return supplyDao.getCount();
    }
    public Supply getSupply(){
        return supplyDao.getSupply();
    }

    @Override
    public List<Double> getSupplyTP(String jointime) {
        return supplyDao.getSupplyTP(jointime);
    }

    @Override
    public double getunivalentBydrugnameandexp(String drugname, String exp) {
        return supplyDao.getunivalentBydrugnameandexp(drugname,exp);
    }


}
