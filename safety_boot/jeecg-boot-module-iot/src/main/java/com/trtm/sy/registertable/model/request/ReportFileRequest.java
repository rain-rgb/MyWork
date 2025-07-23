package com.trtm.sy.registertable.model.request;


import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReportFileRequest {

    private String fileId;

    private List<Map<String, Object>> sbList;

    private String fhr;

    private String tjr;

    private String ypId;
}
