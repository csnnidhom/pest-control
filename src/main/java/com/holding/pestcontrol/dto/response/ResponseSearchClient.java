package com.holding.pestcontrol.dto.response;

import com.holding.pestcontrol.entity.Client;
import com.holding.pestcontrol.entity.UploadFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ResponseSearchClient{

    private Client client;

    private Optional<Object> fileNames;

    // Constructor
    public ResponseSearchClient(Client client, Optional<UploadFile> uploadFiles) {
        this.client = client;
        this.fileNames = uploadFiles.map(UploadFile::getName);
    }

}
