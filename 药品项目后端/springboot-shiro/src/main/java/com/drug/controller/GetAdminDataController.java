package com.drug.controller;

import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.dto.SupplyCount;
import com.drug.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GetAdminDataController {

    @Autowired
    SupplyService supplyService;

    @RequestMapping("/getadmindataone")
    public ReturnMessage getAdmin(){
        Map map = new HashMap<>();


        List<SupplyCount> count = supplyService.getCount();


        map.put("alldata",count);

        return ReturnMessage.successWithData(map);
    }

}
