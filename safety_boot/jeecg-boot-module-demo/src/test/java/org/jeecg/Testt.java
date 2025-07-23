package org.jeecg;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Testt {
    public static void main(String[] args) {
        int i = 0;
        int total = 241234;
        if (total % 2000 != 0) {
            i = total / 2000 + 1;
        } else {
            i = total / 2000;
        }
        if (i > 0) {
            for (int j = 1; j < i + 1; j++) {

                System.out.println("J:" + j + "------------------------------------------i:" + i);
            }

        }
    }
}