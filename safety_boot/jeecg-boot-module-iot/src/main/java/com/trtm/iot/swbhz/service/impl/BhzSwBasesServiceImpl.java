package com.trtm.iot.swbhz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementWarnVO;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.entity.BhzSwWarnVO;
import com.trtm.iot.swbhz.mapper.BhzSwCailiaoMapper;
import com.trtm.iot.swbhz.mapper.BhzSwBasesMapper;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.system.entity.ShebeiInfo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 水稳主表
 * @Author: jeecg-boot
 * @Date:   2021-02-24
 * @Version: V1.0
 */
@Service
public class BhzSwBasesServiceImpl extends ServiceImpl<BhzSwBasesMapper, BhzSwBases> implements IBhzSwBasesService {

	@Autowired
	private BhzSwBasesMapper bhzSwBasesMapper;
	@Autowired
	private BhzSwCailiaoMapper bhzSwCailiaoMapper;

	@Override
	public void updateByGuid(BhzSwBases bhzSwBases) {
		bhzSwBasesMapper.updateByGuid(bhzSwBases);
	}

	@Override
	@Transactional
	public void saveMain(BhzSwBases bhzSwBases, List<BhzSwCailiao> bhzSwCailiaoList) {
		bhzSwBasesMapper.insert(bhzSwBases);
		if(bhzSwCailiaoList!=null && bhzSwCailiaoList.size()>0) {
			for(BhzSwCailiao entity:bhzSwCailiaoList) {
				//外键设置
				entity.setBaseGuid(bhzSwBases.getGuid());
				bhzSwCailiaoMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(BhzSwBases bhzSwBases,List<BhzSwCailiao> bhzSwCailiaoList) {
		bhzSwBasesMapper.updateById(bhzSwBases);

		//1.先删除子表数据
		bhzSwCailiaoMapper.deleteByMainId(bhzSwBases.getGuid());

		//2.子表数据重新插入
		if(bhzSwCailiaoList!=null && bhzSwCailiaoList.size()>0) {
			for(BhzSwCailiao entity:bhzSwCailiaoList) {
				//外键设置
				entity.setBaseGuid(bhzSwBases.getGuid());
				bhzSwCailiaoMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		bhzSwCailiaoMapper.deleteByMainId(id);
		bhzSwBasesMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			bhzSwCailiaoMapper.deleteByMainId(id.toString());
			bhzSwBasesMapper.deleteById(id);
		}
	}

	@Override
	public List<BhzSwBases> selectswbhzone(Integer id, Integer alertstate) {
		try {
			QueryWrapper<BhzSwBases> queryWrapper=new QueryWrapper<>();
			queryWrapper.ge("id",id);
			queryWrapper.eq("alertstate", alertstate);
			queryWrapper.last("limit 100");
			return this.list(queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public List<BhzSwBases> selectswbhzones(Integer id, Integer alertstate) {
//		try {
//			QueryWrapper<BhzSwBases> queryWrapper=new QueryWrapper<>();
//			queryWrapper.ge("id",id);
//			queryWrapper.eq("alertstate", alertstate);
//			queryWrapper.last("limit 1");
//			return this.list(queryWrapper);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return bhzSwBasesMapper.selectswbhzones(id,alertstate);
	}

	@Override
	public List<BhzSwBases> selectswbhzlist(Integer id, Integer alertstate, String shebeibianhao) {
		try {
			QueryWrapper<BhzSwBases> queryWrapper=new QueryWrapper<>();
			queryWrapper.ge("id",id);
			queryWrapper.eq("alertstate", alertstate);
			queryWrapper.in("shebeibianhao",shebeibianhao);
			queryWrapper.last("limit 100");
			return this.list(queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<BhzSwBases> selectswbhzcblist(Integer id, Integer alertstate, String shebeibianhao, Integer chaobiaodengji) {
		try {
			QueryWrapper<BhzSwBases> queryWrapper=new QueryWrapper<>();
			queryWrapper.ge("id",id);
			queryWrapper.eq("alertstate", alertstate);
			queryWrapper.gt("chaobiaodengji",chaobiaodengji);
			queryWrapper.in("shebeibianhao",shebeibianhao);
			queryWrapper.last("limit 100");
			return this.list(queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateswbhzone(String id, Integer alertstate) {

		return bhzSwBasesMapper.updatealertsate(id,alertstate);
	}

	@Override
	public int updateswbhzdengji(String id, Integer chaobiaodengji) {

		return bhzSwBasesMapper.updateswbhzdengji(id,chaobiaodengji);
	}

    @Override
    public BhzSwWarnVO selectWranCount(String orgCode) {
		return bhzSwBasesMapper.selectWranCount(orgCode);
    }

    @Override
    public List<BhzCementWarnVO> selectWranCountByorgcde(String orgCode) {
		return bhzSwBasesMapper.selectWranCountByorgcde(orgCode);
    }

    @Override
    public List<BhzCementWarnVO> selectWranCountByshebeino(String code, int i) {
		return bhzSwBasesMapper.selectWranCountByshebeino(code,i);
    }

    @Override
    public List<BhzCementWarnVO> selectBiaoduanByshebeino(String sysOrgCode) {

		return bhzSwBasesMapper.selectBiaoduanByshebeino(sysOrgCode);
    }

    @Override
    public Integer selectBiheCount(String orgCode) {
		return bhzSwBasesMapper.selectBiheCount(orgCode);
    }

    @Override
	public int updateswbhzongliang(String id, Float zongchanliang) {

		return bhzSwBasesMapper.updateswbhzongliang(id,zongchanliang);
	}

	@Override
	public List<BhzSwBases> selectList1(String shebeiNo, Integer id) {
		return bhzSwBasesMapper.selectList1(shebeiNo, id);
	}

	@Override
	public List<BhzSwBases> selectListToJTJT(String shebeilist, Integer curid) {
		return bhzSwBasesMapper.selectListToJTJT(shebeilist, curid);
	}

	@Override
	public List<BhzSwBases> selectListToDJ(String shebeilist, Integer curid) {
		return bhzSwBasesMapper.selectListToDJ(shebeilist, curid);
	}

	@Override
	public List<BhzSwBases> selectListToTCP(String shebeilist, Integer curid) {
		return bhzSwBasesMapper.selectListToTCP(shebeilist, curid);
	}

    @Override
    public List<BhzSwBases> selectBHZUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel) {
		return bhzSwBasesMapper.selectBHZUnifiedProcess(curid,alertstate,shebeiList,overLevel);
    }

    @Override
	public List<BhzSwBases> selectByGuid(String guid) {
		return bhzSwBasesMapper.selectByGuid(guid);
	}

	@Override
	public String getUnit(String shebeibianhao) {
		return bhzSwBasesMapper.getUnit(shebeibianhao);
	}

	@Override
	public List<BhzSwBases> selectcbList(String shebeilist, Integer curid) {
		return bhzSwBasesMapper.selectcbList(shebeilist, curid);
	}

	@Override
	public List<BhzSwBases> selectkzlist(String shebeilist, Integer curid) {
		return bhzSwBasesMapper.selectkzlist(shebeilist, curid);
	}
}
