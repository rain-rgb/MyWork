package org.jeecg.modules.system.util;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.DepartIdprojectModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.model.SysDepartTreeprojectModel;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 * 对应部门的表,处理并查找树级数据
 * <P>
 *
 * @Author: Steve
 * @Date: 2019-01-22
 */
@Component
public class FindsDepartsChildrenUtil {
    @Autowired
    private static ISysDepartprojectService sysDepartprojectService;
    @Autowired
    private  ISysDepartprojectService sysDepartprojectServices;
    @PostConstruct
    public void init() {
        sysDepartprojectService = sysDepartprojectServices;
    }
	//部门树信息-树结构
	//private static List<SysDepartTreeModel> sysDepartTreeList = new ArrayList<SysDepartTreeModel>();

	//部门树id-树结构
    //private static List<DepartIdModel> idList = new ArrayList<>();


    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合
     */
    public static List<SysDepartTreeModel> wrapTreeDataToTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
    	List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        List<SysDepartTreeModel> tree = findChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * 获取 DepartIdModel
     * @param recordList
     * @return
     */
    public static List<DepartIdModel> wrapTreeDataToDepartIdTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdModel> idList = new ArrayList<DepartIdModel>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepart depart = recordList.get(i);
            records.add(new SysDepartTreeModel(depart));
        }
        findChildren(records, idList);
        return idList;
    }
    public static List<DepartIdprojectModel> wrapTreeDataToDepartIdTreeLists(List<SysDepartproject> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdprojectModel> idList = new ArrayList<DepartIdprojectModel>();
        List<SysDepartTreeprojectModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepartproject depart = recordList.get(i);
            records.add(new SysDepartTreeprojectModel(depart));
        }
        findChildrens(records, idList);
        return idList;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<SysDepartTreeModel> findChildren(List<SysDepartTreeModel> recordList,
                                                         List<DepartIdModel> departIdList) {

        List<SysDepartTreeModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepartTreeModel branch = recordList.get(i);
            if (oConvertUtils.isEmpty(branch.getParentId())) {
                treeList.add(branch);
                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
                departIdList.add(departIdModel);
            }
        }
        getGrandChildren(treeList,recordList,departIdList);

        //idList = departIdList;
        return treeList;
    }

    /**
     * queryTreeList的子方法====3====
     *该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<SysDepartTreeModel> treeList,List<SysDepartTreeModel> recordList,List<DepartIdModel> idList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeModel model = treeList.get(i);
            DepartIdModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                SysDepartTreeModel m = recordList.get(i1);
                if (m.getParentId()!=null && m.getParentId().equals(model.getId())) {
                    model.getChildren().add(m);
                    DepartIdModel dim = new DepartIdModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getGrandChildren(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }

    }


    /**
     * queryTreeList的子方法 ====4====
     * 该方法是将子节点为空的List集合设置为Null值
     */
    private static void setEmptyChildrenAsNull(List<SysDepartTreeModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeModel model = treeList.get(i);
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
                model.setIsLeaf(true);
            }else{
                setEmptyChildrenAsNull(model.getChildren());
                model.setIsLeaf(false);
            }
        }
        // sysDepartTreeList = treeList;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合
     */
    public static List<SysDepartTreeprojectModel> wrapTreeDataToTreeLists(List<SysDepartproject> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdprojectModel> idList = new ArrayList<DepartIdprojectModel>();
        List<SysDepartTreeprojectModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepartproject depart = recordList.get(i);
            records.add(new SysDepartTreeprojectModel(depart));
        }
        List<SysDepartTreeprojectModel> tree = findChildrens(records, idList);
        //setEmptyChildrenAsNulls(tree);
        setEmptyChildrenAsNulls1(records);
        return records;
    }

    public static List<SysDepartTreeprojectModel> wrapTreeDataToTreeLists1(List<SysDepartproject> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdprojectModel> idList = new ArrayList<DepartIdprojectModel>();
        List<SysDepartTreeprojectModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepartproject depart = recordList.get(i);
            records.add(new SysDepartTreeprojectModel(depart));
        }
        //List<SysDepartTreeprojectModel> tree = findChildrens(records, idList);
        //setEmptyChildrenAsNulls(tree);
        setEmptyChildrenAsNulls1(records);
        return records;
    }
    private static List<SysDepartTreeprojectModel> findChildrens(List<SysDepartTreeprojectModel> recordList,
                                                                 List<DepartIdprojectModel> departIdList) {

        List<SysDepartTreeprojectModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysDepartTreeprojectModel branch = recordList.get(i);
            if (oConvertUtils.isEmpty(branch.getParentId())) {
                treeList.add(branch);
                DepartIdprojectModel departIdModel = new DepartIdprojectModel().convert(branch);
                departIdList.add(departIdModel);
            }
        }
        getGrandChildrens(treeList,recordList,departIdList);

        //idList = departIdList;
        return treeList;
    }
    private static void setEmptyChildrenAsNulls1(List<SysDepartTreeprojectModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeprojectModel model = treeList.get(i);
            List<SysDepartproject> sysDepartprojects = sysDepartprojectService.querylistidList(model.getId());
            if(sysDepartprojects.size()>0){
                model.setIsLeaf(false);
            }else{
                model.setIsLeaf(true);
            }
//            if (model.getChildren().size() == 0) {
//                model.setChildren(null);
//                model.setIsLeaf(true);
//            }else{
//                setEmptyChildrenAsNulls1(model.getChildren());
//                model.setIsLeaf(false);
//            }
        }
        // sysDepartTreeList = treeList;
    }
    private static void getGrandChildrens(List<SysDepartTreeprojectModel> treeList,List<SysDepartTreeprojectModel> recordList,List<DepartIdprojectModel> idList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeprojectModel model = treeList.get(i);
            DepartIdprojectModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                SysDepartTreeprojectModel m = recordList.get(i1);
                if (m.getParentId()!=null && m.getParentId().equals(model.getId())) {
                    model.getChildren().add(m);
                    DepartIdprojectModel dim = new DepartIdprojectModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getGrandChildrens(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }

    }
}
