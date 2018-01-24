package com.fantasticpan.file.controller;

import com.fantasticpan.file.entity.Files;
import com.fantasticpan.file.entity.Member;
import com.fantasticpan.file.repository.FileRepository;
import com.fantasticpan.file.repository.MemberRepository;
import com.fantasticpan.file.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by 李攀 on 2017/12/5.
 */
@Controller
public class FileController {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    MemberRepository memberRepository;

    /**
     * 带表单参数的文件上传
     */
    @RequestMapping(value = "/formfile", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView formUploadFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("files");
        //for (int i = 0;i<files.size();i++) {
        //    MultipartFile multipartFile = files.get(i);
        //    String file = multipartFile.getName();
        //}

        MultipartFile multipartFile = ((MultipartHttpServletRequest) request).getFile("file");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Member member = new Member(username, password);

        Long length = multipartFile.getSize();//返回的是字节，1M=1024KB=1048576字节 1KB=1024Byte
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();//文件后缀名
        //String prefix = fileName.substring(0,fileName.lastIndexOf("."));//文件后缀名

        String fileType = ".txt,.docx,.doc";
        //String[] typeArray = fileType.split(",");

        String error[] = {"文件内容为空 ！", "文件大小限制1M ！", "文件后缀名有误 ！", "提交成功！", "提交失败，请与工作人员联系"};

        ModelAndView mav = new ModelAndView();

        if (multipartFile.isEmpty()) {
            mav.setViewName("message");
            mav.addObject("error", error[0]);
            return mav;
        } else if (length > 1048576) {
            mav.setViewName("message");
            mav.addObject("error", error[1]);
            return mav;
        } else if (!Arrays.asList(fileType.split(",")).contains(suffix)) {
            mav.setViewName("message");
            mav.addObject("error", error[2]);
            return mav;
        }

        Files files = new Files();
        //String filePath = "/file/UploadFiles" + "/" + UUID.randomUUID() + "/";
        //String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
        String filePath = "F:\\文件";
        String fileUrl = filePath + fileName;
        files.setName(fileName);
        files.setUrl(fileUrl);
        files.setDate(new Timestamp(System.currentTimeMillis()));

        try {
            FileUtil.uploadFile(filePath, fileName, multipartFile);
            fileRepository.save(files);
            memberRepository.save(member);

            mav.setViewName("message");
            mav.addObject("error", error[3]);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();

            mav.setViewName("message");
            mav.addObject("error", error[4]);
            return mav;
        }
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile multipartFile) throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        Long length = multipartFile.getSize();//返回的是字节，1M=1024KB=1048576字节 1KB=1024Byte
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();//文件后缀名
        //String prefix = fileName.substring(0,fileName.lastIndexOf("."));//文件后缀名

        String fileType = ".txt,.docx,.doc";
        //String[] typeArray = fileType.split(",");

        String error[] = {"文件内容为空 ！", "文件大小限制1M ！", "文件后缀名有误 ！", "提交成功！", "提交失败，请与工作人员联系"};

        ModelAndView mav = new ModelAndView();

        if (multipartFile.isEmpty()) {
            mav.setViewName("message");
            mav.addObject("error", error[0]);
            return mav;
        } else if (length > 1048576) {
            mav.setViewName("message");
            mav.addObject("error", error[1]);
            return mav;
        } else if (!Arrays.asList(fileType.split(",")).contains(suffix)) {
            mav.setViewName("message");
            mav.addObject("error", error[2]);
            return mav;
        }

        Files files = new Files();
        String filePath = "/file/UploadFiles" + "/" + UUID.randomUUID() + "/";
        //String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
        String fileUrl = filePath + fileName;
        files.setName(fileName);
        files.setUrl(fileUrl);
        files.setDate(new Timestamp(System.currentTimeMillis()));

        try {
            FileUtil.uploadFile(filePath, fileName, multipartFile);
            fileRepository.save(files);

            mav.setViewName("message");
            mav.addObject("error", error[3]);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();

            mav.setViewName("message");
            mav.addObject("error", error[4]);
            return mav;
        }
    }

    @RequestMapping(value = "/fileDownload")
    @ResponseBody
    public void downloadFile(HttpServletResponse response) {

        String fileName = "下学期公式表.doc";
        String filePath = "/file/";
        File file = new File(filePath, fileName);

        try {
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            OutputStream output = response.getOutputStream();

            FileUtil.downloadFile(file, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/zipDownload")
    @ResponseBody
    public void downloadZip(HttpServletResponse response) {

        List<Files> filesList = fileRepository.findAll();

        String zipName = "file.zip";
        String outPath = "/file/";
        File zipPath = new File(outPath, zipName);//使用IO的File根据路径获取文件

        try {
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipName, "UTF-8"));//解决中文名乱码
            OutputStream output = response.getOutputStream();//得到服务器的输入流

            FileUtil.zipFile(zipPath, filesList);
            FileUtil.downloadFile(zipPath, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
