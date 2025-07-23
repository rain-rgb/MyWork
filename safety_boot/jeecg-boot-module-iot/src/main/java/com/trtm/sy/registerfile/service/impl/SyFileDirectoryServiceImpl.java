package com.trtm.sy.registerfile.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.api.tool.VersionUtil;
import com.trtm.sy.enumutil.YesOrNotEnum;
import com.trtm.sy.registerfile.exception.SyFileExceptionEnum;
import com.trtm.sy.registerfile.mapper.SyFileDirectoryMapper;
import com.trtm.sy.registerfile.mapper.SyFileDmMapper;
import com.trtm.sy.registerfile.model.entity.SyFile;
import com.trtm.sy.registerfile.model.entity.SyFileDirectory;
import com.trtm.sy.registerfile.model.entity.SyFileDm;
import com.trtm.sy.registerfile.request.SyFileDmRequest;
import com.trtm.sy.registerfile.service.SyFileDirectoryService;
import com.trtm.sy.registerfile.service.SyFileService;
import com.trtm.sy.registermodules.core.page.PageResult;
import com.trtm.sy.registermodules.core.page.factory.PageFactory;
import com.trtm.sy.registermodules.core.page.factory.PageResultFactory;
import org.jeecg.common.exception.JeecgBootException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 文件目录业务实现类
 * @Author Liupei
 * @Date 2023-06-29
 */
@Service
//@DS("#header.dataSourcePoolName")
public class SyFileDirectoryServiceImpl extends ServiceImpl<SyFileDirectoryMapper, SyFileDirectory> implements SyFileDirectoryService {

    @Resource
    private SyFileDmMapper syFileDmMapper;

    @Resource
    private SyFileDirectoryMapper syFileDirectoryMapper;

    @Resource
    private SyFileService syFileService;


    /**
     * 添加文件目录
     */
    @Override
    public void addSyFileDirectory(SyFileDirectory syFileDirectory, String fileManagerPath) {
        checkFileDirectory(syFileDirectory);

        String mlcode = syFileDirectory.getMlcode();
        if (StrUtil.isBlank(mlcode)) {
            throw new JeecgBootException(String.valueOf(SyFileExceptionEnum.ML_CODE_IS_EMPTY));
        }

        if (StrUtil.isBlank(syFileDirectory.getEditFlag())) {
            syFileDirectory.setEditFlag("1");
        }

        syFileDirectory.setDelFlag("N");
        // 保存用户信息
        this.save(syFileDirectory);
    }

    /**
     * 文件目录校验
     * @param syFileDirectory 文件目录
     */
    private void checkFileDirectory(SyFileDirectory syFileDirectory) {
        String fwjmlid = syFileDirectory.getFwjmlid();

        if (StrUtil.isBlank(fwjmlid) || "-1".equals(fwjmlid)) {
            syFileDirectory.setFwjmlid("-1");
            syFileDirectory.setMlChain("-1");
        } else {
            SyFileDirectory fSyFileDirectory = this.getById(syFileDirectory.getFwjmlid());
            syFileDirectory.setMlChain(fSyFileDirectory.getMlChain() + "," + fwjmlid);
        }
    }

    /**
     * 删除文件目录
     */
    @Override
    public void deleteSyFileDirectory(List<String> param) {
        param.forEach(id -> {
            SyFileDirectory byId = this.getById(id);

            if ("0".equals(byId.getEditFlag())) {
                throw new JeecgBootException(String.valueOf(SyFileExceptionEnum.EDIT_FLAG_CHECK));
            }

            List<SyFileDm> syFileDmList = syFileDmMapper.selectList(Wrappers.lambdaQuery(new SyFileDm()).eq(SyFileDm::getWjmlid, id));
            if(CollectionUtil.isNotEmpty(syFileDmList)){
                throw new JeecgBootException("所删除的目录中已有规程,请先删除规程!");
            }

            List<SyFileDirectory> treeList = syFileDirectoryMapper.getTreeList(byId);
            if(CollectionUtil.isNotEmpty(treeList)){
                treeList.forEach(this::removeById);
            }

        });
    }

    /**
     * 修改文件目录
     */
    @Override
    public void editSyFileDirectory(SyFileDirectory syFileDirectory) {
        List<SyFileDirectory> treeList = syFileDirectoryMapper.getTreeList(syFileDirectory);
        // 组装成树结构
        List<SyFileDirectory> tree = constructTree(treeList, syFileDirectory);

        SyFileDirectory byId = this.getById(syFileDirectory.getWjmlid());

        if ("0".equals(byId.getEditFlag())) {
            throw new JeecgBootException(String.valueOf(SyFileExceptionEnum.EDIT_FLAG_CHECK));
        }

        VersionUtil.checkVersion(syFileDirectory.getVersion(), byId.getVersion());

        String fwjmlid = syFileDirectory.getFwjmlid();

        if (StrUtil.isBlank(fwjmlid) || "-1".equals(fwjmlid)) {
            syFileDirectory.setFwjmlid("-1");
            syFileDirectory.setMlChain("-1");
        } else {
            SyFileDirectory fSyFileDirectory = this.getById(syFileDirectory.getFwjmlid());

            if (fwjmlid.equals(syFileDirectory.getWjmlid())) {
                throw new JeecgBootException(String.valueOf(SyFileExceptionEnum.FML_IS_NOT_ITSELF));
            }

            List<SyFileDirectory> collections = treeList.stream().filter(child -> child.getWjmlid().equals(fwjmlid)).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collections)) {
                throw new JeecgBootException(String.valueOf(SyFileExceptionEnum.NOT_MODIFY_FML_TO_CHILD));
            }

            syFileDirectory.setMlChain(fSyFileDirectory.getMlChain() + "," + fwjmlid);
        }

        tree.forEach(this::recursionSyFileDirectory);

        this.updateById(syFileDirectory);
    }

    /**
     * 递归处理子级目录
     * @param f 父级目录
     */
    private void recursionSyFileDirectory(SyFileDirectory f) {
        List<SyFileDirectory> children = f.getChildren();
        if (CollectionUtil.isNotEmpty(children)) {
            children.forEach(c -> {
                recursionSyFileDirectory(c);
                c.setFwjmlid(f.getWjmlid());
                c.setMlChain(f.getMlChain() + "," + f.getWjmlid());
                this.updateById(c);
            });
        }
    }

    /**
     * 获取文件目录
     */
    @Override
    public List<SyFileDirectory> getSyFileDirectory(SyFileDirectory request) {
        List<SyFileDirectory> list =
                this.list(Wrappers.lambdaQuery(new SyFileDirectory())
                        .eq(SyFileDirectory::getDelFlag, YesOrNotEnum.N)
                        .eq(StrUtil.isNotBlank(request.getMlClass()),SyFileDirectory::getMlClass, request.getMlClass())
                        .orderByAsc(SyFileDirectory::getCreateTime));
        return constructTree(list,null);
    }

    /**
     * 获取文件目录类型
     */
    @Override
    public List<SyFileDirectory> getSyFileDirectoryByClass(SyFileDirectory request) {
        List<SyFileDirectory> result = new ArrayList<>();

        List<SyFileDirectory> list =
                this.list(Wrappers.lambdaQuery(new SyFileDirectory())
                        .eq(StrUtil.isNotBlank(request.getWjmlid()), SyFileDirectory::getWjmlid, request.getWjmlid())
                        .eq(StrUtil.isNotBlank(request.getMlClass()), SyFileDirectory::getMlClass, request.getMlClass())
                        .eq(SyFileDirectory::getDelFlag, YesOrNotEnum.N)
                        .orderByAsc(SyFileDirectory::getCreateTime));

        List<SyFileDirectory> rootList = excludeChildren(list);

        // 查询其子集
        rootList.forEach(syFileDirectory -> {

            List<SyFileDirectory> treeList = syFileDirectoryMapper.getTreeList(syFileDirectory);
            // 组装成树结构
            List<SyFileDirectory> tree = constructTree(treeList, syFileDirectory);
            result.addAll(tree);
        });

        return result;
    }

    /**
     * 排除子级
     * @param list 所有目录
     * @return 排除子级后的目录
     */
    public List<SyFileDirectory> excludeChildren(List<SyFileDirectory> list) {
        HashSet<SyFileDirectory> deleteList = new HashSet<>();

        for (SyFileDirectory syFileDirectory : list) {
            String wjmlid = syFileDirectory.getWjmlid();
            for(SyFileDirectory fd : list){
                String[] split = fd.getMlChain().split(",");
                List<String> strings = Arrays.asList(split);
                if(strings.contains(wjmlid)){
                    deleteList.add(fd);
                }
            }
        }
        list.removeAll(deleteList);
        return list;
    }

    /**
     * 构造树形结构
     * @param syFileDirectoryList 所有目录
     * @param root 父级目录
     * @return 改变为树形结构
     */
    public List<SyFileDirectory> constructTree(List<SyFileDirectory> syFileDirectoryList,SyFileDirectory root) {
        try {
            List<SyFileDirectory> rootSyFileDire = new ArrayList<SyFileDirectory>();
            if (ObjectUtil.isNotNull(root)) {
                rootSyFileDire.add(root);
            } else {
                for (SyFileDirectory syFileDirectory : syFileDirectoryList) {
                    if ("-1".equals(syFileDirectory.getFwjmlid())) {
                        rootSyFileDire.add(syFileDirectory);
                    }
                }
            }
            for (SyFileDirectory syFileDirectory : rootSyFileDire) {
                syFileDirectory.setChildren(getChildren(syFileDirectory.getWjmlid(), syFileDirectoryList));
            }
            if (rootSyFileDire.size() == 0) {
                return new ArrayList<>();
            }
            return rootSyFileDire;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 获取子级
     * @param pId 父级目录id
     * @param allSyFileDirectory 所有文件目录
     * @return 获取该父级的所有子级
     */
    public List<SyFileDirectory> getChildren(String pId, List<SyFileDirectory> allSyFileDirectory) {
        List<SyFileDirectory> childList = new ArrayList<>();
        for (SyFileDirectory syFileDirectory : allSyFileDirectory) {
            if (syFileDirectory.getFwjmlid().equals(pId)) {
                childList.add(syFileDirectory);
            }
        }
        for (SyFileDirectory syFileDirectory : childList) {
            syFileDirectory.setChildren(getChildren(syFileDirectory.getWjmlid(), allSyFileDirectory));
        }
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }

    /**
     * 附件、图片上传
     */
    @Override
    public void uploadFile(MultipartFile[] uploadFiles,
                           String wjmlid,
                           String path) throws IOException {

        SyFileDirectory syFileDirectory = this.getById(wjmlid);
        if (ObjectUtil.isNull(syFileDirectory)) {
            throw new JeecgBootException(String.valueOf(SyFileExceptionEnum.WJML_IS_EMPTY));
        }

        StringBuffer fjid = new StringBuffer();
        StringBuffer fileName = new StringBuffer();
        for (MultipartFile uploadFile : uploadFiles) {
            String from = "manager_upload";
            String fileType = "manager_upload";
            Map<String, Object> stringObjectMap = syFileService.uploadFile(uploadFile, from, fileType, path, null, null);
            String id = stringObjectMap.get("id").toString();
            if (StrUtil.isNotBlank(id)) {
                if (StrUtil.isNotBlank(fjid)) {
                    fjid.append(";");
                }
                fjid.append(stringObjectMap.get("id").toString());
            }

            String name = stringObjectMap.get("originalFilename").toString();
            if (StrUtil.isNotBlank(name)) {
                if (StrUtil.isNotBlank(fileName)) {
                    fileName.append(";");
                }
                fileName.append(stringObjectMap.get("originalFilename").toString());
            }
        }
        SyFileDm syFileDm = new SyFileDm();
        syFileDm.setFjid(fjid.toString());
        syFileDm.setWjmlid(syFileDirectory.getWjmlid());
        syFileDm.setBbh(fileName.toString());
        syFileDm.setDelFlag("N");
        syFileDmMapper.insert(syFileDm);
    }

    @Override
    public void uploadFileGc(SyFileDmRequest request) {
        SyFileDm syFileDm = new SyFileDm();
        syFileDm.setFjid(request.getFjid());
        syFileDm.setWjmlid(request.getWjmlid());

        syFileDm.setGcmc(request.getGcmc());
        syFileDm.setBbh(request.getBbh());
        if(StrUtil.isNotBlank(request.getZxrq())){
            syFileDm.setZxrq(request.getZxrq());
        }
        if(StrUtil.isNotBlank(request.getGqrq())){
            syFileDm.setGqrq(request.getGqrq());
        }
        syFileDm.setDelFlag("N");
        syFileDmMapper.insert(syFileDm);
    }

    /**
     * 获取文件目录下的文件
     */
    @Override
    public PageResult<SyFileDm> getSyFileDirectoryDm(SyFileDmRequest request) {
        Page<SyFileDm> syFilePage = syFileDirectoryMapper.getSyFileListByMlid(PageFactory.defaultPage(request), request);

        List<SyFileDm> syFileDmList = syFilePage.getRecords();
        syFileDmList.forEach(syFileDm -> {
            String fjid = syFileDm.getFjid();
            if(StrUtil.isNotBlank(fjid)){
                String[] split = fjid.split(",");

                List<SyFile> syFileList = new ArrayList<>();
                for (String s : split) {
                    SyFile syFile = syFileService.getById(s);
                    if(ObjectUtil.isNotNull(syFile)){
                        syFileList.add(syFile);
                    }
                }
                syFileDm.setSyFileList(syFileList);
            }
        });
        syFilePage.setRecords(syFileDmList);

        return PageResultFactory.createPageResult(syFilePage);
    }

    /**
     * 修改文件目录下的文件详情信息
     */
    @Override
    public void editSyFileByFjId(SyFileDmRequest request) {
        SyFileDm syFileDm = syFileDmMapper.selectById(request);
        VersionUtil.checkVersion(Integer.parseInt(request.getVersion()), syFileDm.getVersion());

        BeanUtil.copyProperties(request, syFileDm);
        syFileDmMapper.updateById(syFileDm);
    }

    /**
     * 删除文件目录下的文件详情信息
     */
    @Override
    public void deleteSyFileDmByFjId(SyFileDmRequest request) {
        SyFileDm syFileDm = syFileDmMapper.selectById(request);
        VersionUtil.checkVersion(Integer.parseInt(request.getVersion()), syFileDm.getVersion());
        syFileDmMapper.delete(Wrappers.lambdaQuery(new SyFileDm()).eq(SyFileDm::getFdmid, request.getFdmid()));
    }
}
