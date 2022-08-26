package com.drug.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drug.entity.dto.ListQuery;
import com.drug.entity.dto.PageQuery;
import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.pojo.Pickdrug;
import com.drug.entity.pojo.Storage;
import com.drug.entity.pojo.Supply;
import com.drug.service.GetMedicineService;
import com.drug.service.StorageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;


@RestController
@RequestMapping("/getmedicine")
public class GetMedicineController {

    @Autowired
    GetMedicineService getMedicineService;

    @Autowired
    StorageService storageService;



    @RequestMapping("/getall")
    public ReturnMessage getAll(@RequestParam("query")String  listQuery){

        JSONObject jsonObject = JSON.parseObject(listQuery);
        ListQuery listQuery1 = new ListQuery();

        List<Pickdrug> pickdrugs= new ArrayList<>();
        Integer total = 0;
        //获取json对象中的信息
        listQuery1.setPage(jsonObject.getInteger("page"));
        listQuery1.setLimit(jsonObject.getInteger("limit"));
        listQuery1.setSort(jsonObject.getString("sort"));
        Boolean downloadAll = jsonObject.getBoolean("downloadAll");



        int start = (listQuery1.getPage()-1)*listQuery1.getLimit();
        int end = listQuery1.getPage()*listQuery1.getLimit();


        Map map = new HashMap();

        if (downloadAll==true){
            Integer allGetMedicineCount = getMedicineService.getAllGetMedicineCount();
            pickdrugs = getMedicineService.getAllPickdrug(0,allGetMedicineCount);
        }else{
            if(listQuery1.getSort().equals("+id")){
                pickdrugs = getMedicineService.getAllPickdrug(start,end);
                total = getMedicineService.getAllGetMedicineCount();
            }else{
                pickdrugs = getMedicineService.getAllPickdrugDesc(start,end);
                total = getMedicineService.getAllGetMedicineCount();
            }
        }
        for (Pickdrug pickdrug : pickdrugs) {
            String exp = pickdrug.getExp();
            pickdrug.setExp(exp.replace("#","\n"));
        }


        map.put("allpick",pickdrugs);
        map.put("total",total);

        return ReturnMessage.successWithData(map);
    }


    @RequestMapping("/allgetmedicinelist")
    public ReturnMessage getAllCount(@RequestParam("query")String temp){

        JSONObject jsonObject = JSON.parseObject(temp);

        Pickdrug pickdrug = new Pickdrug();

        pickdrug.setDrugname(jsonObject.getString("drugname"));
        pickdrug.setHandler(jsonObject.getInteger("handler"));
        pickdrug.setQuantity(jsonObject.getInteger("quantity"));
        pickdrug.setDruguser(jsonObject.getString("druguser"));
        pickdrug.setJointime(jsonObject.getString("jointime"));


        List<Storage> storages = storageService.getStorageByDrugname(pickdrug.getDrugname());
        Integer tempnum = 0;
        Boolean flag = false;
        for (Storage storage : storages) {
            if (flag==false){
                if (pickdrug.getQuantity()<storage.getQuantity() || (tempnum <= storage.getQuantity() && tempnum!=0)){
                    Integer count = 0;
                    if(pickdrug.getQuantity()<storage.getQuantity() ){
                        count =  storage.getQuantity()-pickdrug.getQuantity();
                    }else if (tempnum <= storage.getQuantity()){
                        count =  storage.getQuantity()-tempnum;//20
                    }
                    storage.setQuantity(count);
                    storageService.updateStorage(storage);
                    flag = true;
                }else{
                    if(tempnum!=0){
                        if(storage.getQuantity()>tempnum){
                            tempnum = storage.getQuantity()-tempnum;
                            storage.setQuantity(tempnum);
                            storageService.updateStorage(storage);
                            tempnum = 0;
                        }else{
                            tempnum = tempnum-storage.getQuantity();
                            storage.setQuantity(0);
                            storageService.updateStorage(storage);
                        }

                    }else{
                        tempnum = pickdrug.getQuantity()-storage.getQuantity();//100
                        storage.setQuantity(0);
                        storageService.updateStorage(storage);
                    }
                }
            }
        }
        if(flag==true){
            getMedicineService.insertPickdrug(pickdrug);
        }


        return ReturnMessage.success();
    }


    @RequestMapping("/delmedicine")
    public ReturnMessage  delMedicine(@RequestParam("query")String id){
        getMedicineService.deletePickdrug(Integer.parseInt(id));
        return ReturnMessage.success();
    }

    @RequestMapping("/getsearch")
    public ReturnMessage getSearch(@RequestParam("query")String pq){
        Map map = new HashMap<>();
        List<Pickdrug> allPickdrug = new ArrayList<>();
        PageQuery pageQuery = new PageQuery();
        int total=0;
        JSONObject jsonObject = JSON.parseObject(pq);
        //获取json对象中的信息
        pageQuery.setPage(jsonObject.getInteger("page"));
        pageQuery.setLimit(jsonObject.getInteger("limit"));
        pageQuery.setSort(jsonObject.getString("sort"));
        String drugname = jsonObject.getString("drugname");
        String druguser = jsonObject.getString("druguser");
        if(drugname != null && !StringUtils.isEmpty(drugname)){
            pageQuery.setDrugname(drugname);
        }
        if(druguser != null && !StringUtils.isEmpty(druguser)){
            pageQuery.setDruguser(druguser);
        }


        int start = (pageQuery.getPage()-1)*pageQuery.getLimit();
        int end = pageQuery.getPage()*pageQuery.getLimit();
        pageQuery.setStart(start);
        pageQuery.setEnd(end);
        if(pageQuery.getSort().equals("+id")){
            allPickdrug = getMedicineService.getSearch(pageQuery);
        }else if(pageQuery.getSort().equals("-id")){
            allPickdrug  = getMedicineService.getSearchDesc(pageQuery);
        }
        for (Pickdrug pickdrug : allPickdrug) {
            String exp = pickdrug.getExp();
            pickdrug.setExp(exp.replace("#","\n"));
        }
        total = getMedicineService.getSearchCount(pageQuery);
        map.put("items",allPickdrug );
        map.put("total",total);
        return ReturnMessage.successWithData(map);
    }
}
