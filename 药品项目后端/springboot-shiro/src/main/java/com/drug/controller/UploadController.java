package com.drug.controller;

import com.drug.entity.dto.ReturnMessage;
import com.drug.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class UploadController {

    @Value("${file.staticPath}")
    private String staticPath;

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping("/upload/file")
    public ReturnMessage upload(@RequestParam("avatar") MultipartFile multipartFile,@RequestParam("userid")Integer userid) throws FileNotFoundException {

        Map map = new HashMap();
        if (multipartFile.isEmpty()){
            return ReturnMessage.failWithMsg("文件有误！");
        }

//        String getpath = ResourceUtils.getURL("classpath:\\").getPath().replace("%20", " ").replace('/', '\\');

//        String getpath = this.getClass().getClassLoader().getResource("static").getFile();
//        try {
//            getpath  = java.net.URLDecoder.decode(getpath,"utf-8");
//            System.out.println(getpath);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        File path = new File(getpath);



        //指定上传目录
        String suffix = "E:\\平常事宜\\毕业论文的材料\\avatar\\";
        File targetFile = new File(suffix);


        String fileName = UUID.randomUUID()+"-id="+userid+".png";

        try {
            if(!targetFile.exists())targetFile.mkdirs();

            File targetFileName = new File(targetFile,fileName);

            if (targetFileName.exists())targetFileName.delete();

            System.out.println(fileName);

            multipartFile.transferTo(targetFileName);


            String imgUrl = staticPath+"/img/"+fileName;


            userService.insertUserAvatar(userid,imgUrl);

            System.out.println(imgUrl);

            map.put("avatar",imgUrl);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReturnMessage.successWithData(map);
    }
}
