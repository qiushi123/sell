package com.qcl.uploadfile;

import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.shuidaxia.bean.ShuiUser;
import com.qcl.shuidaxia.user.ShuiUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * ========================
 * 文件上传
 * ========================
 */
@Controller
@Slf4j
public class UploadController {

    @Value("${spring.profiles.active}")
    private String active;//当前是线上环境还是测试环境

    @Autowired
    private ShuiUserService service;//水大侠用户的服务

    private String fileName;

    /**
     * 提取上传方法为公共方法
     *
     * @param uploadDir 上传文件目录
     * @param file      上传对象
     * @throws Exception
     */
    private void executeUpload(String uploadDir, MultipartFile file, String name) throws Exception {
        //文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //上传文件名
        //        String filename = UUID.randomUUID() + suffix;
        fileName = name + suffix;
        //服务器端保存的文件对象
        File serverFile = new File(uploadDir + fileName);
        //将上传的文件写入到服务器端文件内
        file.transferTo(serverFile);
    }

    /**
     * 上传文件方法
     *
     * @param file 前台上传的文件对象
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String upload(@RequestParam("file") MultipartFile file,
                  @RequestParam("userId") String userId) {
        try {
            //上传目录地址
            //            String uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload/";
            String uploadDir;
            if ("test".equals(active)) {//测试环境
                uploadDir = "/Users/qcl/upload/";
            } else {//线上环境的图片存储目录
                uploadDir = "//usr/share/nginx/html/upload/";
            }

            //如果目录不存在，自动创建文件夹
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            log.error("uploadDir:" + uploadDir);
            //调用上传方法
            executeUpload(uploadDir, file, userId);

        } catch (Exception e) {
            //打印错误堆栈信息
            e.printStackTrace();
            return "上传失败";
        }
        //上传成功以后把对应的url存储到用户数据库中
        ShuiUser shuiUser = service.findByUserId(Long.parseLong(userId));
        if (shuiUser == null) {
            throw new SellException(ResultEnum.USER_NO);
        }
        String url;
        if ("test".equals(active)) {//测试环境
            url = "/Users/qcl/upload/" + fileName;
        } else {//线上环境的图片存储目录
            url = "http://30paotui.com:9000/upload/" + fileName;
        }
        shuiUser.setYajinImg(url);
        service.create(shuiUser);
        return "上传成功";
    }

    /**
     * 上传多个文件
     *
     * @param request 请求对象
     * @param file    上传文件集合
     * @return
     */
    @RequestMapping(value = "/uploads", method = RequestMethod.POST)
    public @ResponseBody
    String uploads(HttpServletRequest request, MultipartFile[] file) {
        try {
            //上传目录地址
            String uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload/";
            //如果目录不存在，自动创建文件夹
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            //遍历文件数组执行上传
            for (int i = 0; i < file.length; i++) {
                if (file[i] != null) {
                    //调用上传方法
                    executeUpload(uploadDir, file[i], "文件名");
                }
            }
        } catch (Exception e) {
            //打印错误堆栈信息
            e.printStackTrace();
            return "上传失败";
        }
        return "上传成功";
    }
}
