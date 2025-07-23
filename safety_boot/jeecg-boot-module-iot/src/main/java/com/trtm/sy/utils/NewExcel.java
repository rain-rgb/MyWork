package com.trtm.sy.utils;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

public class NewExcel {
    /**
     * 导出Excel加边框样式
     */
    public static void exportNewExcel(List<?> list, String title, String sheetName, Class<?> entity, String fileName, HttpServletResponse response) {
        ExportParams exportParams = new ExportParams(title, sheetName);
        //添加样式
        exportParams.setStyle(ExcelExportStylerUtil.class);
        //冻结表头
        exportParams.setCreateHeadRows(true);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, entity, list);

        if (workbook == null) {
            throw new RuntimeException("Excel表导出失败");
        }
        OutputStream outputStream = null;
        BufferedOutputStream buffOutputStream = null;
        try {
            // 指定下载的文件名--设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            //response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setCharacterEncoding("UTF-8");
            // 导出Excel
            outputStream = response.getOutputStream();
            buffOutputStream = new BufferedOutputStream(outputStream);
            workbook.write(buffOutputStream);
            buffOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (buffOutputStream != null) {
                    buffOutputStream.close();
                }
                if (workbook != null) {
                    workbook.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
