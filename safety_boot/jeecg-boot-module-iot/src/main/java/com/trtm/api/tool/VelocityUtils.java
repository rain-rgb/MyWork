package com.trtm.api.tool;

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

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Properties;

public class VelocityUtils {


    /**
     * html转pdf
     */
    public static void convertToPdf(String fileName, StringWriter stringWriter, HttpServletResponse response) throws IOException {
        // 设置请求流
        FileUtils.setAttachmentResponseHeader(response,fileName);
        // 将获取到的velocity流放图输入流中
        InputStream inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes(Charset.defaultCharset()));
        // 构建请求输出流
        PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
        // 构建pdf元素
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        //设置为A4大小
        pdfDocument.setDefaultPageSize(PageSize.A4);
        //添加中文字体支持
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new FontProvider();
        PdfFont sysFont = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
        fontProvider.addFont(sysFont.getFontProgram(), "UniGB-UCS2-H");
        properties.setFontProvider(fontProvider);
        HtmlConverter.convertToPdf(inputStream, pdfDocument, properties);
        pdfWriter.close();
        pdfDocument.close();
    }

    /**
     * 获取html流
     * @param context   velocity变量
     * @param vmPath    路径
     * @return  流
     * @throws IOException /
     */
    public static StringWriter genHtml(VelocityContext context, String vmPath) throws IOException {
        VelocityUtils.initVelocity();
        Template tpl = Velocity.getTemplate(vmPath, "UTF-8");
        StringWriter sw = new StringWriter();
        // 5、合并数据到模板
        tpl.merge(context, sw);
        // 6、释放资源
        sw.close();
        return sw;
    }

    /**
     * 初始化vm方法
     */
    public static void initVelocity()
    {
        Properties p = new Properties();
        try
        {
            // 加载classpath目录下的vm文件
            p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            // 定义字符集
            p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
