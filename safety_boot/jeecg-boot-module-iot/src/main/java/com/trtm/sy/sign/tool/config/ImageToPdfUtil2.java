package com.trtm.sy.sign.tool.config;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class ImageToPdfUtil2 {
    /**
     * Description: 将图片的base64转成PDF的base64编码
     * @param  bas64ImageStr  二维码图片base64
     */
    public String imageBase64ToPdfBase64(String bas64ImageStr){
        String pdfBase64 = "";//PDF的base64编码

        try{
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String time=sdf.format(date);
            //临时pdf文件路径  D:\app
            //图片名称流水号
            String custOrderNbr = time;

            //1.根据微厅返回的请求参数生成二维码 base64 编码
            bas64ImageStr = bas64ImageStr.replaceAll("\r|\n", "");

            //2.获取要存储文件的路径,即获取src资源文件编译后的路径（即classes路径）
            String url = this.getClass().getClassLoader().getResource("").getPath();
            //对路劲进行拼接添加
            String serviceImgPath = url + "serviceImg/";//服务器上存放二维码图片的路径
            String servicePdfPath = url + "servicePdf/";//服务器上存放二维码PDF的路径

            //3.判断服务器是否存在此文件夹,不存在则新建
            File file1 =new File(serviceImgPath);
            File file2 =new File(servicePdfPath);
            if(!file1.exists()  && !file1.isDirectory()) {
                file1.mkdir();
            }
            if(!file2.exists()  && !file2.isDirectory()) {
                file2.mkdir();
            }
            //4.二维码图片的文件名字和最终保存的二维码文件路径
            String fileImageName = custOrderNbr+"_phone.png";//二维码图片路径名字
            String filePdfName = custOrderNbr+"_phone.pdf";//PDF图片路径名字
            String lastImagePath = serviceImgPath+fileImageName;//最终二维码图片存放的路径
            String lastPdfPath = servicePdfPath+filePdfName;//最终二维码PDF存放的路径

            //5.首先保存二维码图片
            Base64ToImage(bas64ImageStr,lastImagePath);

            //6.然后把二维码图片转成PDF二维码文件进行储存
            ImgChangePDF(lastImagePath,lastPdfPath);

            //7.最后将PDF转成base64,PDF的base64才是最终能推送到签字版的
            File file3 =new File(lastPdfPath);
            pdfBase64 = PDFToBase64(file3);
            pdfBase64 = pdfBase64.replaceAll("\r|\n", "");

            //8.需要删除创建的临时文件
            File imagefile = new File(lastImagePath);
            if(imagefile.exists()){
                imagefile.delete();
            }
            File pdffile = new File(lastPdfPath);
            if(pdffile.exists()){
                pdffile.delete();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return pdfBase64;
    }

    /**
     * pdf 转64位编码
     * @param file
     * @return
     */
    public static String PDFToBase64(File file) {
        BASE64Encoder encoder = new BASE64Encoder();
        FileInputStream fin =null;
        BufferedInputStream bin =null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout =null;
        try {
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while(len != -1){
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            //刷新此输出流并强制写出所有缓冲的输出字节
            bout.flush();
            byte[] bytes = baos.toByteArray();
            return encoder.encodeBuffer(bytes).trim();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * base64转image
     * @param imgStr
     * @param imgFilePath
     * @return
     */

    //base64 转成图片
    public static boolean Base64ToImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        // 解密
        try {
            // 解密
            Base64.Decoder decoder = Base64.getDecoder();
            // 去掉base64前缀 data:image/png;base64,
            imgStr = imgStr.substring(imgStr.indexOf(",", 1) + 1, imgStr.length());
            byte[] b = decoder.decode(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 保存图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            // 返回图片的相对路径 = 图片分类路径+图片名+图片后缀
            return true;
        } catch (IOException e) {
             e.printStackTrace();
             return false;
        }

    }



    /**
     * 将图片转换成PDF
     * @param imageUrl        二维码图片路径 可以调用 FileUtil.getFileList() 方法
     * @param target        PDF的名字和位置
     */
    public static void ImgChangePDF(String imageUrl, String target) {
        //创建一个文档对象
        Document doc = new Document();
        try {
            //定义输出文件的位置
            PdfWriter.getInstance(doc, new FileOutputStream(target));
            //开启文档
            doc.open();

                //路径
                Image img = Image.getInstance(imageUrl);
                //获得宽高
                Float h = img.getHeight();
                Float w = img.getWidth();
                //统一压缩
                Integer percent = getPercent(h, w);
                //图片居中
                img.setAlignment(Image.MIDDLE);
                //百分比显示图
                img.scalePercent(percent);
                //设置高和宽的比例
                doc.add(img);

            // 关闭文档
            if(doc != null){
                doc.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    /**
     * 压缩
     * @param
     */
    public static Integer getPercent(Float h,Float w)
    {
        Integer g=0;
        Float g2=0.0f;
        g2=480/w*100;
        g=Math.round(g2);
        return g;
    }
    /**
     * @Param source 源文件
     * @Param target 转换后文件
     * @Description 将PDF转为A4格式
     * @Date: 2021/4/25
     **/
    public static void convert(String source, String target) {
        try {
            PdfReader pdfReader = new PdfReader(source);
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(target));
            doc.open();
            PdfContentByte cb = writer.getDirectContent();
            for(int i = 1; i <= pdfReader.getNumberOfPages(); i++){
                PdfImportedPage page = writer.getImportedPage(pdfReader, i);
                float width = page.getWidth();
                float height = page.getHeight();
                if(height > width) {
                    //横向
                    doc.setPageSize(PageSize.A4);
                    doc.newPage();
                    //计算比例
                    float widthScale = getWidthScale(width);
                    float heightScale = getHeightScale(height);
                    //addTemplate方法中有6个float类型的参数，是通过二维图像仿射变换得到的
                    //cb.addTemplate(page, new AffineTransform(widthScale, 0, 0, heightScale,0,0));
                    //二维图像仿射变换:https://www.cnblogs.com/v2m_/archive/2013/05/09/3070187.html
                    cb.addTemplate(page, widthScale, 0, 0, heightScale,0,0);
                } else {
                    //纵向
                    doc.setPageSize(new com.itextpdf.text.Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth()));
                    doc.newPage();
                    float widthScale = getWidthScale(height);
                    float heightScale = getHeightScale(width);
                    cb.addTemplate(page, widthScale, 0, 0, heightScale,0,0);
                }
            }
            doc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private static float getWidthScale(float width) {
        float scale = PageSize.A4.getWidth() / width;
        return scale;
    }

    private static float getHeightScale(float height) {
        float scale = PageSize.A4.getHeight() / height;
        return scale;
    }

}
