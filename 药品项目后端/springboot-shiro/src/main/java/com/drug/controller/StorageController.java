package com.drug.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drug.entity.dto.DrugName;
import com.drug.entity.dto.ListQuery;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.pojo.Storage;
import com.drug.entity.pojo.Supply;
import com.drug.service.DrugService;
import com.drug.service.StorageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@CrossOrigin
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    StorageService storageService;

    @Autowired
    DrugService drugService;


    @RequestMapping("/fetchprice")
    public ReturnMessage fetchPrice(@RequestParam("query")String drugname){

        List<Double> price  = storageService.getPriceByDrugname(drugname);

        Map<String,Object> map = new HashMap<>();

        map.put("price",String.valueOf(price.get(0)));

        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/fetchexpnum")
    public ReturnMessage fetchExpNum(@RequestParam("query")String query){
        JSONObject json = JSONObject.parseObject(query);

        String exp = json.getString("exp");
        String drugname = json.getString("drugname");

        String[] date = exp.split("#");

        int count = 0;
        for (String s : date) {
            if(s!=null && !s.equals("")){
                Integer quantity = storageService.getQuantityByDrugnameAndExp(drugname,s);
                count+=quantity;
            }
        }


        Map<String, Object> map = new HashMap<>();

        map.put("count",count);

        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/getallexp")
    public ReturnMessage getAllExp(@RequestParam("query")String drugname){
        Map<String, Object> map = new HashMap<>();

        List<String> allExpByDrugname = storageService.getAllExpByDrugname(drugname);

        map.put("explist",allExpByDrugname);
        return ReturnMessage.successWithData(map);
    }



    @RequestMapping("/deletestorage")
    public ReturnMessage deleteStorage(@RequestParam("query")Integer storageid){
        storageService.deleteStorageByID(storageid);

        return ReturnMessage.success();
    }



    @RequestMapping("/updatestorage")
    public ReturnMessage  updateStorage(@RequestParam("query")String pq){
        JSONObject jsonObject = JSONObject.parseObject(pq);
        Storage storage = new Storage();

        storage.setStorageid(jsonObject.getInteger("storageid"));
        storage.setDrugname(jsonObject.getString("drugname"));
        storage.setQuantity(jsonObject.getInteger("quantity"));
        storage.setExp(jsonObject.getString("exp"));
        storage.setPrice(jsonObject.getDouble("price"));

        storageService.updateStorageInfo(storage);

        return ReturnMessage.success();
    }



    @RequestMapping("/getdrugnamelist")
    public ReturnMessage getDrugNameList(){
        List<String> list = new ArrayList();

        Map map = new HashMap();
        list = storageService.getDrugnameList();

        List<DrugName> drugnamelist = new ArrayList<>();
        for (String s : list) {
            DrugName dn = new DrugName();
            dn.setValue(s);
            drugnamelist.add(dn);
        }

        map.put("drugnamelist",drugnamelist);
        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/getsearchstorage")
    public ReturnMessage getSearchDealer(@RequestParam("query")String pq){
        Map map = new HashMap<>();
        List<Storage> allStorage = new ArrayList<>();
        PageQuery pageQuery = new PageQuery();
        int total=0;
        JSONObject jsonObject = JSON.parseObject(pq);
        //获取json对象中的信息
        pageQuery.setPage(jsonObject.getInteger("page"));
        pageQuery.setLimit(jsonObject.getInteger("limit"));
        pageQuery.setSort(jsonObject.getString("sort"));
        String drugname = jsonObject.getString("drugname");
        if(drugname != null && !StringUtils.isEmpty(drugname)){
            pageQuery.setDrugname(drugname);
        }

        int start = (pageQuery.getPage()-1)*pageQuery.getLimit();
        int end = pageQuery.getPage()*pageQuery.getLimit();
        pageQuery.setStart(start);
        pageQuery.setEnd(end);
        if(pageQuery.getSort().equals("+storageid")){
            allStorage  = storageService.getSearch(pageQuery);
        }else if(pageQuery.getSort().equals("-storageid")){
            allStorage = storageService.getSearchDesc(pageQuery);
        }
        total = storageService.getSearchCount(pageQuery);
        map.put("items",allStorage );
        map.put("total",total);
        return ReturnMessage.successWithData(map);
    }






    @RequestMapping("/getallstoragedrugname")
    public ReturnMessage getAllStorageDrugname(){
        HashMap map = new HashMap();

        List<String> drugNamelist = storageService.getAllStorageDrugName();
        List<DrugName> list = new ArrayList<>();
        for (String str : drugNamelist) {
            DrugName name = new DrugName();
            name.setValue(str);
            list.add(name);
        }
        map.put("drugname",list);

        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/getnumanddrugname")
    public ReturnMessage getDrugnameAndNum(@RequestParam("query")String drugname){
            HashMap map = new HashMap();
            List<Storage> storages = storageService.getStorageByDrugname(drugname);
            Integer flag = storages.size();
            Integer count = 0;
            for (Storage storage : storages) {
                count+=storage.getQuantity();
            }
            map.put("flag",flag);
            map.put("count",count);
            return ReturnMessage.successWithData(map);
    }


    @RequestMapping("/getallstock")
    public ReturnMessage getAllStock(@RequestParam("query")String  listQuery) throws ParseException {
        Map map = new HashMap<>();

        JSONObject jsonObject = JSON.parseObject(listQuery);
        ListQuery listQuery1 = new ListQuery();

        List<Storage> allStorage = new ArrayList<>();
        Integer total = 0;
        //获取json对象中的信息
        listQuery1.setPage(jsonObject.getInteger("page"));
        listQuery1.setLimit(jsonObject.getInteger("limit"));
        listQuery1.setSort(jsonObject.getString("sort"));




        int start = (listQuery1.getPage()-1)*listQuery1.getLimit();
        int end = listQuery1.getPage()*listQuery1.getLimit();



            if(listQuery1.getSort().equals("+storageid")){
                allStorage = storageService.getAllStorage(start,end);
                total = storageService.getAllStorageCount();
            }else{
                allStorage= storageService.getAllStorageDesc(start,end);
                total = storageService.getAllStorageCount();
            }



        for (Storage storage : allStorage) {
            Date date = new Date();

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

            String temp = sdf1.format(date);


            Date today = sdf1.parse(temp);


            Date expday = sdf1.parse(storage.getExp());

            if(expday.before(today) || expday.equals(today)){
                storage.setStatus(2);
            }else if (getMonth(today,expday)>2){
                storage.setStatus(0);
            }else{
                storage.setStatus(1);
            }
            storageService.updateStorage(storage);
        }

        map.put("allStorage",allStorage);
        map.put("total",total);

        return ReturnMessage.successWithData(map);
    }

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
