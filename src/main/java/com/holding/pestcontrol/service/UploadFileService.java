package com.holding.pestcontrol.service;

import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.UploadFile;
import com.holding.pestcontrol.repository.ClientRepository;
import com.holding.pestcontrol.repository.UploadFileRepository;
import com.holding.pestcontrol.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;
    private final ClientRepository clientRepository;
    private static long MAX_FILE_SIZE = 2048000; //2Mb

    public boolean isValidFileSize(MultipartFile file){
        return file.getSize() <= MAX_FILE_SIZE;
    }
    public String uploadFile(String companyName, MultipartFile file) throws IOException {
        Client client = (Client) clientRepository.findByNamaPerusahaan(companyName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client Not Found "));

        if (!isValidFileSize(file)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum file size is 2 MB");
        }

        if (!file.getContentType().equals("application/pdf")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file must be in PDF format");
        }

        if (file.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Select files to upload");
        }
        UploadFile result = uploadFileRepository.save(UploadFile.builder()
                        .id(UUID.randomUUID().toString())
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .fileData(FileUtils.compressFile(file.getBytes()))
                        .client(client)
                        .build());

        if (result != null){
            return "Success upload " + file.getOriginalFilename();
        }
        return "Failed upload file";
    }

    public byte[] downloadFile(String fileName){
        Optional<UploadFile> uploadFile = Optional.ofNullable(uploadFileRepository.findByName(fileName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found")));

        byte[] fileInBytes = FileUtils.decompressFile(uploadFile.get().getFileData());
        return fileInBytes;
    }
}
