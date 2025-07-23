package com.trtm.sy.cacl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.cacl.model.SyCalcEquation;

import java.util.List;

public interface SyCalcEquationService extends IService<SyCalcEquation> {

    void calcEquationService(List<SyCalcEquation> calcEquation);
}
