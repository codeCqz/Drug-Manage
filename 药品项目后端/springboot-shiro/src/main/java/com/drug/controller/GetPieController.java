package com.drug.controller;


import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.pojo.Supply;
import com.drug.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GetPieController {

    @Autowired
    SupplyService supplyService;


    @RequestMapping("/getpiedata")
    public ReturnMessage getPieData(){
        Map map = new HashMap<>();

        String[] arr = supplyService.getPieDataString();

        int[] allint = new int[arr.length];
        for (int i = 0 ; i<arr.length;i++) {
            List<Supply> quantity = supplyService.getPieDataQuantity(arr[i]);
            int allCount = 0;
            for (Supply supply : quantity) {
                Integer count = supply.getQuantity();
                allCount+=count;
            }
            allint[i] = allCount;

        }
        map.put("allpiedata",arr);
        map.put("count",allint);
        return ReturnMessage.successWithData(map);
    }
}
