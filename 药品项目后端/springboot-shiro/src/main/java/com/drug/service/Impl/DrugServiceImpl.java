package com.drug.service.Impl;

import com.drug.dao.DrugDao;
import com.drug.entity.dto.DrugInfo;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Drug;
import com.drug.entity.pojo.DrugDetails;
import com.drug.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrugServiceImpl implements DrugService {

    @Autowired
    DrugDao drugDao;

    public void insertDrug(Drug drug){
        drugDao.insertDrug(drug);
    }

    public List<Drug> getDrugByDrugname(String drugname){
        return drugDao.getDrugByDrugname(drugname);
    }
    public void insertDrugDetails(DrugDetails drugDetails){
        drugDao.insertDrugDetails(drugDetails);
    }

    public Drug getDrugByDrugID(int drugid){
        return drugDao.getDrugByDrugID(drugid);
    }

    public DrugDetails getDrugDetailsByDrugname(String drugname){
        return drugDao.getDrugDetailsByDrugname(drugname);
    }

    public void deleteDrug(int drugid){
        drugDao.deleteDrug(drugid);
    }

    public  Integer getDrugidByDrugname(String drugname){
        return drugDao.getDrugidByDrugname(drugname);
    }

    public List<String> getAllDrugName(){
        return drugDao.getAllDrugName();
    }

    public Integer getByDrugName(String drugname){
        return drugDao.getByDrugName(drugname);
    }

    public DrugInfo getDrugInfo(String drugname){
        return drugDao.getDrugInfo(drugname);
    }

    public  List<Drug>  getEnterDrug(PageQuery pq){
        return drugDao.getEnterDrug(pq);
    }
    public  List<Drug>  getEnterDrugDesc(PageQuery pq){
        return drugDao.getEnterDrugDesc(pq);
    }
    public Integer getEnterDrugCount(){
        return drugDao.getEnterDrugCount();
    }
    public List<Drug> getSearch(PageQuery pq){
        return drugDao.getSearch(pq);
    }
    public List<Drug> getSearchDesc(PageQuery pq){
        return drugDao.getSearchDesc(pq);
    }
    public int getSearchCount(PageQuery pq){
        return drugDao.getSearchCount(pq);
    }

    public void deleteDrugByDrugid(int drugid){
        drugDao.deleteDrugByDrugid(drugid);
    }
}
