package com.trtm.sy.cacl.util;

import com.trtm.sy.cacl.util.*;
import org.jeecg.common.util.oConvertUtils;

import java.math.BigDecimal;
import java.util.*;

public class PolandNotation {


    public static void main(String[] args) {


//        String expression = "1.2+(2.3+3)*4-5";
//        List<String> infixExpressionList = toInfixExpressionList(expression);
//        System.out.println("中缀表达式对应的List= " + infixExpressionList);
//        List<String> suffixExpressionList = parseSuffixExpressionList(infixExpressionList);
//        System.out.println("后缀表达式对应的List= " + suffixExpressionList);

        String ex = "#klqd# = #jxhz1# * #1000# / #hjmj1#";
        System.out.println(ex);
        ex = ReversePolishMultiCalc.replaceAllBlank(ex);
        System.out.println(ex);
        String[] split = ex.split("=");
        List<String> formula = toSuffixFormula(split[1]);
        String str = "#";
        for (String s : formula) {
            str += s + "#";
        }
        System.out.println(formula);
        System.out.println(str);

        Map<String, String> map = new HashMap<>();
        map.put("jxhz1", "234");
        map.put("hjmj1", "82");

        List<String> list = new ArrayList<>();
        for (String s : formula) {
            if (!ReversePolishMultiCalc.isSymbol(s) && !s.matches("\\d+(\\.\\d+)?")) {
                list.add(map.get(s));
            } else {
                list.add(s);
            }
        }
        System.out.println(list);
        System.out.println(calculate(list));


    }

    public static List<String> toInfixExpressionList(String s) {
        List<String> list = new ArrayList<>();
        int i = 0;
        String str;
        char c;
        do {
            if (((c = s.charAt(i)) < 48 || (c = s.charAt(i)) > 57) && !((c = s.charAt(i)) == 46)) {
                list.add("" + c);
                i++;
            } else {
                str = "";
                while (i < s.length() && (((c = s.charAt(i)) >= 48 && (c = s.charAt(i)) <= 57) || (c = s.charAt(i)) == 46)) {
                    str += c;
                    i++;
                }
                list.add(str);
            }
        } while (i < s.length());
        return list;
    }

    public static List<String> parseSuffixExpressionList(List<String> ls) {
        Stack<String> s1 = new Stack<>();
        List<String> s2 = new ArrayList<>();

        for (String item : ls) {
            if (item.matches("\\d+(\\.\\d+)?")) {
                s2.add(item);
            } else if (item.equals("(")) {
                s1.push(item);
            } else if (item.equals(")")) {
                while (!s1.peek().equals("(")) {
                    s2.add(s1.pop());
                }
                s1.pop();
            } else {
                while (s1.size() != 0 && Operation.getValue(s1.peek()) >= Operation.getValue(item)) {
                    s2.add(s1.pop());
                }
                s1.push(item);
            }
        }
        while (s1.size() != 0) {
            s2.add(s1.pop());
        }
        return s2;
    }

    public static List<String> toSuffixFormula(String s) {
        Stack<String> s1 = new Stack<>();
        List<String> s2 = new ArrayList<>();
        String[] split = s.split("#");
        for (String item : split) {
            if (oConvertUtils.isNotEmpty(item)) {
                if (item.matches("[a-zA-Z0-9]+")) {
                    s2.add(item);
                } else if (item.equals("(")) {
                    s1.push(item);
                } else if (item.equals(")")) {
                    while (!s1.peek().equals("(")) {
                        s2.add(s1.pop());
                    }
                    s1.pop();
                } else {
                    while (s1.size() != 0 && Operation.getValue(s1.peek()) >= Operation.getValue(item)) {
                        s2.add(s1.pop());
                    }
                    s1.push(item);
                }
            }
        }
        while (s1.size() != 0) {
            s2.add(s1.pop());
        }
        return s2;
    }


    //完成对逆波兰表达式的运算
	/*
	 * 1)从左至右扫描，将3和4压入堆栈；
		2)遇到+运算符，因此弹出4和3（4为栈顶元素，3为次顶元素），计算出3+4的值，得7，再将7入栈；
		3)将5入栈；
		4)接下来是×运算符，因此弹出5和7，计算出7×5=35，将35入栈；
		5)将6入栈；
		6)最后是-运算符，计算出35-6的值，即29，由此得出最终结果
	 */

    public static String calculate(List<String> ls) {
        // 创建给栈, 只需要一个栈即可
        Stack<String> stack = new Stack<>();
        // 遍历 ls
        for (String item : ls) {
            // 这里使用正则表达式来取出数
            if (item.matches("\\d+(\\.\\d+)?")) { // 匹配的是多位数
                // 入栈
                stack.push(item);
            } else {
                // pop出两个数，并运算， 再入栈
                BigDecimal num2 = getBigDecimal(stack.pop());
                BigDecimal num1 = getBigDecimal(stack.pop());
                String res;
                if (item.equals("+")) {
                    res = String.valueOf(num1.add(num2));
                } else if (item.equals("-")) {
                    res = String.valueOf(num1.subtract(num2));
                } else if (item.equals("*")) {
                    res = String.valueOf(num1.multiply(num2));
                } else if (item.equals("/")) {
                    res = String.valueOf(num1.divide(num2, 4, BigDecimal.ROUND_DOWN));
                } else {
                    throw new RuntimeException("运算符有误");
                }
                //把res 入栈
                stack.push("" + res);
            }

        }
        //最后留在stack中的数据是运算结果
        return stack.pop();
    }


    public static BigDecimal getBigDecimal(String s) {
        if (oConvertUtils.isEmpty(s) || "/".equals(s) || "\\".equals(s)) {
            return new BigDecimal("0");
        }
        return new BigDecimal(s);
    }
}
