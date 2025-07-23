package com.trtm.sy.registerformula.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.registerformula.mapper.SyDesignFormulasMapper;
import com.trtm.sy.registerformula.model.SyDesignFormulas;
import com.trtm.sy.registerformula.model.SyDesignFormulasRequest;
import com.trtm.sy.registerformula.service.SyDesignFormulasService;
import com.trtm.sy.registerformula.utils.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
// @DS("#header.dataSourcePoolName")
public class SyDesignFormulasServiceImpl extends ServiceImpl<SyDesignFormulasMapper, SyDesignFormulas> implements SyDesignFormulasService {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String FORMULA = "formula:";

    @Override
    @Transactional
    public void saveCalc(SyDesignFormulasRequest request) {
        String jlbbh = request.getJlbbm();
        List<SyDesignFormulas> syDesignFormulas = request.getSyDesignFormulas();
        QueryWrapper<SyDesignFormulas> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SyDesignFormulas::getJlbbm, jlbbh);
        List<SyDesignFormulas> formulasList = this.list(wrapper);
        if (formulasList.size() > 0) {
            this.remove(wrapper);
            List cacheList = redisTemplate.opsForList().range(FORMULA + jlbbh, 0, -1);
            if (cacheList.size() > 0) {
                redisTemplate.delete(FORMULA + jlbbh);
            }
        }

        for (SyDesignFormulas formulas : syDesignFormulas) {
            String infix = formulas.getInfix();
            //去除空格符
            infix = replaceAllBlank(infix);
            //根据=分开，规则=左边为计算结果，=右边是计算步骤
            String[] params = infix.split("=");
            String res = params[0].replace("#", "").trim();
            formulas.setRes(res);
            //计算步骤根据#分隔
            String[] gs = params[1].split("#");

            List<String> list = new ArrayList<>();
            Stack<String> stack = new Stack<>();
            for (String g : gs) {
                if (StringUtils.isEmpty(g)) {
                    continue;
                }
                if (g.matches("[a-zA-Z0-9_]+")) {
                    list.add(g);
                } else if (g.equals("{")) {
                    list.add(g);
                    stack.push(g);
                } else if (g.equals("}")) {
                    while (!stack.peek().equals("{")) {
                        list.add(stack.pop());
                    }
                    stack.pop();
                    list.add(g);
                } else if (g.equals("[")) {
                    list.add(g);
                    stack.push(g);
                } else if (g.equals("]")) {
                    list.add(g);
                    while (!stack.peek().equals("[")) {
                        list.add(stack.pop());
                    }
                    stack.pop();
                } else if (g.equals("(")) {
                    stack.push(g);
                } else if (g.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        list.add(stack.pop());
                    }
                    stack.pop();
                } else {
                    while (stack.size() != 0 && Operation.getValue(stack.peek()) >= Operation.getValue(g)) {
                        list.add(stack.pop());
                    }
                    stack.push(g);
                }
            }
            while (stack.size() != 0) {
                list.add(stack.pop());
            }
            String suffix = "#";
            for (String obj : list) {
                suffix += obj + "#";
            }
            formulas.setSuffix(suffix);
        }
        this.saveBatch(syDesignFormulas);
        redisTemplate.opsForList().rightPush(FORMULA + jlbbh, syDesignFormulas);
    }


    @Override
    public Map<String, Object> calcFormulas(String tableNumber, HashMap<String, Object> map) {
        List<List<SyDesignFormulas>> cacheList = redisTemplate.opsForList().range(FORMULA + tableNumber, 0, -1);
        if (!(cacheList.size() > 0)) {
            QueryWrapper<SyDesignFormulas> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(SyDesignFormulas::getJlbbm, tableNumber);
            List<SyDesignFormulas> formulasList = this.list(wrapper);
            if (formulasList.size() > 0) {
                cacheList.add(formulasList);
                redisTemplate.opsForList().rightPush(FORMULA + tableNumber, formulasList);
            } else {
                throw new JeecgBootException("该记录表暂无计算公式");
            }
        }
        Stack<String> calcStack = new Stack<>();
        List<String> calcList = new ArrayList<>();
        for (SyDesignFormulas formulas : cacheList.get(0)) {
            String suffix = formulas.getSuffix();
            String res = formulas.getRes();
            String xy = formulas.getXy();

            List<String> list = Arrays.asList(suffix.substring(1).split("#"));
            AtomicBoolean numIsEmpty = new AtomicBoolean(false);
            list.forEach(k -> {
                if (!k.matches(".*[+\\-*/\\[\\]{}√^].*") && !k.matches("\\d+(\\.\\d+)?")) {
                    if (oConvertUtils.isEmpty(map.get(k))) {
                        numIsEmpty.set(true);
                    }
                }
            });
            if (numIsEmpty.get()) {
                continue;
            }
            for (String s : list) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                if (!s.matches(".*[+\\-*/\\[\\]{}√^].*") && !s.matches("\\d+(\\.\\d+)?")) {
                    if (oConvertUtils.isEmpty(map.get(s))) {
                        throw new JeecgBootException("表格中" + s + "对应的值为空，请检查数据是否输入或者公式计算顺序是否正确");
                    }
                    calcList.add(String.valueOf(map.get(s)));
                } else {
                    calcList.add(s);
                }
            }

            for (int i = 0; i < calcList.size(); i++) {
                String fh = String.valueOf(calcList.get(i));
                if (fh.matches("\\d+(\\.\\d+)?") || fh.matches("-?\\d+(\\.\\d+)?") || fh.matches("[\\[\\]\\{\\}]+")) {
                    calcStack.push(fh);
                } else {
                    String re = null;
                    BigDecimal result = null;
                    if (fh.matches("\\+|\\-|\\*|\\/")) {
                        BigDecimal num2 = getBigDecimal(calcStack.pop());
                        BigDecimal num1 = getBigDecimal(calcStack.pop());
                        switch (fh) {
                            case "+":
                                result = num1.add(num2);
                                break;
                            case "-":
                                result = num1.subtract(num2);
                                break;
                            case "*":
                                result = num1.multiply(num2);
                                break;
                            case "/":
                                result = num1.divide(num2, 5, BigDecimal.ROUND_DOWN);
                                break;
                        }
                        re = String.valueOf(result);
                        calcStack.push(re);
                    } else if (fh.equals("^")) {
                        String m = calcStack.pop();
                        String num = null;
                        do {
                            String n = calcStack.pop();
                            if (n.matches("\\d+(\\.\\d+)?")||n.matches("-\\d+(\\.\\d+)?")) {
                                num = n;
                            }
                        } while (!calcStack.peek().equals("["));
                        calcStack.pop();
                        if (num != null) {
                            re = String.valueOf(Math.pow(Double.parseDouble(num), Double.parseDouble(m)));
                        }
                        calcStack.push(re);
                    } else if (fh.equals("√")) {
                        String num = null;
                        do {
                            String g = calcStack.pop();
                            if (g.matches("\\d+(\\.\\d+)?")) {
                                num = g;
                            }
                        } while (!calcStack.peek().equals("{"));
                        calcStack.pop();
                        if (num != null) {
                            re = String.valueOf(Math.sqrt(Double.parseDouble(num)));
                        }
                        calcStack.push(re);
                    }
                }
            }
            String re = calcStack.pop();
            if (oConvertUtils.isNotEmpty(xy)) {
                switch (xy) {
                    case "0.1":
                        re = String.valueOf(toRound(Float.parseFloat(re), 0.1f));
                        break;
                    case "0.01":
                        re = String.valueOf(toRound(Float.parseFloat(re), 0.01f));
                        break;
                    case "0.001":
                        re = String.valueOf(toRound(Float.parseFloat(re), 0.001f));
                        break;
                    case "0.5":
                        re = String.valueOf(toRound(Float.parseFloat(re), 0.5f));
                        break;
                    case "1":
                        re = String.valueOf(toRound(Float.parseFloat(re), 1f));
                        break;
                    case "5":
                        re = String.valueOf(toRound(Float.parseFloat(re), 5f));
                        break;
                    case "10":
                        re = String.valueOf(toRound(Float.parseFloat(re), 10f));
                        break;
                    case "100":
                        re = String.valueOf(toRound(Float.parseFloat(re), 100f));
                        break;
                }
            }

            map.put(res, re);
            calcList.clear();
        }
        return map;
    }

    /**
     * 修约方法
     *
     * @param num 需要修约的数值
     * @param f2  修约的精度
     * @return
     */
    public static String toRound(float num, float f2) {

        int decimalPlaces = 0;
        if (f2 == 0.1f) {
            decimalPlaces = 1;
        } else if (f2 == 0.01f) {
            decimalPlaces = 2;
        } else if (f2 == 0.001f) {
            decimalPlaces = 3;
        } else if (f2 == 0.0001f) {
            decimalPlaces = 4;
        } else if (f2 == 1f) {
            decimalPlaces = 0;
        } else if (f2 == 0.5f) {
            decimalPlaces = 1;
            num = num * 2 / 10;
        } else if (f2 == 5f) {
            decimalPlaces = 0;
            num = num * 2 / 10;
        } else if (f2 == 10f) {
            decimalPlaces = 0;
            num = num / 10;
        } else if (f2 == 100f) {
            decimalPlaces = 0;
            num = num / 100;
        }

        BigDecimal originalNum = new BigDecimal(Float.toString(num));
        BigDecimal multiplier = new BigDecimal(10).pow(decimalPlaces);
        BigDecimal scaledNum = originalNum.multiply(multiplier);

        int i = scaledNum.intValue();
        BigDecimal f = scaledNum.subtract(new BigDecimal(i));
        BigDecimal threshold = new BigDecimal("0.5");
        BigDecimal e = new BigDecimal("1E-8");

        int r = (f.compareTo(threshold.subtract(e)) > 0 || (f.compareTo(threshold) == 0 && i % 2 != 0)) ? i + 1 : i;
        BigDecimal res = new BigDecimal(r).divide(multiplier);

        if (decimalPlaces >= 0) {
            if (f2 == 5 || f2 == 0.5) {
                res = res.multiply(new BigDecimal("10")).divide(new BigDecimal("2"));
            } else if (f2 == 10) {
                res = res.multiply(new BigDecimal("10"));
            } else if (f2 == 100) {
                res = res.multiply(new BigDecimal("100"));
            }
            return res.setScale(decimalPlaces, BigDecimal.ROUND_HALF_EVEN).toString();
        } else {
            return res.toString();
        }
    }

    @Override
    public List<SyDesignFormulas> getCalc(String tableNumber) {
        return this.list(new QueryWrapper<SyDesignFormulas>().lambda().eq(SyDesignFormulas::getJlbbm, tableNumber));
    }


    private SyDesignFormulas getFormula(String str) {
        SyDesignFormulas bean = new SyDesignFormulas();
        str = replaceAllBlank(str);
        String data = str.substring(str.indexOf("(") + 1, str.length() - 1);
        String[] cla = data.split(",");
        for (String s : cla) {
            String[] split = s.split("=");
            switch (split[0]) {
                case "id":
                    bean.setId(split.length > 1 ? Integer.valueOf(split[1]) : null);
                    break;
                case "jlbbm":
                    bean.setJlbbm(split.length > 1 ? split[1] : null);
                    break;
                case "infix":
                    bean.setInfix(split.length > 1 ? split[1] : null);
                    break;
                case "suffix":
                    bean.setSuffix(split.length > 1 ? split[1] : null);
                    break;
                case "res":
                    bean.setRes(split.length > 1 ? split[1] : null);
                    break;
                case "bol":
                    bean.setBol(split.length > 1 ? split[1] : null);
                    break;
                case "od":
                    bean.setOd(split.length > 1 ? Integer.valueOf(split[1]) : null);
                    break;
                case "xy":
                    bean.setXy(split.length > 1 ? split[1] : null);
                    break;
                case "remark":
                    bean.setRemark(split.length > 1 ? split[1] : null);
                    break;
            }
        }
        return bean;
    }

    /**
     * 按照 1修约
     *
     * @param f
     * @return
     */
    public static float roundOne(float f) {
        int integer = (int) f / 1;
        float remainder = f % 1;
        if (remainder > 0.5) {
            return integer + 1;
        } else if (remainder == 0.5) {
            return (integer & 1) == 1 ? integer + 1 : integer;
        } else {
            return integer;
        }
    }


    /**
     * 修约方法
     *
     * @param f1 需要修约的数值
     * @param f2 修约的精度
     * @return
     */
    public static float round(float f1, float f2) {
        float multiplying = 1 / f2;
        return roundOne(f1 * multiplying) / multiplying;
    }


    public static BigDecimal getBigDecimal(String s) {
        if (oConvertUtils.isEmpty(s) || "/".equals(s) || "\\".equals(s)) {
            return new BigDecimal("0");
        }
        return new BigDecimal(s);
    }


    /**
     * 去除所有空白符
     *
     * @param s
     * @return
     */
    private String replaceAllBlank(String s) {
        // \\s+ 匹配任何空白字符，包括空格、制表符、换页符等等, 等价于[ \f\n\r\t\v]
        return s.replaceAll("\\s+", "");
    }
}
