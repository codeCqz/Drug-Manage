package com.drug.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.pojo.Dealer;
import com.drug.service.DealerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiresAuthentication//检查是否认证注解
@RequestMapping("/dealer")
public class DealerController {

    @Autowired
    DealerService dealerService;

    @RequestMapping("/insertmanydealer")
    public ReturnMessage insertManyDealer(@RequestParam("data")String dealers){
        JSONObject jsonObject = JSON.parseObject(dealers);
        Dealer dealer = new Dealer();
        dealer.setDealerid(jsonObject.getInteger("dealerid"));
        dealer.setDealername(jsonObject.getString("dealername"));
        dealer.setArea(jsonObject.getString("area"));
        dealer.setAddress(jsonObject.getString("address"));
        dealer.setContacts(jsonObject.getString("contacts"));
        dealer.setTelephone(jsonObject.getString("telephone"));
        dealer.setAccount(jsonObject.getString("account"));
        dealer.setBank(jsonObject.getString("bank"));
        dealerService.insertManyDealer(dealer);
        return ReturnMessage.success();
    }



    @RequestMapping("/getsearchdealer")
    public ReturnMessage getSearchDealer(@RequestParam("query")String pq){
        Map map = new HashMap<>();
        List<Dealer> allDealer = new ArrayList<>();
        PageQuery pageQuery = new PageQuery();
        int total=0;
        JSONObject jsonObject = JSON.parseObject(pq);
        //获取json对象中的信息
        pageQuery.setPage(jsonObject.getInteger("page"));
        pageQuery.setLimit(jsonObject.getInteger("limit"));
        pageQuery.setSort(jsonObject.getString("sort"));
        String dealerid = jsonObject.getString("dealerid");
        String dealername = jsonObject.getString("dealername");
        String area = jsonObject.getString("area");
        if(dealerid != null && !StringUtils.isEmpty(dealerid)){
            pageQuery.setDealerid(dealerid);
        }
        if(dealername != null && !StringUtils.isEmpty(dealername)){
            pageQuery.setDealername(dealername);
        }
        if(area != null && !StringUtils.isEmpty(area)){
            pageQuery.setArea(area);
        }


        int start = (pageQuery.getPage()-1)*pageQuery.getLimit();
        int end = pageQuery.getPage()*pageQuery.getLimit();
        pageQuery.setStart(start);
        pageQuery.setEnd(end);
        if(pageQuery.getSort().equals("+dealerid")){
          allDealer = dealerService.searchByDealer(pageQuery);
        }else{
          allDealer = dealerService.searchByDealerDesc(pageQuery);
        }
        total = dealerService.searchByDealerCount(pageQuery);
        map.put("items",allDealer);
        map.put("total",total);
        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/getderivedealer")
    public ReturnMessage getDeriveDealerInfo(@RequestParam("query")String pq){
        Map map = new HashMap<>();
        List<Dealer> allDealer = new ArrayList<>();
        PageQuery pageQuery = new PageQuery();
        JSONObject jsonObject = JSON.parseObject(pq);
        //获取json对象中的信息
        pageQuery.setSort(jsonObject.getString("sort"));
        if(pageQuery.getSort().equals("+dealerid")){
            allDealer = dealerService.getDeriveDealer();
        }else{
            allDealer = dealerService.getDeriveDealerDesc();
        }
        int total = dealerService.getAllDealerCount();
        map.put("items",allDealer);
        map.put("total",total);
        return ReturnMessage.successWithData(map);
    }


    @RequestMapping("/getalldealer")
    public ReturnMessage getAllDealerInfo(@RequestParam("query")String pq){
        Map map = new HashMap<>();
        List<Dealer> allDealer = new ArrayList<>();
        PageQuery pageQuery = new PageQuery();
        JSONObject jsonObject = JSON.parseObject(pq);
        //获取json对象中的信息
        pageQuery.setPage(jsonObject.getInteger("page"));
        pageQuery.setLimit(jsonObject.getInteger("limit"));
        pageQuery.setSort(jsonObject.getString("sort"));
        int start = (pageQuery.getPage()-1)*pageQuery.getLimit();
        int end = pageQuery.getPage()*pageQuery.getLimit();
        if(pageQuery.getSort().equals("+dealerid")){
            allDealer = dealerService.getAllDealer(start,end);
        }else{
            allDealer = dealerService.getAllDealerDesc(start,end);
        }
        int total = dealerService.getAllDealerCount();
        map.put("items",allDealer);
        map.put("total",total);
        return ReturnMessage.successWithData(map);
    }


    @RequestMapping("/editdealer")
    public ReturnMessage editDealerInfo(@RequestParam("data")String dealer){
        JSONObject jsonObject = JSON.parseObject(dealer);
        Dealer dealerInfo = new Dealer();
        dealerInfo.setDealerid(jsonObject.getInteger("dealerid"));
        dealerInfo.setDealername(jsonObject.getString("dealername"));
        dealerInfo.setArea(jsonObject.getString("area"));
        dealerInfo.setAddress(jsonObject.getString("address"));
        dealerInfo.setTelephone(jsonObject.getString("telephone"));
        dealerInfo.setContacts(jsonObject.getString("contacts"));
        dealerInfo.setBank(jsonObject.getString("bank"));
        dealerInfo.setAccount(jsonObject.getString("account"));
        dealerService.editDealer(dealerInfo);
        return ReturnMessage.success();
    }

    @RequestMapping("/insertdealer")
    public ReturnMessage insertDealer(@RequestParam("data")String dealer){
        JSONObject jsonObject = JSON.parseObject(dealer);
        Dealer dealerInfo = new Dealer();
        dealerInfo.setDealerid(jsonObject.getInteger("dealerid"));
        dealerInfo.setDealername(jsonObject.getString("dealername"));
        dealerInfo.setArea(jsonObject.getString("area"));
        dealerInfo.setAddress(jsonObject.getString("address"));
        dealerInfo.setTelephone(jsonObject.getString("telephone"));
        dealerInfo.setContacts(jsonObject.getString("contacts"));
        dealerInfo.setBank(jsonObject.getString("bank"));
        dealerInfo.setAccount(jsonObject.getString("account"));
        dealerService.insertDealer(dealerInfo);
        return ReturnMessage.success();
    }

    @RequestMapping("/deletedealer")
    public ReturnMessage deleteDealer(@RequestParam("data")Integer dealerid){
        dealerService.deleteDealer(dealerid);
        return ReturnMessage.success();
    }


}
