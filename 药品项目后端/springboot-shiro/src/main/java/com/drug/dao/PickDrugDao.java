package com.drug.dao;

import com.drug.entity.pojo.Pickdrug;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PickDrugDao {
    void insertPickDrug(Pickdrug pd);

    List<Double> getPickTP(String jointime);

    List<Pickdrug> getAllPickByJoinTime(String jointime);

    void updatePickDrug(Pickdrug pickdrug);
}
