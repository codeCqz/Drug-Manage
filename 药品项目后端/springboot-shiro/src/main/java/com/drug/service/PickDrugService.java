package com.drug.service;

import com.drug.entity.pojo.Pickdrug;
import com.drug.entity.pojo.Storage;

import java.util.List;

public interface PickDrugService {
    void insertPickDrug(Pickdrug pd);

    List<Double> getPickTP(String jointime);

    List<Pickdrug> getAllPickByJoinTime(String jointime);

    void updatePickDrug(Pickdrug pickdrug);
}
