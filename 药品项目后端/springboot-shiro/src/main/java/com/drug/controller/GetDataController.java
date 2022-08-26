package com.drug.controller;


import com.drug.entity.dto.ReturnMessage;
import com.drug.entity.pojo.Pickdrug;
import com.drug.entity.pojo.Supply;
import com.drug.service.PickDrugService;
import com.drug.service.StorageService;
import com.drug.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class GetDataController {

    @Autowired
    private SupplyService supplyService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PickDrugService pickDrugService;


    @RequestMapping("/getprofit")
    public ReturnMessage getProfit(){
        Map map = new HashMap();
        double[] arr = new double[7];

        Date date = new Date();
        date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String deDate  = sdf.format(date);
        for (int i = 0; i < 7; i++) {
            String[] strings = deDate.split("-");
            List<Double> supplyTP = supplyService.getSupplyTP(deDate);

            double today = 0;


            for (Double aDouble : supplyTP) {
                today+=aDouble;
            }

            arr[i] = today;
            Calendar cl = setCalendar(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
            Calendar beforeDay = getBeforeDay(cl);
            deDate = printCalendar(beforeDay);
        }
        double[]  reverse = reverseDouble(arr, arr.length);
        map.put("order",reverse);

        deDate  = sdf.format(date);
        for (int i = 0; i < 7; i++) {
            String[] strings = deDate.split("-");
            List<Double> pickTP = pickDrugService.getPickTP(deDate);

            double today = 0;


            for (Double aDouble : pickTP) {
                today+=aDouble;
            }

            arr[i] = today;
            Calendar cl = setCalendar(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
            Calendar beforeDay = getBeforeDay(cl);
            deDate = printCalendar(beforeDay);
        }
        double[]  reverse2 = reverseDouble(arr, arr.length);
        map.put("pick",reverse2);

        deDate  = sdf.format(date);


        for (int i = 0; i < 7; i++) {
            String[] strings = deDate.split("-");
            List<Pickdrug> pick = pickDrugService.getAllPickByJoinTime(deDate);


            double today = 0;


            for (Pickdrug pickdrug : pick) {
                String drugname = pickdrug.getDrugname();
                Integer quantity = pickdrug.getQuantity();
                Double price = pickdrug.getPrice();
                String exp = pickdrug.getExp();
                String[] strings1 = exp.split("#");
                for (String s : strings1) {
                    if (s!=null && !s.equals("")){
                        pickdrug.setExp(s);
                    }
                }
                double v = supplyService.getunivalentBydrugnameandexp(drugname, pickdrug.getExp());
                double count = price*quantity - v*quantity;
                today+=count;
            }
            BigDecimal b = new BigDecimal(today);
            today = b.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();

            arr[i] = today;
            Calendar cl = setCalendar(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
            Calendar beforeDay = getBeforeDay(cl);
            deDate = printCalendar(beforeDay);
        }

        double[]  reverse3 = reverseDouble(arr, arr.length);

        map.put("profit",reverse3);

        return ReturnMessage.successWithData(map);
    }


    public static double[] reverseDouble(double a[], int n){
        double[] b = new double[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return b;
    }


    @RequestMapping("/getdata")
    public ReturnMessage getBuyerAllData(@RequestParam("query")String role){
        if(role.equals("buyer") || role.equals("admin") || role.equals("drugpicker")){
            HashMap map = new HashMap();
            Integer classCount = supplyService.getBuyerDataClassCount();
            Date date = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = sdf1.format(date);
            Integer todayNewAdd = supplyService.getBuyerDataToDayNewAdd(nowDate);
            Integer expiredSupply = storageService.getWillExpiredDrug();
            Integer quantity = 0;
            Integer allCount = 0;
            Integer allSupplyCount = supplyService.getAllSupplyCount();
            List<Supply> allSupply = supplyService.getAllSupply(0, allSupplyCount);
            for (Supply supply : allSupply) {
                if(supply.getStatus()==0 || supply.getStatus()==1 ){
                    quantity  = supply.getQuantity();
                    allCount+=quantity;
                }
            }
            map.put("classCount",classCount);
            map.put("todayNewAdd",todayNewAdd);
            map.put("expiredSupply",expiredSupply);
            map.put("allCount", allCount);
            return ReturnMessage.successWithData(map);
        }else{
            return ReturnMessage.failWithMsg("角色识别失败！");
        }
    }


    @RequestMapping("/getallcount")
    public  ReturnMessage getAllCount(@RequestParam("query")String role){
        Map map = new HashMap();
        int[] arr = new int[7];

        Date date = new Date();
        Integer quantity = 0;
        date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String deDate = deDate = sdf.format(date);
        String[] dateArr = new String[7];
        for (int i = 0; i < 7; i++) {
            dateArr[i] = deDate;
            String[] strings = deDate.split("-");
            Calendar cl = setCalendar(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
            Calendar beforeDay = getBeforeDay(cl);
            deDate = printCalendar(beforeDay);
        }


        Integer allCount = 0;
        Integer allSupplyCount = supplyService.getAllSupplyCount();
        List<Supply> allSupply = supplyService.getAllSupply(0, allSupplyCount);

        for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0:
                    for (Supply supply : allSupply) {
                        if (supply.getStatus() != 2) {
                            quantity = supply.getQuantity();
                            allCount += quantity;
                        }
                    }
                    arr[0] = allCount;
                    break;
                case 1:
                    allCount = 0;
                    quantity = 0;
                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0])) {
                            if (supply.getStatus() != 2) {
                                quantity = supply.getQuantity();
                                allCount += quantity;
                            }
                        }
                    }
                    arr[1] = allCount;
                    break;
                case 2:
                    allCount = 0;
                    quantity = 0;
                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1])) {
                            if (supply.getStatus() != 2) {
                                quantity = supply.getQuantity();
                                allCount += quantity;
                            }
                        }
                    }
                    arr[2] = allCount;
                    break;
                case 3:
                    allCount = 0;
                    quantity = 0;
                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1]) && !supply.getJointime().equals(dateArr[2])) {
                            if (supply.getStatus() != 2) {
                                quantity = supply.getQuantity();
                                allCount += quantity;
                            }
                        }
                    }
                    arr[3] = allCount;
                    break;
                case 4:
                    allCount = 0;
                    quantity = 0;
                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1]) && !supply.getJointime().equals(dateArr[2]) && !supply.getJointime().equals(dateArr[3])) {
                            if (supply.getStatus() != 2) {
                                quantity = supply.getQuantity();
                                allCount += quantity;
                            }
                        }
                    }
                    arr[4] = allCount;
                    break;
                case 5:
                    allCount = 0;
                    quantity = 0;
                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1]) && !supply.getJointime().equals(dateArr[2]) && !supply.getJointime().equals(dateArr[3])
                                && !supply.getJointime().equals(dateArr[4])) {
                            if (supply.getStatus() != 2) {
                                quantity = supply.getQuantity();
                                allCount += quantity;
                            }
                        }
                    }
                    arr[5] = allCount;
                    break;
                case 6:
                    allCount = 0;
                    quantity = 0;
                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1]) && !supply.getJointime().equals(dateArr[2]) && !supply.getJointime().equals(dateArr[3])
                                && !supply.getJointime().equals(dateArr[4]) && !supply.getJointime().equals(dateArr[5])) {
                            if (supply.getStatus() != 2) {
                                quantity = supply.getQuantity();
                                allCount += quantity;
                            }
                        }
                    }
                    arr[6] = allCount;
                    break;
            }

        }

        int[] reverse = reverse(arr,arr.length);
        map.put("allcount",reverse);
        return ReturnMessage.successWithData(map);
    }


    @RequestMapping("/getallexpire")
    public ReturnMessage getAllExpire(@RequestParam("query")String role){
        Map map = new HashMap();
        int[] arr = new int[7];

        Date date = new Date();
        date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String deDate = deDate = sdf.format(date);
        String[] dateArr = new String[7];
        for (int i = 0; i < 7; i++) {
            dateArr[i] = deDate;
            String[] strings = deDate.split("-");
            Calendar cl = setCalendar(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
            Calendar beforeDay = getBeforeDay(cl);
            deDate = printCalendar(beforeDay);
        }

        Integer allCount = 0;
        Integer allSupplyCount = supplyService.getAllSupplyCount();
        List<Supply> allSupply = supplyService.getAllSupply(0, allSupplyCount);

        for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0:
                    for (Supply supply : allSupply) {
                        if (supply.getStatus()== 1) {
                            allCount += 1;
                        }
                    }
                    arr[0] = allCount;
                    break;
                case 1:
                    allCount = 0;
                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0])) {
                            if (supply.getStatus()== 1) {
                                allCount += 1;
                            }
                        }
                    }
                    arr[1] = allCount;
                    break;
                case 2:
                    allCount = 0;
                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1])) {
                            if (supply.getStatus()== 1) {
                                allCount += 1;
                            }
                        }
                    }
                    arr[2] = allCount;
                    break;
                case 3:
                    allCount = 0;

                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1]) && !supply.getJointime().equals(dateArr[2])) {
                            if (supply.getStatus()== 1) {

                                allCount += 1;
                            }
                        }
                    }
                    arr[3] = allCount;
                    break;
                case 4:
                    allCount = 0;

                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1]) && !supply.getJointime().equals(dateArr[2]) && !supply.getJointime().equals(dateArr[3])) {
                            if (supply.getStatus()== 1) {

                                allCount += 1;
                            }
                        }
                    }
                    arr[4] = allCount;
                    break;
                case 5:
                    allCount = 0;

                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1]) && !supply.getJointime().equals(dateArr[2]) && !supply.getJointime().equals(dateArr[3])
                                && !supply.getJointime().equals(dateArr[4])) {
                            if (supply.getStatus()== 1) {
                                allCount += 1;
                            }
                        }
                    }
                    arr[5] = allCount;
                    break;
                case 6:
                    allCount = 0;

                    for (Supply supply : allSupply) {
                        if (!supply.getJointime().equals(dateArr[0]) && !supply.getJointime().equals(dateArr[1]) && !supply.getJointime().equals(dateArr[2]) && !supply.getJointime().equals(dateArr[3])
                                && !supply.getJointime().equals(dateArr[4]) && !supply.getJointime().equals(dateArr[5])) {
                            if (supply.getStatus()== 1) {
                                allCount += 1;
                            }
                        }
                    }
                    arr[6] = allCount;
                    break;
            }

        }

        int[] reverse = reverse(arr,arr.length);
        map.put("expire",reverse);
        return ReturnMessage.successWithData(map);
    }

    @RequestMapping("/getdayadd")
    public ReturnMessage getDayAdd(@RequestParam("query")String role){
        Map map = new HashMap();
        int[] arr = new int[7];

        Date date = new Date();
        date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String deDate = deDate = sdf.format(date);
        for (int i = 0; i < 7; i++) {
            String[] strings = deDate.split("-");
            Integer today = supplyService.getBuyerDiffDataDayAdd(deDate);
            arr[i] = today;
            Calendar cl = setCalendar(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
            Calendar beforeDay = getBeforeDay(cl);
            deDate = printCalendar(beforeDay);
        }
        int[] reverse = reverse(arr, arr.length);
        map.put("dayadd",reverse);
        return ReturnMessage.successWithData(map);

    }

    @RequestMapping("/getdrugclass")
    public ReturnMessage getBuyerAllDrugClassData(@RequestParam("query")String role){
        Map map = new HashMap();
        int[] arr = new int[7];
        Date date = new Date();
        date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String deDate = deDate = sdf.format(date);
        for (int i = 0; i < 7; i++) {
            String[] strings = deDate.split("-");
            Integer today = supplyService.getBuyerDiffDataClassCount(deDate);
            arr[i] = today;
            Calendar cl = setCalendar(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
            Calendar beforeDay = getBeforeDay(cl);
            deDate = printCalendar(beforeDay);
        }
        int[] reverse = reverse(arr, arr.length);
        map.put("drugclass",reverse);
        return ReturnMessage.successWithData(map);
    }
    public static int[] reverse(int a[], int n){
        int[] b = new int[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return b;
    }
    public static Calendar setCalendar(int year, int month, int date){
        Calendar cl = Calendar.getInstance();
        cl.set(year, month-1, date);
        return cl;
    }

    /**
     * 获取当前时间的前一天时间
     * @param cl
     * @return
     */
    private static Calendar getBeforeDay(Calendar cl){
        //使用roll方法进行向前回滚
        //cl.roll(Calendar.DATE, -1);
        //使用set方法直接进行设置
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day-1);
        return cl;
    }
    /**
     * 打印时间
     * @param cl
     */
    public String  printCalendar(Calendar cl) {
        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH) + 1;
        int day = cl.get(Calendar.DATE);
        String mon = null;
        String d = null;
        if (month <= 9 && month >= 1) {
            if (day <= 9 && day >= 1) {
                d = "0" + day;
            }
            mon = "0" + month;
        } else {
            if (day <= 9 && day >= 1) {
                d = "0" + day;
            }
            return year + "-" + month + "-" + day;
        }
        if (mon != null && d != null) {
            return year + "-" + mon + "-" + d;
        } else if (mon != null && d == null) {
            return year + "-" + mon + "-" + day;
        } else if (mon == null && d != null) {
            return year + "-" + month + "-" + d;
        }else{
            return year + "-" + month + "-" + day;
        }
    }
}
