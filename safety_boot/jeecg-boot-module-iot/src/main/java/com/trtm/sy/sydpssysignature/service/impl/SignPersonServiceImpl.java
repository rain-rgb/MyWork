package com.trtm.sy.sydpssysignature.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.sydpssysignature.entity.SignPerson;
import com.trtm.sy.sydpssysignature.mapper.SignPersonMapper;
import com.trtm.sy.sydpssysignature.service.SignPersonService;
import org.springframework.stereotype.Service;

@Service
public class SignPersonServiceImpl extends ServiceImpl<SignPersonMapper, SignPerson> implements SignPersonService {
}
