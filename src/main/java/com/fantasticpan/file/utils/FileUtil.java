package com.fantasticpan.file.utils;

import com.fantasticpan.file.entity.Files;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 李攀 on 2017/12/5.
 */
public class FileUtil {

    /**
     * 上传文件
     */
    public static void uploadFile(String filePath, String fileName, MultipartFile multipartFile) throws Exception {

        File file = new File(filePath + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        multipartFile.transferTo(file);
    }

    /**
     * 下载文件
     */
    public static void downloadFile(File file, OutputStream output) {

        FileInputStream fileInput = null;
        BufferedInputStream inputStream = null;
        try {
            fileInput = new FileInputStream(file);
            inputStream = new BufferedInputStream(fileInput);
            byte[] buffer = new byte[8192];//1024*8
            int i;
            while ((i = inputStream.read(buffer)) != -1) {
                output.write(buffer,0,i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (fileInput != null)
                    fileInput.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩文件
     */
    public static void zipFile(File zipPath, List<Files> filesList) {

        FileOutputStream fileOutput;
        ZipOutputStream zipOutput;
        BufferedOutputStream bufferedOutput;
        FileInputStream fileInput = null;
        BufferedInputStream bufferedInput = null;
        try {
            fileOutput = new FileOutputStream(zipPath);//输出流，压缩包所在路径
            bufferedOutput = new BufferedOutputStream(fileOutput);
            zipOutput = new ZipOutputStream(bufferedOutput);

            for (int i = 0;i<filesList.size();i++) {
                Files files = filesList.get(i);
                File filePath = new File(files.getUrl());//待压缩文件路径
                //压缩条目
                ZipEntry entry = new ZipEntry(i+"."+filePath.getName());
                //读取待压缩的文件并写进压缩包里
                fileInput = new FileInputStream(filePath);
                bufferedInput = new BufferedInputStream(fileInput);
                zipOutput.putNextEntry(entry);
                byte[] buffer = new byte[8192];//官方API文档推荐大小8192
                int num;
                while ((num = bufferedInput.read(buffer)) != -1) {
                    zipOutput.write(buffer,0,num);
                }
                //不能写成  int i = bufferedInput.read(buffer);while(i != -1);否则形成死循环，一直写入
            }

            zipOutput.closeEntry();
            fileInput.close();
            bufferedInput.close();
            zipOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除生成得压缩包
     */
    public void deleteZip(File file) {
        file.delete();
    }
}
