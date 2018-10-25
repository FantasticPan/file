package com.pan.file.controller;

import com.pan.file.entity.Files;
import com.pan.file.entity.Member;
import com.pan.file.repository.ConfigurationRepository;
import com.pan.file.repository.FileRepository;
import com.pan.file.repository.MemberRepository;
import com.pan.file.utils.FileUtil;
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
    private FileRepository fileRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;

    /**
     * 带表单参数的文件上传
     */
    @RequestMapping(value = "/formFile", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView formUploadFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        /*
		或者方法头这样写：
		单文件
	    public ModelAndView formUploadFile(@RequestParam(value = "file") MultipartFile multipartFile) {}
		多文件
	    public ModelAndView formUploadFile(@RequestParam(value = "file") MultipartFile[] multipartFile) {}
		*/

        //List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("files");
        //for (int i = 0;i<files.size();i++) {
        //    MultipartFile multipartFile = files.get(i);
        //    String file = multipartFile.getName();
        //}

        //单文件上传
        MultipartFile multipartFile = ((MultipartHttpServletRequest) request).getFile("file");
        //多文件上传
        //MultipartFile multipartFile = multipartRequestgetFiles("file").get(0);

        //获取表单参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //生成Member对象，存入数据库
        Member member = new Member(username, password);

        //返回的是字节，1M=1024KB=1048576字节 1KB=1024Byte
        Long length = multipartFile.getSize();
        String fileName = multipartFile.getOriginalFilename();
        //获取文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
        //获取文件后缀名
        //String prefix = fileName.substring(0,fileName.lastIndexOf("."));

        //指定符合要求的后缀
        //String fileType = ".txt,.docx,.doc";
        String fileType = configurationRepository.findUploadFileType(1);
        //将字符串通过指定符号转变为数组
        //String[] typeArray = fileType.split(",");

        //String information[] = {"文件内容为空 ！", "文件大小限制1M ！", "文件后缀名有误 ！", "提交成功！", "提交失败，请与工作人员联系"};
        String tips = configurationRepository.findTips(1);
        String information[] = tips.split(",");
        ModelAndView mav = new ModelAndView();

        if (multipartFile.isEmpty()) {
            mav.setViewName("message");
            mav.addObject("error", information[0]);
            return mav;
        } else if (length > 1048576) {
            mav.setViewName("message");
            mav.addObject("error", information[1]);
            return mav;
        } else if (!Arrays.asList(fileType.split(",")).contains(suffix)) {
            mav.setViewName("message");
            mav.addObject("error", information[2]);
            return mav;
        }

        Files files = new Files();
        //加上UUID，防止路径重复
        //String filePath = "/file/uploadFiles" + "/" + UUID.randomUUID() + "/";
        //String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
        //String filePath = "d:/uploadFies" + "/" + UUID.randomUUID() + "/";
        String uploadFilePath = configurationRepository.findUploadFilePath(1); //eg：windows:D:/uploadFies Linux:/file/uploadFiles
        String filePath = uploadFilePath + "/" + UUID.randomUUID() + "/";
        String fileUrl = filePath + fileName;
        files.setName(fileName);
        files.setUrl(fileUrl);
        files.setDate(new Timestamp(System.currentTimeMillis()));

        try {
            //调用上传方法
            FileUtil.uploadFile(filePath, fileName, multipartFile);
            //数据库保存文件的存储路径
            fileRepository.save(files);

            memberRepository.save(member);

            mav.setViewName("message");
            mav.addObject("error", information[3]);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();

            mav.setViewName("message");
            mav.addObject("error", information[4]);
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
        //获取文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
        //获取文件后缀名
        //String prefix = fileName.substring(0,fileName.lastIndexOf("."));

        //String fileType = ".txt,.docx,.doc";
        //String[] typeArray = fileType.split(",");
        String fileType = configurationRepository.findUploadFileType(1);

        String tips = configurationRepository.findTips(1);
        //String information[] = {"文件内容为空 ！", "文件大小限制1M ！", "文件后缀名有误 ！", "提交成功！", "提交失败，请与工作人员联系"};
        String information[] = tips.split(",");

        ModelAndView mav = new ModelAndView();

        if (multipartFile.isEmpty()) {
            mav.setViewName("message");
            mav.addObject("error", information[0]);
            return mav;
        } else if (length > 1048576) {
            mav.setViewName("message");
            mav.addObject("error", information[1]);
            return mav;
        } else if (!Arrays.asList(fileType.split(",")).contains(suffix)) {
            mav.setViewName("message");
            mav.addObject("error", information[2]);
            return mav;
        }

        Files files = new Files();
        String uploadFilePath = configurationRepository.findUploadFilePath(1);
        String filePath = uploadFilePath + "/" + UUID.randomUUID() + "/";
        //String filePath = "d:/uploadFies" + "/" + UUID.randomUUID() + "/";
        //String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
        String fileUrl = filePath + fileName;
        files.setName(fileName);
        files.setUrl(fileUrl);
        files.setDate(new Timestamp(System.currentTimeMillis()));

        try {
            FileUtil.uploadFile(filePath, fileName, multipartFile);
            fileRepository.save(files);

            mav.setViewName("message");
            mav.addObject("error", information[3]);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();

            mav.setViewName("message");
            mav.addObject("error", information[4]);
            return mav;
        }
    }

    @RequestMapping(value = "/fileDownload")
    @ResponseBody
    public void downloadFile(HttpServletResponse response) {

        //String fileName = "下学期公式表.doc";
        String fileName = configurationRepository.findDownloadFileName(1);
        //String filePath = "/file/";
        //String filePath = "D:\\file\\";
        //String filePath = "D:/file/";
        String filePath = configurationRepository.findDownLoadFilePath(1);
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

        //String zipName = "file.zip";
        //String outPath = "/file/";
        //String outPath = "D:/file/";
        String outPath = configurationRepository.findDownLoadFilePath(1);
        String zipName = configurationRepository.findDownloadZipFileName(1);
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
