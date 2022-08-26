package com.drug.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drug.entity.dto.*;
import com.drug.entity.pojo.Drug;
import com.drug.entity.pojo.DrugDetails;
import com.drug.entity.pojo.User;
import com.drug.service.DrugService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiresAuthentication//检查是否认证注解
@RequestMapping("/drug")
public class DrugController {

    @Autowired
    DrugService drugService;

    @RequestMapping("/deletedrug")
    public void deletedrug(@RequestParam("query")String pq){
        JSONObject jsonObject = JSON.parseObject(pq);
        Integer drugid = jsonObject.getInteger("drugid");

        drugService.deleteDrug(drugid);
        drugService.deleteDrugByDrugid(drugid);
    }

    @RequestMapping("/insertdrug")
    public ReturnMessage insertDrug(@RequestParam("query")String pq){
        JSONObject jsonObject = JSON.parseObject(pq);
        Drug drug = new Drug();

        drug.setDrugname(jsonObject.getString("drugname"));
        drug.setApprovalnum(jsonObject.getString("approvalnum"));
        drug.setCompany(jsonObject.getString("company"));
        drug.setStandardcode(jsonObject.getString("standardcode"));

        drugService.insertDrug(drug);

        DrugDetails drugDetails = new DrugDetails();

        drugDetails.setDrugname(jsonObject.getString("drugname"));
        drugDetails.setIngredients(jsonObject.getString("ingredients"));
        drugDetails.setDrugclass(jsonObject.getString("drugclass"));
        drugDetails.setOtc(jsonObject.getString("otc"));
        drugDetails.setSpecifications(jsonObject.getString("specifications"));
        drugDetails.setDrugtype(jsonObject.getString("drugtype"));
        drugDetails.setIndication(jsonObject.getString("indication"));
        drugDetails.setUsageanddosage(jsonObject.getString("usageanddosage"));
        drugDetails.setStorage(jsonObject.getString("storage"));
        drugDetails.setAdversereaction(jsonObject.getString("adversereaction"));
        drugDetails.setTaboo(jsonObject.getString("taboo"));
        drugDetails.setDrugregulatoryclassification(jsonObject.getString("drugregulatoryclassification"));
        drugDetails.setFunctionalindications(jsonObject.getString("functionalindications"));
        drugDetails.setCharacter(jsonObject.getString("character"));

        drugService.insertDrugDetails(drugDetails);

        return ReturnMessage.success();
    }

    @RequestMapping("/search")
    public ReturnMessage searchUser(@RequestParam("query")String pq){
        Map map = new HashMap<>();
        List<Drug> allDrug = new ArrayList<>();
        PageQuery pageQuery = new PageQuery();
        int total=0;
        JSONObject jsonObject = JSON.parseObject(pq);
        //获取json对象中的信息
        pageQuery.setPage(jsonObject.getInteger("page"));
        pageQuery.setLimit(jsonObject.getInteger("limit"));
        pageQuery.setSort(jsonObject.getString("sort"));
        String standardcode = jsonObject.getString("standardcode");
        String drugname = jsonObject.getString("drugname");

        if(drugname!= null && !StringUtils.isEmpty(drugname)){
            pageQuery.setDrugname(drugname);
        }
        if(standardcode  != null && !StringUtils.isEmpty(standardcode )){
            pageQuery.setStandardcode(standardcode);
        }

        System.out.println(pageQuery.getSort()+"==========================>");

        int start = (pageQuery.getPage()-1)*pageQuery.getLimit();
        int end = pageQuery.getPage()*pageQuery.getLimit();
        pageQuery.setStart(start);
        pageQuery.setEnd(end);
        if(pageQuery.getSort().equals("+drugid")){
            allDrug = drugService.getSearch(pageQuery);
        }else if(pageQuery.getSort().equals("-drugid")){
            allDrug = drugService.getSearchDesc(pageQuery);
        }
        total = drugService.getSearchCount(pageQuery);
        map.put("items",allDrug);
        map.put("total",total);
        return ReturnMessage.successWithData(map);
    }


    @RequestMapping("/getinsertdrug")
    public ReturnMessage getEnterDrug(@RequestParam("query")String pq){
        HashMap map = new HashMap();


        JSONObject jsonObject = JSON.parseObject(pq);
        PageQuery pageQuery = new PageQuery();
        //获取json对象中的信息
        pageQuery.setPage(jsonObject.getInteger("page"));
        pageQuery.setLimit(jsonObject.getInteger("limit"));
        pageQuery.setSort(jsonObject.getString("sort"));

        int start = (pageQuery.getPage()-1)*pageQuery.getLimit();
        int end = pageQuery.getPage()*pageQuery.getLimit();

        pageQuery.setStart(start);
        pageQuery.setEnd(end);

        List<Drug> drugList = null;



        if(pageQuery.getSort().equals("+drugid")){
            drugList = drugService.getEnterDrug(pageQuery);
        }else if((pageQuery.getSort().equals("-drugid"))){
            drugList = drugService.getEnterDrugDesc(pageQuery);
        }

        System.out.println(pageQuery.getSort()+"==========================>");
        int count = drugService.getEnterDrugCount();
        map.put("allenterdrug",drugList);
        map.put("total",count);

        return ReturnMessage.successWithData(map);
    }



    @RequestMapping("/getalldrugname")
    public ReturnMessage getAllDrugName(){



        Map map = new HashMap<>();
        List<String> drugNamelist = drugService.getAllDrugName();
        List<DrugName> list = new ArrayList<>();
        for (String str : drugNamelist) {
            DrugName name = new DrugName();
            name.setValue(str);
            list.add(name);
        }
        map.put("drugnamelist",list);
        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/getbydrugname")
    public ReturnMessage getByDrugName(@RequestParam("query")String drugname){
        Map map = new HashMap<>();
        Integer flag = drugService.getByDrugName(drugname);
        map.put("flag",flag);
        return ReturnMessage.successWithData(map);
    }
    @RequestMapping("/getdruginfo")
    public ReturnMessage getDrugInfo(@RequestParam("query")String drugname){
        Map map = new HashMap();


        DrugInfo di = drugService.getDrugInfo(drugname);

        map.put("druginfo",di);

        return ReturnMessage.successWithData(map);
    }
}
