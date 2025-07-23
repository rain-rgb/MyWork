package com.trtm.sy.registertable.model.request;

import lombok.Data;

import java.util.List;

@Data
public class submissionReportRequest {


    private String fhr;
    private String jcr;
    private String jlr;
    private String ypid;
    private String checkVersion;
    private List<Report> reportList;

    private ReportFileRequest fileRequest;

}
