package com.trtm.iot.czsh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.mapper.BhzCementOverHandlerMapper;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 拌合站处置审核信息
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
@Service
public class BhzCementOverHandlerServiceImpl extends ServiceImpl<BhzCementOverHandlerMapper, BhzCementOverHandler> implements IBhzCementOverHandlerService {

	@Resource
	private BhzCementOverHandlerMapper bhzCementOverHandlerMapper;

	@Override
	@Transactional
	public void saveMain(BhzCementOverHandler bhzCementOverHandler ) {
		bhzCementOverHandlerMapper.insert(bhzCementOverHandler);
	}

	@Override
	@Transactional
	public void updateMain(BhzCementOverHandler bhzCementOverHandler) {
		bhzCementOverHandlerMapper.updateById(bhzCementOverHandler);

		//1.先删除子表数据

		//2.子表数据重新插入
	}

	@Override
	@Transactional
	public void delMain(String id) {
		bhzCementOverHandlerMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			bhzCementOverHandlerMapper.deleteById(id);
		}
	}

	/**
	 * 根据id修改或者添加审核信息
	 * @param spyj
	 * @param spjg
	 * @param hntbatch
	 * @param bizPath
	 */
	@Override
	public int shenheAddOrUpdate(String spyj, String spjg, String hntbatch, String bizPath,String  shenpiren) {
		String i = bhzCementOverHandlerMapper.dataById(hntbatch);
		int result = 0;
		if (i==null){
			 result = bhzCementOverHandlerMapper.shenHeAddById(spyj, spjg, hntbatch, bizPath,shenpiren);
		}else {
			result = bhzCementOverHandlerMapper.shenHeUpdateById(spyj, spjg, hntbatch, bizPath,shenpiren);
		}
		return result;
	}

	/**
	 * 根据id添加修改处置信息
	 * @param wtyy
	 * @param clfs
	 * @param cljg
	 * @param hntbatch
	 * @param bizPath
	 * @param chuzhiren
	 * @return
	 */
	@Override
	public int chuZhiAddOrUpDate(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren) {
		String i = bhzCementOverHandlerMapper.dataById(hntbatch);
		int result = 0;
		if (i==null){
			result = bhzCementOverHandlerMapper.chuZhiAddById(wtyy,clfs,cljg,hntbatch,bizPath,chuzhiren);
		}else {
			result = bhzCementOverHandlerMapper.chuZhiUpdateById(wtyy,clfs,cljg,hntbatch,bizPath,chuzhiren);
		}
		return result;
	}

	@Override
	public BhzCementOverHandler selectlist(String baseid) {
		try {
			QueryWrapper<BhzCementOverHandler> queryWrapper=new QueryWrapper<>();
			queryWrapper.eq("baseid",baseid);
			return this.getOne(queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Page<BhzCementOverHandler> selectlist1(String baseid) {
		try {
			QueryWrapper<BhzCementOverHandler> queryWrapper=new QueryWrapper<>();
			Page<BhzCementOverHandler> page = new Page<BhzCementOverHandler>(1, 1);
			queryWrapper.eq("baseid",baseid);
			return this.page(page,queryWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<BhzCementOverHandler> listToday(String shebeilist) {
		return bhzCementOverHandlerMapper.listToday(shebeilist);
	}

	@Override
	public Integer BhzCZAddOrUpDate(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren, int status) {
		String i = bhzCementOverHandlerMapper.dataById(hntbatch);
		int result = 0;
		if (i==null){
			result = bhzCementOverHandlerMapper.BhzchuZhiAddById(wtyy,clfs,cljg,hntbatch,bizPath,chuzhiren,status);
		}else {
			result = bhzCementOverHandlerMapper.BhzchuZhiUpdateById(wtyy,clfs,cljg,hntbatch,bizPath,chuzhiren,status);
		}
		return result;
	}

	@Override
	public Integer supervisorAddOrUpdate(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren, int status) {
		String i = bhzCementOverHandlerMapper.dataById(hntbatch);
		int result = 0;
		if (i==null){
			result = bhzCementOverHandlerMapper.BhzSHAddById(spyj, spjg, hntbatch, bizPath,shenpiren,status);
		}else {
			result = bhzCementOverHandlerMapper.BhzSHUpdateById(spyj, spjg, hntbatch, bizPath,shenpiren,status);
		}
		return result;
	}

	@Override
	public Integer headquartersAddOrUpdate(String zhbyj, String zhbjg, String hntbatch, String bizPath, String shenpiren, int status) {
		String i = bhzCementOverHandlerMapper.dataById(hntbatch);
		int result = 0;
		if (i==null){
			result = bhzCementOverHandlerMapper.BhzZHBSHAddById(zhbyj, zhbjg, hntbatch, bizPath,shenpiren,status);
		}else {
			result = bhzCementOverHandlerMapper.BhzZHBSHUpdateById(zhbyj, zhbjg,  hntbatch, bizPath,shenpiren,status);
		}
		return result;
	}

    @Override
    public Integer supervisorBohui(String jlbh, String hntbatch, String people, int status) {
		String i = bhzCementOverHandlerMapper.dataById(hntbatch);
		int result = 0;
		if (i==null){
			result = bhzCementOverHandlerMapper.BhzShBoHuiAddById(jlbh,hntbatch,people,status);
		}else {
			result = bhzCementOverHandlerMapper.BhzShBoHuiUpdateById(jlbh,  hntbatch, people,status);
		}
		return result;

    }

	@Override
	public Integer headquartersBohui(String zhbbh, String hntbatch,  String shenpiren, int status) {
		String i = bhzCementOverHandlerMapper.dataById(hntbatch);
		int result = 0;
		if (i==null){
			result = bhzCementOverHandlerMapper.BhzZHBBoHuiAddById(zhbbh, hntbatch,  shenpiren,status);
		}else {
			result = bhzCementOverHandlerMapper.BhzZHBBoHuiUpdateById(zhbbh,  hntbatch,  shenpiren,status);
		}
		return result;
	}


}
