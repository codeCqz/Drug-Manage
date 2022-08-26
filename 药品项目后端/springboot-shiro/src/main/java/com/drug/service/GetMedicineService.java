package com.drug.service;

import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Pickdrug;
import com.drug.entity.pojo.Supply;

import java.util.List;

public interface GetMedicineService {
    void insertPickdrug(Pickdrug pickdrug);
    Integer getAllGetMedicineCount();
    List<Pickdrug> getAllPickdrug(Integer start, Integer end);
    List<Pickdrug> getAllPickdrugDesc(int start,int end);
    void deletePickdrug(int id);
    List<Pickdrug> getAllPickdrugByID(Integer handler);
    List<Pickdrug> getSearch(PageQuery pq);
    List<Pickdrug> getSearchDesc(PageQuery pq);
    int getSearchCount(PageQuery pq);
}
