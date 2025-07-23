package com.trtm.sy.registerformula.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.registerformula.model.SyDesignFormulas;
import com.trtm.sy.registerformula.model.SyDesignFormulasRequest;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SyDesignFormulasService extends IService<SyDesignFormulas> {

    void saveCalc(SyDesignFormulasRequest request);

    Map<String, Object> calcFormulas(String tableNumber, HashMap<String, Object> map);

    List<SyDesignFormulas> getCalc(String tableNumber);
}
