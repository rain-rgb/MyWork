package org.jeecg.modules.system.model;

import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDepartproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 分部分项 存储树结构数据的实体类
 * <p>
 *
 * @Author Steve
 * @Since 2019-01-22
 */
public class SysDepartTreeprojectModel implements Serializable{

    private static final long serialVersionUID = 1L;

    /** 对应SysDepart中的id字段,前端数据树中的key*/
    private String key;

    /** 对应SysDepart中的id字段,前端数据树中的value*/
    private String value;

    /** 对应depart_name字段,前端数据树中的title*/
    private String title;


    private boolean isLeaf;
    // 以下所有字段均与SysDepart相同

    private String id;

    private String parentId;

    private String departName;

    private String departNameEn;

    private String departNameAbbr;

    private Integer departOrder;

    private String description;

    private String orgCategory;

    private String orgType;

    private String orgCode;

    private String mobile;

    private String fax;

    private String address;

    private String memo;

    private String status;

    private String delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String orgCodes;

    private Integer pici;

    private Integer parentstatus;

    private List<SysDepartTreeprojectModel> children = new ArrayList<>();
    private String wbsStructureType;
    private String treeid;
    private String templateid;

    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     * @param SysDepartproject
     */
	public SysDepartTreeprojectModel(SysDepartproject SysDepartproject) {
		this.key = SysDepartproject.getId();
        this.value = SysDepartproject.getId();
        this.title = SysDepartproject.getDepartName();
        this.id = SysDepartproject.getId();
        this.parentId = SysDepartproject.getParentId();
        this.departName = SysDepartproject.getDepartName();
        this.departNameEn = SysDepartproject.getDepartNameEn();
        this.departNameAbbr = SysDepartproject.getDepartNameAbbr();
        this.departOrder = SysDepartproject.getDepartOrder();
        this.description = SysDepartproject.getDescription();
        this.orgCategory = SysDepartproject.getOrgCategory();
        this.orgType = SysDepartproject.getOrgType();
        this.orgCode = SysDepartproject.getOrgCode();
        this.mobile = SysDepartproject.getMobile();
        this.fax = SysDepartproject.getFax();
        this.address = SysDepartproject.getAddress();
        this.memo = SysDepartproject.getMemo();
        this.status = SysDepartproject.getStatus();
        this.delFlag = SysDepartproject.getDelFlag();
        this.createBy = SysDepartproject.getCreateBy();
        this.createTime = SysDepartproject.getCreateTime();
        this.updateBy = SysDepartproject.getUpdateBy();
        this.updateTime = SysDepartproject.getUpdateTime();
        this.orgCodes= SysDepartproject.getOrgCodes();
        this.pici= SysDepartproject.getPici();
        this.parentstatus= SysDepartproject.getParentstatus();
        this.wbsStructureType= SysDepartproject.getWbsStructureType();
        this.treeid= SysDepartproject.getTreeid();
        this.templateid = SysDepartproject.getTemplateid();
    }

    public String getTreeid() {
        return treeid;
    }

    public String getTemplateid() {
        return templateid;
    }

    public void setTemplateid(String templateid) {
        this.templateid = templateid;
    }

    public void setTreeid(String treeid) {
        this.treeid = treeid;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isleaf) {
         this.isLeaf = isleaf;
    }

    public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SysDepartTreeprojectModel> getChildren() {
        return children;
    }

    public void setChildren(List<SysDepartTreeprojectModel> children) {
        if (children==null){
            this.isLeaf=true;
        }
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getOrgCategory() {
		return orgCategory;
	}

	public void setOrgCategory(String orgCategory) {
		this.orgCategory = orgCategory;
	}

	public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDepartNameEn() {
        return departNameEn;
    }

    public void setDepartNameEn(String departNameEn) {
        this.departNameEn = departNameEn;
    }

    public String getDepartNameAbbr() {
        return departNameAbbr;
    }

    public void setDepartNameAbbr(String departNameAbbr) {
        this.departNameAbbr = departNameAbbr;
    }

    public Integer getDepartOrder() {
        return departOrder;
    }

    public void setDepartOrder(Integer departOrder) {
        this.departOrder = departOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOrgCodes() {
        return orgCodes;
    }

    public void setOrgCodes(String orgCodes) {
        this.orgCodes = orgCodes;
    }

    public Integer getPici() {
        return pici;
    }

    public void setPici(Integer pici) {
        this.pici = pici;
    }

    public Integer getParentstatus() {
        return parentstatus;
    }

    public String getWbsStructureType() {
        return wbsStructureType;
    }

    public void setParentstatus(Integer parentstatus) {
        this.parentstatus = parentstatus;
    }

    public void setWbsStructureType(String wbsStructureType) {
        this.wbsStructureType = wbsStructureType;
    }

    public SysDepartTreeprojectModel() { }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        SysDepartTreeprojectModel model = (SysDepartTreeprojectModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(parentId, model.parentId) &&
                Objects.equals(departName, model.departName) &&
                Objects.equals(departNameEn, model.departNameEn) &&
                Objects.equals(departNameAbbr, model.departNameAbbr) &&
                Objects.equals(departOrder, model.departOrder) &&
                Objects.equals(description, model.description) &&
                Objects.equals(orgCategory, model.orgCategory) &&
                Objects.equals(orgType, model.orgType) &&
                Objects.equals(orgCode, model.orgCode) &&
                Objects.equals(mobile, model.mobile) &&
                Objects.equals(fax, model.fax) &&
                Objects.equals(address, model.address) &&
                Objects.equals(memo, model.memo) &&
                Objects.equals(status, model.status) &&
                Objects.equals(delFlag, model.delFlag) &&
                Objects.equals(createBy, model.createBy) &&
                Objects.equals(createTime, model.createTime) &&
                Objects.equals(updateBy, model.updateBy) &&
                Objects.equals(updateTime, model.updateTime) &&
                Objects.equals(orgCodes, model.orgCodes) &&
                Objects.equals(children, model.children) &&
                Objects.equals(pici, model.pici);
    }

    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(id, parentId, departName, departNameEn, departNameAbbr,
        		departOrder, description, orgCategory, orgType, orgCode, mobile, fax, address,
        		memo, status, delFlag, createBy, createTime, updateBy, updateTime,orgCodes,pici,
        		children);
    }

}
