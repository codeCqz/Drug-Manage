package com.drug.controller;


import com.alibaba.fastjson.JSONObject;
import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.pojo.Pickdrug;
import com.drug.entity.pojo.Storage;
import com.drug.service.PickDrugService;
import com.drug.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/pickdrug")
public class PickDrugController {


    @Autowired
    StorageService storageService;


    @Autowired
    PickDrugService pickDrugService;


    @RequestMapping("/update")
    public ReturnMessage updatePickDrug(@RequestParam("query")String query){
        JSONObject jsonObject = JSONObject.parseObject(query);
        Pickdrug pd = jsonObject.getObject("pd", Pickdrug.class);


        pickDrugService.updatePickDrug(pd);

        return ReturnMessage.success();
    }

    @RequestMapping("/insert")
    public ReturnMessage insertPickdrug(@RequestParam("query")String query){
        JSONObject jsonObject = JSONObject.parseObject(query);
        Pickdrug pd = jsonObject.getObject("pd", Pickdrug.class);



        double TP = pd.getPrice()*pd.getQuantity();
        BigDecimal b = new BigDecimal(TP);
        TP = b.setScale(2, RoundingMode.HALF_DOWN).doubleValue();

        pd.setTP(TP);


        String drugname = pd.getDrugname();
        String exp = pd.getExp();
        int q =  pd.getQuantity();


        String[] strings = exp.split("#");

        boolean deduct = false;

        boolean flag = false;

        int count = 0;

        Map<String,Integer> map = new HashMap();

        for (String s : strings) {
            if(s!=null && !s.equals("")){
                System.out.println(s);
                Integer quantity = storageService.getQuantityByDrugnameAndExp(drugname,s);
                map.put(s,quantity);
                count+=quantity;
            }
        }
        for (String s : strings) {
            if (s!=null && !s.equals("")){
                if (!deduct){
                    if(q < map.get(s)){
                        Storage storage = new Storage();
                        storage.setDrugname(drugname);
                        storage.setExp(s);
                        Integer c =  map.get(s)-q;
                        storage.setQuantity(c);
                        storageService.updateStotageByExpAndDrugname(storage);
                        deduct=true;
                    }else{
                        Storage storage = new Storage();
                        storage.setDrugname(drugname);
                        storage.setExp(s);
                        Integer c =  q-map.get(s);
                        storage.setQuantity(c);
                        storageService.deleteStorage(storage);
                    }
                }
            }
        }


        pickDrugService.insertPickDrug(pd);

        return ReturnMessage.success();
    }
}
