package com.pan.file.repository;

import com.pan.file.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 李攀 on 2017/12/5.
 */
public interface FileRepository extends JpaRepository<Files,Long> {
}
