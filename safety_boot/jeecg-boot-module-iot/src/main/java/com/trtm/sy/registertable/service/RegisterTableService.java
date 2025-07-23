package com.trtm.sy.registertable.service;

import com.trtm.sy.registertable.model.RegisterTableRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegisterTableService {
    void handleFileUpload(MultipartFile file, RegisterTableRequest tableRequest, String tableNum);
//
//    void spiltFile(MultipartFile file, RegisterTableRequest tableRequest);
//
//    void updateTableFormPattern(String tableNum, List<RegisterTableRequest> requestList);
//
//    void saveFormList(List<List<BaseForm>> request, String tableNum);
//
//    void uploadWord(MultipartFile file);
}
