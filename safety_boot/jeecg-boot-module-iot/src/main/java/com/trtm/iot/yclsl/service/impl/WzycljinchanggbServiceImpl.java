package com.trtm.iot.yclsl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.mapper.WzcailiaonamedictMapper;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.mapper.WzgongyingshangMapper;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.mapper.WzliaocangMapper;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.mapper.WzycljinchanggbMapper;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 原材料收料主表
 * @Author: jeecg-boot
 * @Date: 2021-04-21
 * @Version: V1.0
 */
@Service
public class WzycljinchanggbServiceImpl extends ServiceImpl<WzycljinchanggbMapper, Wzycljinchanggb> implements IWzycljinchanggbService {
    @Autowired
    WzycljinchanggbMapper wzycljinchanggbMapper;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private WzcailiaonamedictMapper wzcailiaonamedictMapper;
    @Autowired
    private WzgongyingshangMapper wzgongyingshangMapper;
    @Autowired
    private WzliaocangMapper wzliaocangMapper;

    @Override
    public List<Wzycljinchanggb> selectycljinchangList(Integer id, Integer taizhangtj) {
//        try {
//            QueryWrapper<Wzycljinchanggb> queryWrapper = new QueryWrapper<>();
//            queryWrapper.ge("id", id);
//            queryWrapper.eq("taizhangtj", taizhangtj);
//            queryWrapper.last("limit 100");
////            queryWrapper.orderByDesc("jinchangshijian");
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return wzycljinchanggbMapper.selectycljinchangList(id,taizhangtj);
    }


    @Override
    public List<Wzycljinchanggb> selectycljinchangListRc(String sbbh, Integer taizhangtj, String beizhu) {
        try {
            QueryWrapper<Wzycljinchanggb> queryWrapper = new QueryWrapper<>();
            // 瑞苍高速用瑞苍高速的统计方式
            queryWrapper.like(sbbh.equals("rcgs"),"shebeibianhao",sbbh);
            // 其他比如甬金衢上的统计方式
            queryWrapper.notLike(!sbbh.equals("rcgs"),"shebeibianhao","rcgs");
            // 备注不为空的话就按备注获取数据
            queryWrapper.eq(StringUtils.isNotBlank(beizhu),"beizhu",beizhu);
//            queryWrapper.ne(!StringUtils.isNotBlank(beizhu),"beizhu",beizhu);
            queryWrapper.ne("jingzhongt","0");
            queryWrapper.eq("taizhangtj", taizhangtj);
            queryWrapper.last("limit 100");
//            queryWrapper.orderByDesc("jinchangshijian");
            return this.list(queryWrapper);
        } catch (Exception e) {
            return null;
//            e.printStackTrace();
        }
//        return wzycljinchanggbMapper.selectycljinchangList(id,taizhangtj);
    }


    @Override
    public void updateistongji(Integer id, Integer taizhangtj) {
        wzycljinchanggbMapper.updateistongji(id, taizhangtj);
    }

    @Override
    public Wzycljinchanggb selectByjinchuliaodanno(String jinchuliaodanno) {
        try {
            QueryWrapper<Wzycljinchanggb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("jinchuliaodanNo", jinchuliaodanno);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Wzycljinchanggb> selectLists(List<String> strsToList1, Integer curid) {
        try {
            QueryWrapper<Wzycljinchanggb> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("id", curid);
            queryWrapper.in("shebeibianhao", strsToList1);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Wzycljinchanggb> selectLists1(String strsToList1, Integer curid) {
//        try {
//            QueryWrapper<Wzycljinchanggb> queryWrapper = new QueryWrapper<>();
//            queryWrapper.ge("id", curid);
//            queryWrapper.in("shebeibianhao", strsToList1);
//            queryWrapper.last("limit 100");
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return wzycljinchanggbMapper.selectLists1(strsToList1,curid);
    }

    @Override
    public List<Wzycljinchanggb> selectListsyjqs(String strsToList1, Integer curid) {
        return wzycljinchanggbMapper.selectListsyjqs(strsToList1,curid);
    }

    @Override
    public int saveMain(Wzycljinchanggb wzycljinchanggb, Wzcailiaonamedict wzcailiaonamedict, Wzgongyingshang wzgongyingshang, Wzliaocang wzliaocang) {
        try {
            LambdaQueryWrapper<Wzycljinchanggb> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Wzycljinchanggb::getGuid, wzycljinchanggb.getGuid());
            Wzycljinchanggb one = this.getOne(queryWrapper);
            if (one == null) {
                wzycljinchanggbMapper.insert(wzycljinchanggb);
            } else {
                wzycljinchanggb.setId(one.getId());
                wzycljinchanggbMapper.updateById(wzycljinchanggb);
            }
            if (wzcailiaonamedict != null) {
                Wzcailiaonamedict wzcailiaonamedict1 = wzcailiaonamedictService.queryselectone1(wzcailiaonamedict.getCailiaono());
                if (wzcailiaonamedict1 == null) {
                    wzcailiaonamedictMapper.insert(wzcailiaonamedict);
                } else {
                    wzcailiaonamedict.setId(wzcailiaonamedict1.getId());
                    wzcailiaonamedictMapper.updateById(wzcailiaonamedict);
                }
            }
            if (wzgongyingshang != null) {
                Wzgongyingshang wzgongyingshang1 = wzgongyingshangService.selectnameone(wzgongyingshang.getGuid());
                if (wzgongyingshang1 == null) {
                    wzgongyingshangMapper.insert(wzgongyingshang);
                } else {
                    wzgongyingshang.setId(wzgongyingshang1.getId());
                    wzgongyingshangMapper.updateById(wzgongyingshang);
                }
            }
            if (wzliaocang != null) {
                Wzliaocang wzliaocang1 = wzliaocangService.queryselectone(wzliaocang.getGuid());
                if (wzliaocang1 == null) {
                    wzliaocangMapper.insert(wzliaocang);
                } else {
                    wzliaocang.setId(wzliaocang1.getId());
                    wzliaocangMapper.updateById(wzliaocang);
                }
            }
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 500;
    }

    @Override
    public Wzycljinchanggb selectone(String jinchuliaodanno) {
        return wzycljinchanggbMapper.selectone(jinchuliaodanno);
    }

    @Override
    public List<Wzycljinchanggb> selecones(int istongji, Integer curid) {
        return wzycljinchanggbMapper.selecones(istongji,curid);
    }

    @Override
    public List<Wzycljinchanggb> selectyclList(Integer curid, String shebeilist) {
        return wzycljinchanggbMapper.selectyclList(curid,shebeilist);
    }

    @Override
    public List<Wzycljinchanggb> selectydyclList(Integer curid, String shebeilist) {
        return wzycljinchanggbMapper.selectydyclList(curid,shebeilist);
    }

    @Override
    public List<Wzycljinchanggb> selectlistjz(Integer curid, int jingzhongTtj) {
        return wzycljinchanggbMapper.selectlistjz(curid,jingzhongTtj);
    }

    @Override
    public List<Wzycljinchanggb> slistrqid(String shebeilist, Integer curid) {
        return wzycljinchanggbMapper.slistrqid(shebeilist,curid);
    }
}
