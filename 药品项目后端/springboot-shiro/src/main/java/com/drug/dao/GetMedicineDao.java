package com.drug.dao;

import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Pickdrug;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GetMedicineDao {
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
