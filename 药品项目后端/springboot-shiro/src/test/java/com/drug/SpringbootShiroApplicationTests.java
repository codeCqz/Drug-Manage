package com.drug;

import com.drug.entity.pojo.Drug;
import com.drug.entity.pojo.DrugDetails;
import com.drug.service.DrugService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class SpringbootShiroApplicationTests {

    @Autowired
    DrugService drugService;

    @Test
    void text() throws IOException {
        String path = "drug.xlsx";
        // 获取文件流
        FileInputStream in = new FileInputStream(path);
        // 读取到工作簿
        Workbook workbook = new XSSFWorkbook(in);
        // 根据索引得到工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 获取行
        Row row = sheet.getRow(0);

        Cell cell = row.getCell(0);
        // 获取单元格的值
        // getStringCellValue：获取字符串
        // getNumericCellValue：获取数值
        // 读取值的时候一定要注意类型
        cell.setCellType(CellType.STRING);
        String str = cell.getStringCellValue();
        System.out.println(str);
        in.close();
    }



    @Test
    void drugdetail() throws IOException {
        String path = "E:\\SpringBoot项目\\springboot-shiro\\src\\main\\resources\\drugDetails.xlsx";
        // 获取文件流
        FileInputStream in = new FileInputStream(path);
        // 读取到工作簿
        Workbook workbook = new XSSFWorkbook(in);
        // 根据索引得到工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 获取行
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            DrugDetails drugDetails = new DrugDetails();
            String drugid = "";

            for (int j = 0; j < row.getLastCellNum(); j++) {

                // 获取单元格
                Cell cell = row.getCell(j,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                // 获取单元格的值
                // 读取值的时候一定要注意类型
                cell.setCellType(CellType.STRING);
                String str = cell.getStringCellValue();
                str=str.trim();
                if(j==0){
                    List<Drug> druglist = drugService.getDrugByDrugname(str);
                    if (druglist.size()>0){

                            Drug drug = druglist.get(0);
                            drugDetails.setDrugid(drug.getDrugid());
                        }else{
                        break;
                    }
                    drugDetails.setDrugname(str);
                }if(j==1){
                    drugDetails.setIngredients(str);
                }if(j==2){
                    drugDetails.setDrugclass(str);
                }if(j==3){
                    drugDetails.setOtc(str);
                }if(j==4){
                    drugDetails.setSpecifications(str);
                }if(j==5){
                    drugDetails.setDrugtype(str);
                }if(j==6){
                    drugDetails.setIndication(str);
                }if(j==7){
                    drugDetails.setUsageanddosage(str);
                }if(j==8){
                    drugDetails.setStorage(str);
                }if(j==9){
                    drugDetails.setAdversereaction(str);
                }if(j==10){
                    drugDetails.setTaboo(str);
                }if(j==11){
                    drugDetails.setDrugregulatoryclassification(str);
                }if(j==12){
                    drugDetails.setFunctionalindications(str);
                }
                if(j==13){
                    drugDetails.setCharacter(str);
                }
            }
//          写入数据库
            if(drugDetails.getDrugid()!=null){
                drugService.insertDrugDetails(drugDetails);
            }else{
                continue;
            }

        }
        in.close();
    }



    @Test
    void deal(){
        for (int i = 1; i <= 209048; i++) {
            if(drugService.getDrugByDrugID(i)!=null){
                Drug drug =drugService.getDrugByDrugID(i);
                if(drugService.getDrugDetailsByDrugname(drug.getDrugname().trim())!=null){
                    continue;
                }else{
                    drugService.deleteDrug(i);
                }
            }


        }
    }
    @Test
    void testDate(){
        Date date = new Date();
        date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map map = new HashMap();
        String deDate = deDate = sdf.format(date);
        for (int i = 0; i < 7; i++) {
            String[] strings = deDate.split("-");
            System.out.println(deDate);
            Calendar cl = setCalendar(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
            Calendar beforeDay = getBeforeDay(cl);
            deDate = printCalendar(beforeDay);
        }
    }
    public  Calendar setCalendar(int year,int month,int date){
        Calendar cl = Calendar.getInstance();
        cl.set(year, month-1, date);
        return cl;
    }

    /**
     * 获取当前时间的前一天时间
     * @param cl
     * @return
     */
    private Calendar getBeforeDay(Calendar cl){
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
    @Test
    void test(){
        Map map = new HashMap();
        map.put("111","222");

        System.out.println(map);
    }
}
