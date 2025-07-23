package org.jeecg.modules.job.sljbz;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonPropertyOrder(alphabetic = true)
public class ProjectPreviewVo implements Serializable {
    private String projectId;
    private String projectName;
    private Integer designPileNum;
    private String constructionUnit;
    private String supervisorUnit;
    private String contractSection;
    private String wayLeve;
    private Integer finishPileNum;
    private Double finishProjectLong;

}
