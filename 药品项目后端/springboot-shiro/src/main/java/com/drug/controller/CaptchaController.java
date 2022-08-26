package com.drug.controller;


import com.drug.entity.dto.ReturnMessage;
import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/captcha")
public class CaptchaController {

    @Resource
    private Producer producer;

    @GetMapping("/getText")
    public ReturnMessage getText(){
        Map map = new HashMap();
        map.put("text",producer.createText());

        return new ReturnMessage().successWithData(map);
    }


    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response,String text) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        // 生成图片验证码
        BufferedImage image = producer.createImage(text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

}
