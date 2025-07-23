package com.trtm.sy.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;


/**
 * PDF工具
 *
 * @author zxl
 * @date 2022/8/5
 */
public class PdfUtil {

    static {
        // Velocity初始化
        Velocity.setProperty(RuntimeConstants.OUTPUT_ENCODING, StandardCharsets.UTF_8);
        Velocity.setProperty(RuntimeConstants.INPUT_ENCODING, StandardCharsets.UTF_8);
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
    }

    /**
     * 据模板生成pfd格式文件
     *
     * @param context      上下文对象
     * @param template     pdf模板
     * @param outputStream 生成ofd文件输出流
     */
    public static void pdfFile(Context context, String template, OutputStream outputStream) throws IOException {
        try (PdfWriter pdfWriter = new PdfWriter(outputStream)) {
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);

            ConverterProperties properties = new ConverterProperties();
            FontProvider fontProvider = new FontProvider();
            // 字体设置，解决中文不显示问题
            PdfFont sysFont = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
            fontProvider.addFont(sysFont.getFontProgram(), "UniGB-UCS2-H");
            properties.setFontProvider(fontProvider);

            Template pfdTemplate = Velocity.getTemplate(template, "UTF-8");
            StringWriter writer = new StringWriter();
            pfdTemplate.merge(context, writer);
            HtmlConverter.convertToPdf(writer.toString(), pdfDocument, properties);
            pdfDocument.close();
        }
    }


    /**
     * 传入对应的属性和html生成对应的pdf
     *
     * @param response 输出流
     * @param context  上下文对象
     * @param pdf      路径
     * @param fileName 文件名称，采用记录编号或者报告编号
     * @param html     html的文件
     * @return 返回文件名
     * @throws IOException
     */
    public static String getPDF(HttpServletResponse response, VelocityContext context, String pdf, String fileName, String html) throws IOException {
        FileOutputStream outputStream = null;
        try {
            fileName = fileName + ".pdf";
            File file = new File(pdf);
            if (!file.exists()) {
                file.mkdirs();
            }
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Content-Disposition", "inline; fileName=" + URLUtil.encode(fileName, CharsetUtil.CHARSET_UTF_8));
//        try(ServletOutputStream outputStream = response.getOutputStream()){
//        try (FileOutputStream outputStream = new FileOutputStream(new File("E:\\66.pdf"))) {
            outputStream = new FileOutputStream(new File(pdf + fileName));
            PdfUtil.pdfFile(context, "html/" + html, outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return pdf + fileName;
    }


    /**
     * 合并PDF文件
     *
     * @param sourceFilePaths 需要合并的PDF文件路径列表
     * @param destFilePath    合并后的新文件
     */
    public static String mergePdfFile(List<String> sourceFilePaths, String destFilePath) throws Exception{
        if (sourceFilePaths == null || sourceFilePaths.isEmpty() || destFilePath == null) {
            return null;
        }

        Document document = null;
        PdfCopy copy = null;
        OutputStream os = null;
        try {
            // 创建合并后的新文件的目录
            Path dirPath = Paths.get(destFilePath.substring(0, destFilePath.lastIndexOf(File.separator)));
            Files.createDirectories(dirPath);

            os = new BufferedOutputStream(new FileOutputStream(new File(destFilePath)));
            document = new Document(new PdfReader(sourceFilePaths.get(0)).getPageSize(1));
            copy = new PdfCopy(document, os);
            document.open();
            for (String sourceFilePath : sourceFilePaths) {
                // 如果PDF文件不存在，则跳过
                if (!new File(sourceFilePath).exists()) {
                    continue;
                }

                // 读取需要合并的PDF文件
                PdfReader reader = new PdfReader(sourceFilePath);
                // 获取PDF文件总页数
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
        } finally {
            if (copy != null) {
                copy.close();
            }
            if (document != null) {
                document.close();
            }
            if (os != null) {
                os.close();
            }
        }
        return destFilePath;
    }

}


