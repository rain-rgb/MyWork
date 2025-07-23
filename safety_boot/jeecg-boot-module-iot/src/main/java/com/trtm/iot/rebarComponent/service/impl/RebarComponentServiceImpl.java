package com.trtm.iot.rebarComponent.service.impl;

import com.trtm.iot.rebarComponent.entity.RebarComponent;
import com.trtm.iot.rebarComponent.mapper.RebarComponentMapper;
import com.trtm.iot.rebarComponent.service.IRebarComponentService;
import com.trtm.iot.rebarComponent.entity.ComponentVo;
import com.trtm.iot.rebarComponent.vo.MaterialVo;
import com.trtm.iot.rebarComponentMaterial.mapper.RebarComponentMaterialMapper;
import com.trtm.iot.rebarComponentMaterial.service.IRebarComponentMaterialService;

import com.trtm.iot.rebarComponentTask.mapper.RebarComponentTaskMapper;
import com.trtm.iot.rebarComponentTask.service.IRebarComponentTaskService;
import com.trtm.iot.rebarTaskChecklist.entity.RebarTaskChecklist;
import com.trtm.iot.rebarTaskChecklist.mapper.RebarTaskChecklistMapper;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Description: rebar_component
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Service
public class RebarComponentServiceImpl extends ServiceImpl<RebarComponentMapper, RebarComponent> implements IRebarComponentService {
    @Autowired
    private RebarComponentMapper rebarComponentMapper;
    @Autowired
    private RebarComponentTaskMapper rebarComponentTaskMapper;
    @Autowired
    private RebarComponentMaterialMapper rebarComponentMaterialMapper;
    @Autowired
    private RebarTaskChecklistMapper rebarTaskChecklistMapper;

    @Override
    public List<ComponentVo> queryComponentByTaskId(String taskId) {
        List<ComponentVo> list = new ArrayList<>();
        RebarTaskChecklist rebarTaskChecklist = rebarTaskChecklistMapper.selectByTaskId(taskId);

        List<RebarComponent> rebarComponents = rebarComponentMapper.queryComponentByTaskId(
                taskId,
                rebarTaskChecklist.getOrgCode(),
                rebarTaskChecklist.getOrgCodes());

        if (rebarComponents!=null){
            for (RebarComponent rebarComponent : rebarComponents) {
                ComponentVo componentVo = new ComponentVo();
                componentVo.setComponentName(rebarComponent.getComponentName());
                componentVo.setComponentId(rebarComponent.getComponentId());
                componentVo.setComponentModel(rebarComponent.getComponentModel());
                componentVo.setRemark(rebarComponent.getRemark());
                componentVo.setOrgCode(rebarComponent.getOrgCode());
                componentVo.setOrgCodes(rebarComponent.getOrgCodes());
                String number = rebarComponentTaskMapper.queryComponentNumber(rebarComponent.getComponentId(),taskId);
                componentVo.setComponentNumber(number);
                list.add(componentVo);
            }
        }

        return list;
    }

    @Override
    @Transactional
    public boolean deletedById(String id) {
        RebarComponent rebarComponent = rebarComponentMapper.selectById(id);
        boolean deleteById = rebarComponentMapper.isDeleteById(id);
        if (rebarComponent!=null){
            //同时删除和构件对应的材料
            rebarComponentMaterialMapper.deleteByComponentId(rebarComponent.getComponentId());
        }
        return deleteById;
    }

    @Override
    public boolean addRebarComponent(RebarComponent rebarComponent) {
        //编号不能重复
        String componentId = rebarComponent.getComponentId();
        List<RebarComponent> is =rebarComponentMapper.queryComponentByComponentId(componentId);
        if (is.size()>0){
            return false;
        }
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        rebarComponent.setIsDeleted(0);
        rebarComponent.setStatus(0);
        rebarComponent.setCreatePerson(loginUser.getRealname());
        if (rebarComponent.getImage()==null){
            rebarComponent.setImage("https://safety-boots.oss-cn-beijing.aliyuncs.com/temp/nopic_1734331588609.png");
        }
        rebarComponentMapper.insert(rebarComponent);
        return true;
    }

    @Override
    public List<RebarComponent> queryComponentPageList(String sys_depart_orgcode, String componentName,String startDate,String endDate,String sys_depart_orgcodes,Integer status) {
        if (sys_depart_orgcodes.equals("1")){
            return rebarComponentMapper.queryComponentPageList1(sys_depart_orgcode,componentName,startDate,endDate,sys_depart_orgcodes,status);
        }else {
            return rebarComponentMapper.queryComponentPageList(sys_depart_orgcode,componentName,startDate,endDate,sys_depart_orgcodes,status);
        }
    }

    @Override
    public RebarComponent getBycomponentId(String componentId,String orgCodes) {
        return rebarComponentMapper.getBycomponentId(componentId,orgCodes);
    }


    @Autowired
    private IRebarComponentService rebarComponentService;
    @Autowired
    private IWzcailiaonamedictManService wzcailiaonamedictManService;
    @Autowired
    private IRebarComponentTaskService rebarComponentTaskService;

    @Override
    public  List<ComponentVo> getComponentList( ComponentVo componentVo,Integer pageNo, Integer pageSize,String sys_depart_orgcode,String sys_depart_orgcodes){
        List<RebarComponent> rebarComponents = rebarComponentService.queryComponentPageList(sys_depart_orgcode,
                componentVo.getComponentName(),
                componentVo.getStartDate(),
                componentVo.getEndDate(),
                sys_depart_orgcodes,
                componentVo.getStatus()
        );
        if (rebarComponents == null) {
            return null;
        }
        List<ComponentVo> componentVos = new ArrayList<>();
        for (RebarComponent rebarComponent : rebarComponents) {
            ComponentVo componentVo1 = new ComponentVo();
            BeanUtils.copyProperties(rebarComponent,componentVo1);

            String componentId = componentVo1.getComponentId();
            String orgCodes = componentVo1.getOrgCodes();
            //从任务列表查构件数量
            String componentNumber= rebarComponentTaskService.getComponentNumberBycomponentId(componentId,orgCodes);
            if (componentNumber!=null){
                componentVo1.setComponentNumber(componentNumber);
                //构件总重量
                componentVo1.setTotalWeight(Double.parseDouble(componentNumber)*componentVo1.getWeight());
                //通过构件id查出构件所对应的所有材料
                List<MaterialVo> materialVos = wzcailiaonamedictManService.queryMaterialByComponentId(componentId);
                Double weight = 0.0;
                for (MaterialVo materialVo : materialVos) {
                    String materialNumber = materialVo.getMaterialNumber();
                    Double weight1 = materialVo.getWeight();
                    if (weight1!=null){
                        weight+=Double.parseDouble(materialNumber)*weight1*Double.parseDouble(componentNumber);
                    }
                }
                //原料消耗总重量
                componentVo1.setMaterialTotalWeight(weight);
            }
            componentVos.add(componentVo1);
        }
        List<ComponentVo> e = componentVos.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        return e;
    }

    @Override
    public Integer editDepartComponent(List<String> ids, String orgCodes) {
        return rebarComponentMapper.editDepartComponent(ids,orgCodes);
    }

    @Override
    public boolean addTaskCheck(ComponentVo componentVo) {

        String orgCodes = componentVo.getOrgCodes();

        List<RebarComponent> rebarComponents = componentVo.getRebarComponents();
        for (RebarComponent rebarComponent : rebarComponents) {
            rebarComponent.setId(UUID.randomUUID().toString().replace("-", ""));
            rebarComponent.setOrgCodes(orgCodes);
            String orgCodes1 = rebarComponent.getOrgCodes();
            String componentId = rebarComponent.getComponentId();
            RebarComponent rebarComponent1 = rebarComponentMapper.queryComponentByComponentIdAndOrgCodes(componentId,orgCodes1);
            //添加前查询是否已经存在
            if (rebarComponent1!=null){
                rebarComponent.setComponentNumber(rebarComponent1.getComponentNumber()+1);
                rebarComponentMapper.updateByComponentId(rebarComponent.getComponentId(),rebarComponent.getComponentNumber(),rebarComponent.getOrgCodes());
            }else {
                rebarComponent.setComponentNumber(1);
                rebarComponentMapper.insert(rebarComponent);
            }

        }

        return true;
    }

    @Override
    public boolean editComponentByTaskIdAndComponentId(String taskId, String componentId) {
        boolean edited = rebarComponentMapper.editComponentByTaskIdAndComponentId(taskId,componentId);
        return edited;
    }


}
