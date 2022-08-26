package com.drug.service;

import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Storage;
import com.drug.entity.pojo.Supply;

import java.util.List;

public interface StorageService {
    Integer getIsTrue(String drugname,String exp);
    void insertStorage(Storage storage);
    void updateStorage(Storage storage);
    void deleteStorage(Storage storage);
    Integer getQuantityByDrugnameAndExp(String drugname,String exp);
    List<Storage> getStorageByDrugname(String drugname);
    List<String> getAllStorageDrugName();

    List<Storage> getAllStorage(Integer start, Integer end);
    List<Storage> getAllStorageDesc(Integer start,Integer end);

    Integer getAllStorageCount();


    List<Storage> getSearch(PageQuery pq);
    List<Storage> getSearchDesc(PageQuery pq);

    int getSearchCount(PageQuery pq);

    List<String> getDrugnameList();

    void updateStorageInfo(Storage storage);


    void deleteStorageByID(Integer storageid);

    List<String> getAllExpByDrugname(String drugname);

    List<Double> getPriceByDrugname(String drugname);

    void updateStotageByExpAndDrugname(Storage storage);


    Integer getStotageDataClassCount();

    Integer getWillExpiredDrug();
}
