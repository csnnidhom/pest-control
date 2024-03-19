package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, String> {
    Optional<UploadFile> findByName(String fileName);
}
