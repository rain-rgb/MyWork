package com.trtm.sy.registerfile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.registerfile.model.entity.SyFile;
import com.trtm.sy.registerfile.model.entity.SyFileDirectory;
import com.trtm.sy.registerfile.model.entity.SyFileDm;
import com.trtm.sy.registerfile.request.SyFileDmRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface SyFileDirectoryMapper extends BaseMapper<SyFileDirectory> {

    /**
     * 通过目录id获取该目录下所有的文件
     */
    Page<SyFileDm> getSyFileListByMlid(@Param("page") Page<SyFile> page, @Param("request") SyFileDmRequest request);

    /**
     * 获取树结构文件目录
     */
    List<SyFileDirectory> getTreeList(@Param("request") SyFileDirectory request);

}
