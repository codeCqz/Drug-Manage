package com.drug.service.Impl;

import com.drug.dao.GetMedicineDao;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Pickdrug;
import com.drug.service.GetMedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetMedicineServiceImpl implements GetMedicineService {

    @Autowired
    GetMedicineDao getMedicineDao;

    public void insertPickdrug(Pickdrug pickdrug){
        getMedicineDao.insertPickdrug(pickdrug);
    }

    public Integer getAllGetMedicineCount(){
        return getMedicineDao.getAllGetMedicineCount();
    }
    public List<Pickdrug> getAllPickdrug(Integer start, Integer end){
        return getMedicineDao.getAllPickdrug(start,end);
    }
    public  List<Pickdrug> getAllPickdrugDesc(int start,int end){
        return getMedicineDao.getAllPickdrugDesc(start,end);
    }
    public void deletePickdrug(int id){
        getMedicineDao.deletePickdrug(id);
    }
    public List<Pickdrug> getAllPickdrugByID(Integer handler){
        return getMedicineDao.getAllPickdrugByID(handler);
    }
    public List<Pickdrug> getSearch(PageQuery pq){
        return getMedicineDao.getSearch(pq);
    }
    public List<Pickdrug> getSearchDesc(PageQuery pq){
        return getMedicineDao.getSearchDesc(pq);
    }
    public int getSearchCount(PageQuery pq){
        return getMedicineDao.getSearchCount(pq);
    }
}
