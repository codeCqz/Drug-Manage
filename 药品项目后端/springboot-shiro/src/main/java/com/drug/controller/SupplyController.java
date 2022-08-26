package com.drug.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drug.entity.dto.DealerInfo;
import com.drug.entity.dto.ListQuery;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.pojo.Dealer;
import com.drug.entity.pojo.Storage;
import com.drug.entity.pojo.Supply;
import com.drug.entity.pojo.User;
import com.drug.service.DealerService;
import com.drug.service.DrugService;
import com.drug.service.StorageService;
import com.drug.service.SupplyService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.security.krb5.internal.ETypeInfo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/supply")
public class SupplyController {

    @Autowired
    SupplyService supplyService;

    @Autowired
    DealerService dealerService;

    @Autowired
    DrugService drugService;

    @Autowired
    StorageService storageService;


    @RequestMapping("/getsearchsupply")
    public ReturnMessage getSearchDealer(@RequestParam("query")String pq){
        Map map = new HashMap<>();
        List<Supply> allSupply = new ArrayList<>();
        PageQuery pageQuery = new PageQuery();
        int total=0;
        JSONObject jsonObject = JSON.parseObject(pq);
        //获取json对象中的信息
        pageQuery.setPage(jsonObject.getInteger("page"));
        pageQuery.setLimit(jsonObject.getInteger("limit"));
        pageQuery.setSort(jsonObject.getString("sort"));
        String drugname = jsonObject.getString("drugname");
        String dealername = jsonObject.getString("dealername");
        if(drugname != null && !StringUtils.isEmpty(drugname)){
            pageQuery.setDrugname(drugname);
        }
        if(dealername != null && !StringUtils.isEmpty(dealername)){
            pageQuery.setDealername(dealername);
        }


        int start = (pageQuery.getPage()-1)*pageQuery.getLimit();
        int end = pageQuery.getPage()*pageQuery.getLimit();
        pageQuery.setStart(start);
        pageQuery.setEnd(end);
        if(pageQuery.getSort().equals("+supplyid")){
            allSupply = supplyService.getSearch(pageQuery);
        }else if(pageQuery.getSort().equals("-supplyid")){
            allSupply = supplyService.getSearchDesc(pageQuery);
        }
        total =supplyService.getSearchCount(pageQuery);
        map.put("items",allSupply);
        map.put("total",total);
        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/deletesupply")
    public ReturnMessage deleteSupply(@RequestParam("query")String query){
        Storage storage = new Storage();
        Supply supply2 = supplyService.getSupplyById(Integer.parseInt(query));
        int count = 0;
        if (supply2.getStatus() == 1){
            if (storageService.getQuantityByDrugnameAndExp(supply2.getDrugname(),supply2.getExp())!=null){

                count = storageService.getQuantityByDrugnameAndExp(supply2.getDrugname(),supply2.getExp());
            }
            if (count != 0){
                storage.setDrugname(supply2.getDrugname());
                Integer oldQuantity = storageService.getQuantityByDrugnameAndExp(supply2.getDrugname(),supply2.getExp());
                storage.setQuantity(oldQuantity-supply2.getQuantity());
                storage.setExp(supply2.getExp());
                if(storage.getQuantity()!=0){
                    storageService.updateStorage(storage);
                }else {
                    storageService.deleteStorage(storage);
                }

            }
        }

        supplyService.deleteSupply(Integer.parseInt(query));
        return ReturnMessage.success();
    }


    @RequestMapping("/updatesupply")
    public ReturnMessage updateSupply(@RequestParam("query")String  query) throws ParseException {
        Map map = new HashMap();
        JSONObject jsonObject = JSON.parseObject(query);
        Supply supply = new Supply();
        supply.setSupplyid(jsonObject.getInteger("supplyid"));
        supply.setDealerid(jsonObject.getInteger("dealerid"));
        supply.setDealername(jsonObject.getString("dealername"));
        supply.setDrugid(jsonObject.getInteger("drugid"));
        supply.setDrugname(jsonObject.getString("drugname"));
        supply.setQuantity(jsonObject.getInteger("quantity"));
        supply.setHandler(jsonObject.getInteger("handler"));
        supply.setEdittime(jsonObject.getString("edittime"));
        String status= jsonObject.getString("status");
        String exp= jsonObject.getString("exp");
        supply.setUnivalent(jsonObject.getDouble("univalent"));
        Double TP = jsonObject.getDouble("univalent")*jsonObject.getInteger("quantity");
        BigDecimal b = new BigDecimal(TP);
        TP = b.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        supply.setTP(TP);
        supply.setExp(exp);


        Date date = new Date();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        String temp = sdf1.format(date);


        Date today = sdf1.parse(temp);


        Date expday = sdf1.parse(supply.getExp());

        if(expday.before(today)){
            supply.setStatus(2);
        }else if (getMonth(today,expday)>2){
            supply.setStatus(0);
        }else{
            supply.setStatus(1);
        }
        supplyService.updateSupply(supply);

        if (supply.getStatus()==1){
            Storage storage = new Storage();

            Supply supply2 = supplyService.getSupplyById(supply.getSupplyid());

            if (storageService.getIsTrue(supply.getDrugname(),supply.getExp())==0){
                storage.setDrugname(supply2.getDrugname());
                storage.setQuantity(supply2.getQuantity());
                storage.setExp(supply2.getExp());
                storageService.insertStorage(storage);
            }else{
                storage.setDrugname(supply2.getDrugname());
                Integer oldQuantity = storageService.getQuantityByDrugnameAndExp(supply2.getDrugname(),supply.getExp());
                storage.setQuantity(supply.getQuantity()+oldQuantity);
                storage.setExp(supply2.getExp());
                storageService.updateStorage(storage);
            }
        }
        return ReturnMessage.successWithData(map);
    }



    @RequestMapping("/getallsupply")
    public ReturnMessage getAllSupply(@RequestParam("query")String  listQuery) throws ParseException {
        JSONObject jsonObject = JSON.parseObject(listQuery);
        ListQuery listQuery1 = new ListQuery();

        List<Supply> allSupply = new ArrayList<>();
        Integer total = 0;
        //获取json对象中的信息
        listQuery1.setPage(jsonObject.getInteger("page"));
        listQuery1.setLimit(jsonObject.getInteger("limit"));
        listQuery1.setSort(jsonObject.getString("sort"));
        listQuery1.setIsAll(jsonObject.getBoolean("isAll"));
        Boolean downloadAll = jsonObject.getBoolean("downloadAll");



        int start = (listQuery1.getPage()-1)*listQuery1.getLimit();
        int end = listQuery1.getPage()*listQuery1.getLimit();


        Map map = new HashMap();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        if (downloadAll==true){
            Integer allSupplyCount = supplyService.getAllSupplyCount();
            allSupply = supplyService.getAllSupply(0,allSupplyCount);
        }else{
            if(!listQuery1.getIsAll()){
                if(listQuery1.getSort().equals("+supplyid")){
                    allSupply = supplyService.getAllSupplyByUserId(start,end,user.getUserid());
                    total = supplyService.getAllSupplyCountByid(user.getUserid());
                }else{
                    allSupply = supplyService.getAllSupplyByUserIdDesc(start,end,user.getUserid());
                    total = supplyService.getAllSupplyCountByid(user.getUserid());
                }
            }else{
                if(listQuery1.getSort().equals("+supplyid")){
                    allSupply = supplyService.getAllSupply(start,end);
                    total = supplyService.getAllSupplyCount();
                }else{
                    allSupply = supplyService.getAllSupplyDesc(start,end);
                    total = supplyService.getAllSupplyCount();
                }
            }
        }
        for (Supply supply : allSupply) {
            Date date = new Date();

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            String temp = sdf1.format(date);


            Date today = sdf1.parse(temp);


            Date expday = sdf1.parse(supply.getExp());

            if(expday.before(today) || expday.equals(today)){
                supply.setStatus(2);
            }else if (getMonth(today,expday)>2){
                supply.setStatus(0);
            }else{
                supply.setStatus(1);
            }
            supplyService.updateSupply(supply);
        }

        map.put("allSupply",allSupply);
        map.put("total",total);
        return ReturnMessage.successWithData(map);
    }

    @Scheduled(cron="0 0 0 * * 0-7")//什么时候执行
    public void autoUpdateStatus() throws ParseException {
        List<Supply> allSupply = new ArrayList<>();
        Integer allSupplyCount = supplyService.getAllSupplyCount();
        allSupply = supplyService.getAllSupply(0,allSupplyCount);
        for (Supply supply : allSupply) {
            Date date = new Date();

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            String temp = sdf1.format(date);


            Date today = sdf1.parse(temp);


            Date expday = sdf1.parse(supply.getExp());

            if(expday.before(today)){
                supply.setStatus(2);
            }else if (getMonth(today,expday)>2){
                supply.setStatus(0);
            }else{
                supply.setStatus(1);
            }
            supplyService.updateSupply(supply);
        }
    }

    @RequestMapping("/getdealerinfo")
    public ReturnMessage getDealerInfo(){
        Map map = new HashMap();
        List<DealerInfo> dealerInfo = dealerService.getDealerInfo();
        map.put("dealerInfo",dealerInfo);
        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/insertsupply")
    public ReturnMessage insertSupply(@RequestParam("query")String pq) throws ParseException {
        Map map = new HashMap();
        JSONObject jsonObject = JSONObject.parseObject(pq);
        Integer supplyid = jsonObject.getInteger("supplyid");
        String dealername = jsonObject.getString("dealername");
        String drugname = jsonObject.getString("drugname");
        Integer quantity = jsonObject.getInteger("quantity");
        String status= jsonObject.getString("status");
        String jointime = jsonObject.getString("jointime");
        String exp= jsonObject.getString("exp");

        Supply supply = new Supply();
        supply.setSupplyid(supplyid);


        if(jsonObject.getBoolean("isUpload")){
            supply.setHandler(jsonObject.getInteger("handler"));
            Calendar calendar = new GregorianCalendar(1900,0,-1);
           if(exp.indexOf("-")==-1){
               int intDay = Integer.parseInt(exp);
               Date dd = DateUtils.addDays(calendar.getTime(),intDay);
               //对日期格式化操作
               SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");

               exp = simpleDateFormat.format(dd);
           }
            supply.setSupplyid(supplyid);
        }else{
            Class<User> clazz = User.class;
            User user = jsonObject.getObject("handler",clazz);
            supply.setHandler(user.getUserid());
            supply.setSupplyid(null);
        }


        Date date = new Date();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

        String temp = sdf1.format(date);

        jointime = temp;

        Date today = sdf1.parse(temp);

        Date expday = sdf1.parse(exp);
        if(expday.before(today)){
            supply.setStatus(2);
        }else if (getMonth(today,expday)>2){
            supply.setStatus(0);
        }else{
            supply.setStatus(1);
        }


        Integer dealerid = dealerService.getDealerByDealername(dealername);
        Integer drugid = drugService.getDrugidByDrugname(drugname);

        supply.setDealerid(dealerid);
        supply.setDealername(dealername);
        supply.setDrugid(drugid);
        supply.setDrugname(drugname);
        supply.setQuantity(quantity);
        supply.setEdittime(jointime);
        supply.setJointime(jointime);
        supply.setExp(exp);
        supply.setUnivalent(jsonObject.getDouble("univalent"));
        Double TP = jsonObject.getDouble("univalent")*jsonObject.getInteger("quantity");
        BigDecimal b = new BigDecimal(TP);
        TP = b.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        supply.setTP(TP);
        supplyService.InsertSupply(supply);
        Storage storage = new Storage();

        storage.setDrugname(supply.getDrugname());
        storage.setQuantity(supply.getQuantity());
        storage.setExp(supply.getExp());
        Double price = jsonObject.getDouble("univalent")*1.1;
        BigDecimal t = new BigDecimal(price);
        price  = t.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        storage.setPrice(price );
        storage.setStatus(supply.getStatus());


        storageService.insertStorage(storage);

        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/supplystatus")
    public ReturnMessage supplyStatus(@RequestParam("query")String data){

        JSONObject jsonObject = JSONObject.parseObject(data);

        String drugname = jsonObject.getString("drugname");
        Integer supplyid = jsonObject.getInteger("supplyid");

        Map map = new HashMap();
        supplyService.updateStatus(supplyid);

        Storage storage = new Storage();

        Supply supply = supplyService.getSupplyById(supplyid);

        if (storageService.getIsTrue(supply.getDrugname(),supply.getExp())==0){
            storage.setDrugname(supply.getDrugname());
            storage.setQuantity(supply.getQuantity());
            storage.setExp(supply.getExp());
            storageService.insertStorage(storage);
        }else{
            storage.setDrugname(supply.getDrugname());
            Integer oldQuantity = storageService.getQuantityByDrugnameAndExp(supply.getDrugname(),supply.getExp());
            storage.setQuantity(supply.getQuantity()+oldQuantity);
            storage.setExp(supply.getExp());
            storageService.updateStorage(storage);
        }

        return ReturnMessage.successWithData(map);
    }

    //计算两个时间相差几个月
    public  int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        if ((startCalendar.get(Calendar.DATE) == 1)&& (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }
}
