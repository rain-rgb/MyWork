package com.trtm.sy.sycspz.utils;

import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Calendar;

class XSSFDateUtil extends DateUtil{
    protected static int absoluteDay(Calendar cal, boolean use1904windowing) {    
        return DateUtil.absoluteDay(cal, use1904windowing);    
    } 
}