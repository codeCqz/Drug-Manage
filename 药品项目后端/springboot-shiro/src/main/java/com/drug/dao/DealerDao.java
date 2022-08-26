package com.drug.dao;

import com.drug.entity.dto.DealerInfo;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Dealer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DealerDao {
    List<Dealer> getAllDealer(int start,int end);
    List<Dealer> getAllDealerDesc(int start,int end);


    List<Dealer> getDeriveDealer();
    List<Dealer> getDeriveDealerDesc();


    void editDealer(Dealer dealer);
    void insertDealer(Dealer dealer);
    void deleteDealer(int dealerid);
    Integer getAllDealerCount();

    List<Dealer> searchByDealer(PageQuery pq);
    List<Dealer> searchByDealerDesc(PageQuery pq);
    int searchByDealerCount(PageQuery pq);
    void  insertManyDealer(Dealer dealer);
    List<DealerInfo> getDealerInfo();
    Integer getDealerByDealername(String dealername);
}
