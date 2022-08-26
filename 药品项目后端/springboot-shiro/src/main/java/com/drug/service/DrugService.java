package com.drug.service;

import com.drug.entity.dto.DrugInfo;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Drug;
import com.drug.entity.pojo.DrugDetails;
import com.drug.entity.pojo.User;

import java.util.List;

public interface DrugService {
    void insertDrug(Drug drug);
    List<Drug> getDrugByDrugname(String drugname);
    Drug getDrugByDrugID(int drugid);
    void insertDrugDetails(DrugDetails drugDetails);
    DrugDetails getDrugDetailsByDrugname(String drugname);
    void deleteDrug(int drugid);
    Integer getDrugidByDrugname(String drugname);
    List<String> getAllDrugName();
    Integer getByDrugName(String drugname);
    DrugInfo getDrugInfo(String drugname);
    List<Drug>  getEnterDrug(PageQuery pq);
    Integer getEnterDrugCount();
    List<Drug>  getEnterDrugDesc(PageQuery pq);
    List<Drug> getSearch(PageQuery pq);
    List<Drug> getSearchDesc(PageQuery pq);
    int getSearchCount(PageQuery pq);
    void deleteDrugByDrugid(int drugid);
}
