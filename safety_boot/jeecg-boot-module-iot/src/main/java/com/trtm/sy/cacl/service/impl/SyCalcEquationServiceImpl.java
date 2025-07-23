package com.trtm.sy.cacl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.cacl.mapper.SyCalcEquationMapper;
import com.trtm.sy.cacl.model.SyCalcEquation;
import com.trtm.sy.cacl.service.SyCalcEquationService;
import com.trtm.sy.cacl.util.PolandNotation;
import com.trtm.sy.cacl.util.ReversePolishMultiCalc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SyCalcEquationServiceImpl extends ServiceImpl<SyCalcEquationMapper, SyCalcEquation> implements SyCalcEquationService {

    @Override
    public void calcEquationService(List<SyCalcEquation> calcEquation) {
        for (SyCalcEquation syCalcEquation : calcEquation) {
            String infix = syCalcEquation.getInfix();
            String inf = ReversePolishMultiCalc.replaceAllBlank(infix);
            String[] split = inf.split("=");
            String[] res = split[0].split("#");
            syCalcEquation.setRes(res[1]);
            List<String> formula = PolandNotation.toSuffixFormula(split[1]);
            String str = "#";
            for (String ele : formula) {
                str += ele + "#";
            }
            syCalcEquation.setSuffix(str);
        }
        this.saveBatch(calcEquation);
    }
}
