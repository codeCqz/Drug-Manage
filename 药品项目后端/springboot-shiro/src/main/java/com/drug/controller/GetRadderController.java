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
public class GetRadderController {


    @Autowired
    SupplyService supplyService;

    @RequestMapping("/getradder")
    public ReturnMessage getRadder(){
        Boolean isShow = false;

        Map map = new HashMap();

        String[]  radder = supplyService.getRadder();
        int[] arr = new int[5];

        for (int i = 0; i < radder.length; i++) {
            Integer everyExp = supplyService.getEveryExp(radder[i]);
            arr[i] = everyExp;
        }

        int[] arr2 = new int[5];
        int count = 0;
        for (int i  = 0; i < radder.length; i++) {
            List<Supply> everyExp = supplyService.getEveryDrug(radder[i]);
            for (Supply supply : everyExp) {
                Integer quantity = supply.getQuantity();
                count+=quantity;
            }
            arr2[i] = count;
        }
        int[] arr3 = new int[5];
        for (int i = 0; i < radder.length; i++) {
            Integer every = supplyService.getEverySupply(radder[i]);
            arr3[i] = every;
        }
        if (arr3[0]==0 &&  arr3[1]==0 && arr3[2]==0 && arr3[3]==0 && arr3[4]==0){
            isShow = true;
        }
        System.out.println(isShow);
        map.put("radder",radder);
        map.put("everyExp",arr);
        map.put("everyDrug",arr2);
        map.put("everySupply",arr3);
        map.put("isShow",isShow);
        return ReturnMessage.successWithData(map);
    }
}
