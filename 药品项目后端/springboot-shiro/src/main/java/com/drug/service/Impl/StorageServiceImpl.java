package com.drug.service.Impl;

import com.drug.dao.StorageDao;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Storage;
import com.drug.entity.pojo.Supply;
import com.drug.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {
    @Autowired
    StorageDao storageDao;

    @Override
    public Integer getIsTrue(String drugname,String exp){
        return storageDao.getIsTrue(drugname,exp);
    }

    @Override
    public void insertStorage(Storage storage){
        storageDao.insertStorage(storage);
    }
    @Override
    public void updateStorage(Storage storage){
        storageDao.updateStorage(storage);
    }
    @Override
    public   Integer getQuantityByDrugnameAndExp(String drugname,String exp){
        return storageDao.getQuantityByDrugnameAndExp(drugname,exp);
    }
    @Override
    public List<Storage>  getStorageByDrugname(String drugname){
        return storageDao.getStorageByDrugname(drugname);
    }

    @Override
    public List<String> getAllStorageDrugName(){
        return storageDao.getAllStorageDrugName();
    }
    @Override
    public void  deleteStorage(Storage storage){    storageDao.deleteStorage(storage);
    }

    @Override
    public List<Storage> getAllStorage(Integer start, Integer end){
        return storageDao.getAllStorage(start,end);
    }
    @Override
    public List<Storage> getAllStorageDesc(Integer start,Integer end){
        return storageDao.getAllStorageDesc(start,end);
    }
    @Override
    public Integer getAllStorageCount(){
        return storageDao.getAllStorageCount();
    }

    @Override
    public List<Storage> getSearch(PageQuery pq){
        return storageDao.getSearch(pq);
    }
    @Override
    public  List<Storage> getSearchDesc(PageQuery pq){
        return storageDao.getSearchDesc(pq);
    }
    @Override
    public int getSearchCount(PageQuery pq){
        return  storageDao.getSearchCount(pq);
    }
    @Override
    public List<String> getDrugnameList(){
        return storageDao.getDrugnameList();
    }
    @Override
    public void updateStorageInfo(Storage storage){
        storageDao.updateStorageInfo(storage);
    }

    @Override
    public void deleteStorageByID(Integer storageid){
        storageDao.deleteStorageByID(storageid);
    }

    @Override
    public  List<String> getAllExpByDrugname(String drugname){
        return storageDao.getAllExpByDrugname(drugname);
    }

    @Override
    public List<Double> getPriceByDrugname(String drugname){
        return  storageDao.getPriceByDrugname(drugname);
    }

    @Override
    public void updateStotageByExpAndDrugname(Storage storage) {
        storageDao.updateStotageByExpAndDrugname(storage);
    }

    @Override
    public Integer getStotageDataClassCount() {
        return storageDao.getStotageDataClassCount();
    }

    @Override
    public Integer getWillExpiredDrug() {
        return storageDao.getWillExpiredDrug();
    }



}

