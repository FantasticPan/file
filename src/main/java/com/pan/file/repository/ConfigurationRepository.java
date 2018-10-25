package com.pan.file.repository;

import com.pan.file.entity.FileConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by FantasticPan on 2018/10/23.
 */
public interface ConfigurationRepository extends JpaRepository<FileConfiguration, Long> {

    @Query("select f.downloadFileName from FileConfiguration f where f.id =?1")
    String findDownloadFileName(Integer id);

    @Query("select f.downloadZipFileName from FileConfiguration f where f.id =?1")
    String findDownloadZipFileName(Integer id);

    @Query("select f.uploadFileType from FileConfiguration f where f.id =?1")
    String findUploadFileType(Integer id);

    @Query("select f.tips from FileConfiguration f where f.id =?1")
    String findTips(Integer id);

    @Query("select f.uploadFilePath from FileConfiguration f where f.id =?1")
    String findUploadFilePath(Integer id);

    @Query("select f.downLoadFilePath from FileConfiguration f where f.id =?1")
    String findDownLoadFilePath(Integer id);
}
