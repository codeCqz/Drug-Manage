package com.drug.service.Impl;

import com.drug.dao.DealerDao;
import com.drug.entity.dto.DealerInfo;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.pojo.Dealer;
import com.drug.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealerServiceImpl implements DealerService {


    @Autowired
    DealerDao dealerDao;

    public  List<Dealer> getAllDealer(int start,int end){
        return dealerDao.getAllDealer(start,end);
   }

    public List<Dealer> getAllDealerDesc(int start,int end){
       return dealerDao.getAllDealerDesc(start,end);
   }


    public  List<Dealer> getDeriveDealer(){
       return dealerDao.getDeriveDealer();
   }
    public  List<Dealer> getDeriveDealerDesc(){
       return dealerDao.getDeriveDealerDesc();
   }
    public void editDealer(Dealer dealer){
       dealerDao.editDealer(dealer);
   }


    public void insertDealer(Dealer dealer){
       dealerDao.insertDealer(dealer);
    }


    public  void deleteDealer(int dealerid){
       dealerDao.deleteDealer(dealerid);
    }

    public  Integer getAllDealerCount(){
       return dealerDao.getAllDealerCount();
   }

    public List<Dealer> searchByDealer(PageQuery pq){
       return dealerDao.searchByDealer(pq);
    }
    public List<Dealer> searchByDealerDesc(PageQuery pq){
       return dealerDao.searchByDealerDesc(pq);
    }
    public int searchByDealerCount(PageQuery pq){
       return dealerDao.searchByDealerCount(pq);
    }
    public void insertManyDealer(Dealer dealer){
       dealerDao.insertManyDealer(dealer);
    }

    public List<DealerInfo> getDealerInfo(){
        return dealerDao.getDealerInfo();
    }

    public Integer getDealerByDealername(String dealername){
        return dealerDao.getDealerByDealername(dealername);
    }
}
