package com.drug.dao;

import com.drug.entity.dto.DrugInfo;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Drug;
import com.drug.entity.pojo.DrugDetails;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DrugDao {
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
    List<Drug> getEnterDrug(PageQuery pq);
    List<Drug>  getEnterDrugDesc(PageQuery pq);
    Integer getEnterDrugCount();
    List<Drug> getSearch(PageQuery pq);
    List<Drug> getSearchDesc(PageQuery pq);
    int getSearchCount(PageQuery pq);
    void deleteDrugByDrugid(int drugid);
}
