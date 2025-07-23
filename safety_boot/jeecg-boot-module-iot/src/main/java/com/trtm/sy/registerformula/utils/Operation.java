package com.trtm.sy.registerformula.utils;

public class Operation {
    private static int ADD = 1;
    private static int SUB = 1;
    private static int MUL = 2;
    private static int DIV = 2;
    private static int GH = 3;
    private static int CM = 4;

    public static int getValue(String operation) {
        int result = 0;
        switch (operation) {
            case "+":
                result = ADD;
                break;
            case "-":
                result = SUB;
                break;
            case "*":
                result = MUL;
                break;
            case "/":
                result = DIV;
                break;
            case "√":
                result = GH;
                break;
            case "^":
                result = CM;
                break;
            default:
//                System.out.println("不存在该运算符" + operation);
                break;
        }
        return result;
    }
}
