package com.trtm.sy.sycspz.utils;

import com.xkcoding.http.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;

public class ExcelUtil {
	
    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";  
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";  
    public static final String EMPTY = "";  
    public static final String POINT = ".";  
    public static SimpleDateFormat sdf =   new SimpleDateFormat("yyyy/MM/dd");


    /** 
     * 获得path的后缀名 
     * @param path 
     * @return 
     */  
    public static String getPostfix(String path){  
        if(path==null || EMPTY.equals(path.trim())){  
            return EMPTY;  
        }  
        if(path.contains(POINT)){  
            return path.substring(path.lastIndexOf(POINT)+1,path.length());  
        }  
        return EMPTY;  
    }
    
    /** 
     * 单元格格式 
     * @param hssfCell 
     * @return 
     */  
    @SuppressWarnings({ "static-access", "deprecation" })  
    public static String getHValue(HSSFCell hssfCell){
         if (hssfCell.getCellType() == BOOLEAN) {
             return String.valueOf(hssfCell.getBooleanCellValue());
         } else if (hssfCell.getCellType() == NUMERIC) {
             String cellValue = "";
             if(HSSFDateUtil.isCellDateFormatted(hssfCell)){
                 Date date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());
                 cellValue = sdf.format(date);
             }else{
                 DecimalFormat df = new DecimalFormat("#.##");
                 cellValue = df.format(hssfCell.getNumericCellValue());
                 String strArr = cellValue.substring(cellValue.lastIndexOf(POINT)+1,cellValue.length());
                 if(strArr.equals("00")){
                     cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                 }
             }
             return cellValue;
         } else {
            return String.valueOf(hssfCell.getStringCellValue());
         }
    }

    /**
     * 单元格格式
     * @param xssfCell
     * @return
     */
    public static String getXValue(XSSFCell xssfCell){
         if (xssfCell.getCellType() == BOOLEAN) {
             return String.valueOf(xssfCell.getBooleanCellValue());
         } else if (xssfCell.getCellType() == NUMERIC) {
             String cellValue = "";  
             if(XSSFDateUtil.isCellDateFormatted(xssfCell)){  
                 Date date = XSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());  
                 cellValue = sdf.format(date);  
             }else{  
                 DecimalFormat df = new DecimalFormat("#.##");  
                 cellValue = df.format(xssfCell.getNumericCellValue());  
                 String strArr = cellValue.substring(cellValue.lastIndexOf(POINT)+1,cellValue.length());  
                 if(strArr.equals("00")){  
                     cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));  
                 }    
             }  
             return cellValue;  
         } else {  
            return String.valueOf(xssfCell.getStringCellValue());  
         }  
    }   
    
    
    
    
	@SuppressWarnings("unused")
	public static JSONArray ExcelToJson(List<ArrayList<String>> listArr, Integer index, String[] field, JSONObject other, String[] noEmpty) {
		JSONArray json = new JSONArray();
		try {
			List<String> noEmptyList = new ArrayList<String>(Arrays.asList(noEmpty));
			ifor:for(int i = 1;i<listArr.size();i++){
				ArrayList<String> arrStr = listArr.get(i);
				JSONObject jobj = new JSONObject();
				jobj.putAll(other);
				for(int j = 0; j < field.length; j++){
					String str = arrStr.get(j);
					if(StringUtil.isNotEmpty(str)){
						jobj.put(field[j],str);
//						if (noEmptyList.indexOf(field[j]) >= 0) {
//							continue ifor;
//						}else {
//							jobj.put(field[j],str);
//						}
					}
				}
				json.add(jobj);
			}
		} catch (Exception e) {
		}
		return json;
	}
}

