package com.drug.controller;


import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.pojo.Pickdrug;
import com.drug.entity.pojo.User;
import com.drug.service.GetMedicineService;
import org.apache.ibatis.annotations.Delete;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
public class GetPickerDataController {

    @Autowired
    GetMedicineService getMedicineService;

    @RequestMapping("/getbardata")
    public ReturnMessage getBarData(){
        HashMap map = new HashMap();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        List<Pickdrug> pds = getMedicineService.getAllPickdrugByID(user.getUserid());
        ArrayList<Pickdrug> temp = new ArrayList();

        for(int i =0 ;i< pds.size();i++){
            Pickdrug pickdrug = pds.get(i);
            if(i == 0){
                temp.add(pickdrug);
            }else {
                boolean flag = false;
                for(int j = 0 ; j<temp.size();j++){
                    Pickdrug tempdrug = temp.get(j);
                    if(tempdrug.getDrugname().equals(pickdrug.getDrugname())){
                        int count =  pickdrug.getQuantity()+tempdrug.getQuantity();
                        tempdrug.setQuantity(count);
                        temp.set(j,tempdrug);
                        flag = true;
                    }
                }
                if(!flag){
                    temp.add(pickdrug);
                }else{
                    flag = false;
                }
            }

        }
        map.put("bardata",temp);
        return ReturnMessage.successWithData(map);
    }

}
