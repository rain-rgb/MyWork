package com.trtm.iot.tokenrecode.service;

import com.trtm.iot.tokenrecode.entity.Tokenrecode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: tokenrecode
 * @Author: jeecg-boot
 * @Date:   2023-02-22
 * @Version: V1.0
 */
public interface ITokenrecodeService extends IService<Tokenrecode> {

    List<Tokenrecode> seleycogcod(String orgCode2);

    List<Tokenrecode> cxqb();
}
