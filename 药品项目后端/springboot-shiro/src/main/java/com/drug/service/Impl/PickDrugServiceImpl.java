package com.drug.service.Impl;

import com.drug.dao.PickDrugDao;
import com.drug.entity.pojo.Pickdrug;
import com.drug.service.PickDrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PickDrugServiceImpl implements PickDrugService {

    @Autowired
    PickDrugDao pickDrugDao;

    @Override
    public void insertPickDrug(Pickdrug pd) {
        pickDrugDao.insertPickDrug(pd);
    }

    @Override
    public List<Double> getPickTP(String jointime) {
        return pickDrugDao.getPickTP(jointime);
    }

    @Override
    public List<Pickdrug> getAllPickByJoinTime(String jointime) {
        return pickDrugDao.getAllPickByJoinTime(jointime);
    }

    @Override
    public void updatePickDrug(Pickdrug pickdrug) {
        pickDrugDao.updatePickDrug(pickdrug);
    }
}
