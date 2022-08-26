package json;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadJson {

    public static void readChinaJson(){
        InputStreamReader isr = null;//使用系统默认的字符集
        BufferedReader reader = null;
        String str = "";
        List<String> list = new ArrayList<>();
        int line = 0;
        try {
            FileInputStream fis = new FileInputStream("E:\\SpringBoot项目\\drug数据\\src\\main\\resources\\drug_guochan_cfda.json");

            // InputStreamReader isr = new InputStreamReader(fis);//使用系统默认的字符集
            //参数2指明了字符集，具体使用哪个字符集，取决于文件dbcp.txt保存时使用的字符集
            isr = new InputStreamReader(fis,"GBK");

            reader = new BufferedReader(isr);


            while((str = reader.readLine() )!=null){
                line+=1;
                list.add(str);
                System.out.println("第"+line+"行"+str);
            }
            writeGuochan(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(isr!=null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public static void readDrugJson(){
        InputStreamReader isr = null;//使用系统默认的字符集
        BufferedReader reader = null;
        String str = "";
        List list = new ArrayList();
        int line = 0;
        try {
            FileInputStream fis = new FileInputStream("E:\\SpringBoot项目\\drug数据\\src\\main\\resources\\drug.json");

            // InputStreamReader isr = new InputStreamReader(fis);//使用系统默认的字符集
            //参数2指明了字符集，具体使用哪个字符集，取决于文件dbcp.txt保存时使用的字符集
            isr = new InputStreamReader(fis,"UTF-8");

            reader = new BufferedReader(isr);
            while((str = reader.readLine() )!=null){
                line+=1;
                list.add(str);
                System.out.println("第"+line+"行"+str);
            }
            writeDrug(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(isr!=null){
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void  writeDrug(List<String> list) throws IOException {
        long start = System.currentTimeMillis();
        // 创建工作簿
        Workbook workbook = new SXSSFWorkbook();
        // 创建表
        Sheet sheet = workbook.createSheet();
        // 写入数据
        int line = 1;

        Row row = sheet.createRow(0);
        List<String> head = new ArrayList();
//        head.add("class");//0
        head.add("drugname");//1
        head.add("ingredients");//成分2
        head.add("class");//分类3
        head.add("OTC");//OTC类型4
        head.add("specifications");//规格5
        head.add("drugtype");//药品类型6
        head.add("indication");//适应症7
        head.add("usageanddosage");//用法用量8
        head.add("storage");//贮藏9
        head.add("adversereaction");//不良反应10
        head.add("taboo");//禁忌11
        head.add("drugregulatoryclassification");//药品监管分级12
        head.add("functionalindications");//功能主治13
        head.add("character");//性状14
        for (int i = 0; i < 14; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(head.get(i));
        }



        for (String str : list) {
            JSONObject jsonObject = JSON.parseObject(str);
            //获取json对象中的信息
            
            row = sheet.createRow(line);
//            String clazz = jsonObject.getString("class");
            String drugname = jsonObject.getString("中心词");
            String cf = jsonObject.getString("成份");
            String fl = jsonObject.getString("分类");
            String otc = jsonObject.getString("OTC类型");
            String gg = jsonObject.getString("规格");
            String yplx = jsonObject.getString("药品类型");
            String syz = jsonObject.getString("适应症");
            String yfyl = jsonObject.getString("用法用量");
            String zc = jsonObject.getString("贮藏");
            String blfy = jsonObject.getString("不良反应");
            String jj = jsonObject.getString("禁忌");
            String jpjgfj = jsonObject.getString("药品监管分级");
            String gnzz = jsonObject.getString("功能主治");
            String xz = jsonObject.getString("性状");

            for (int i = 0; i < 14; i++) {
//                if(clazz!=null){
//                    Cell cell = row.createCell(0);
//                    cell.setCellValue(clazz);
//                }
                if(drugname!=null){
                    Cell cell = row.createCell(0);
                    cell.setCellValue(drugname);
                }
                if(cf!=null){
                    Cell cell = row.createCell(1);
                    cell.setCellValue(cf);
                }
                if(fl!=null){
                    Cell cell = row.createCell(2);
                    cell.setCellValue(fl);
                }
                if(otc!=null){
                    Cell cell = row.createCell(3);
                    cell.setCellValue(otc);
                }
                if(gg!=null){
                    Cell cell = row.createCell(4);
                    cell.setCellValue(gg);
                }
                if(yplx!=null){
                    Cell cell = row.createCell(5);
                    cell.setCellValue(yplx);
                }
                if(syz!=null){
                    Cell cell = row.createCell(6);
                    cell.setCellValue(syz);
                }
                if(yfyl!=null){
                    Cell cell = row.createCell(7);
                    cell.setCellValue(yfyl);
                }
                if(zc!=null){
                    Cell cell = row.createCell(8);
                    cell.setCellValue(zc);
                }
                if(blfy!=null){
                    Cell cell = row.createCell(9);
                    cell.setCellValue(blfy);
                }
                if(jj!=null){
                    Cell cell = row.createCell(10);
                    cell.setCellValue(jj);
                }
                if(jpjgfj!=null){
                    Cell cell = row.createCell(11);
                    cell.setCellValue(jpjgfj);
                }
                if(gnzz!=null){
                    Cell cell = row.createCell(12);
                    cell.setCellValue(gnzz);
                }
                if(xz!=null){
                    Cell cell = row.createCell(13);
                    cell.setCellValue(xz);
                }
            }
            line++;
        }
        System.out.println("over");
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\SpringBoot项目\\drug数据\\drugDetails.xlsx");
        workbook.write(fileOutputStream);
        // 清除临时文件
        ((SXSSFWorkbook) workbook).dispose();
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        System.out.println("共花费了："+(end-start)/1000+"s");
    }













    public static void  writeGuochan(List<String> list) throws IOException {
        long start = System.currentTimeMillis();
        // 创建工作簿
        Workbook workbook = new SXSSFWorkbook();
        // 创建表
        Sheet sheet = workbook.createSheet();
        // 写入数据
        int line = 1;

        Row row = sheet.createRow(0);
        List<String> head = new ArrayList();
        head.add("drugname");
        head.add("isbn");
        head.add("company");
        head.add("code");
        for (int i = 0; i < 4; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(head.get(i));
        }



        for (String str : list) {
            List<String> strings = strGuochan(str);
            row = sheet.createRow(line);
            for (int i = 0; i < strings.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(strings.get(i));
            }
            line++;
        }
        System.out.println("over");
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\SpringBoot项目\\drug数据\\drug.xlsx");
        workbook.write(fileOutputStream);
        // 清除临时文件
        ((SXSSFWorkbook) workbook).dispose();
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        System.out.println("共花费了："+(end-start)/1000+"s");
    }

    public static List<String> strGuochan(String str){
        List<String> list = new ArrayList<>();

        String[] split = str.split(":");

        for (int i = 1; i < split.length; i++) {
                if(split[i].indexOf("中心词")==-1 ||
                        split[i].indexOf("分类")==-1 ||
                        split[i].indexOf("成份")==-1 ||
                        split[i].indexOf("适应症")==-1 ||
                        split[i].indexOf("OTC类型")==-1 ||
                        split[i].indexOf("company")==-1 ||
                        split[i].indexOf("standard_code")==-1
                ) {
                    list.add(split[i]);
                }
        }

        return list;
    }

    public static void main(String[] args) {
//        readChinaJson();
        readDrugJson();
    }

}
