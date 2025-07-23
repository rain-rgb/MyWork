package com.trtm.iot.hc_token.service.impl;

import com.trtm.iot.hc_token.entity.HcToken;
import com.trtm.iot.hc_token.mapper.HcTokenMapper;
import com.trtm.iot.hc_token.service.IHcTokenService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: token
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Service
public class HcTokenServiceImpl extends ServiceImpl<HcTokenMapper, HcToken> implements IHcTokenService {

}
