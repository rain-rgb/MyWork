package com.trtm.iot.tokenrecode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.tokenrecode.entity.Tokenrecode;
import com.trtm.iot.tokenrecode.mapper.TokenrecodeMapper;
import com.trtm.iot.tokenrecode.service.ITokenrecodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: tokenrecode
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
@Service
public class TokenrecodeServiceImpl extends ServiceImpl<TokenrecodeMapper, Tokenrecode> implements ITokenrecodeService {
    @Autowired
    TokenrecodeMapper tokenrecodeMapper;

    @Override
    public List<Tokenrecode> seleycogcod(String orgCode2) {
        return tokenrecodeMapper.seleycogcod(orgCode2+"%");
    }

    @Override
    public List<Tokenrecode> cxqb() {
        return tokenrecodeMapper.cxqb();
    }
}
