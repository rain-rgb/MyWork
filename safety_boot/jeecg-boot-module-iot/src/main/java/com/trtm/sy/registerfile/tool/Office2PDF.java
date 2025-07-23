//package com.trtm.sy.registerfile.tool;
//
//import com.artofsolving.jodconverter.DocumentConverter;
//import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
//import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
//import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
//import org.apache.commons.lang3.StringUtils;
//import org.jodconverter.OfficeDocumentConverter;
//import org.jodconverter.office.DefaultOfficeManagerBuilder;
//import org.jodconverter.office.OfficeException;
//import org.jodconverter.office.OfficeManager;
//
//import java.io.File;
//import java.net.ConnectException;
//import java.util.regex.Pattern;
//
///**
// * doc docx ex.. ex..x ppt pptx
// */
//public final class Office2PDF {
//    private Office2PDF() {
//    }
//
//    /**
//     * 将office格式的文件转为pdf
//     *
//     * @param sourceFilePath 源文件路径
//     * @return
//     */
//    public static File openOfficeToPDF(String sourceFilePath, String destFilePath) {
//        return office2pdf(sourceFilePath, destFilePath);
//    }
//
//    /**
//     * 将office文档转换为pdf文档
//     *
//     * @param sourceFilePath 原文件路径
//     * @return
//     */
//    public static File office2pdf(String sourceFilePath, String destFilePath) {
//        OpenOfficeConnection connection = null;
//        try {
//            if (StringUtils.isEmpty(sourceFilePath)) {
//                // 打印日志...
//                return null;
//            }
//            File sourceFile = new File(sourceFilePath);
//            if (!sourceFile.exists()) {
//                // 打印日志...
//                return null;
//            }
//            String after_convert_file_path = getAfterConverFilePath(sourceFilePath, destFilePath);
//            connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
//            connection.connect();
//            //由于.xls文件无法预览，改为StreamOpenOfficeDocumentConverter
////            DocumentConverter converter = new CustomDocumentConverter(connection);
//            DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
//            return convertFile(sourceFile, after_convert_file_path, sourceFilePath, converter);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("转换异常");
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 获取转换后文件存放的路径
//     *
//     * @param sourceFilePath 源文件
//     * @param destFilePath   目的路径
//     * @return
//     */
//    public static String getAfterConverFilePath(String sourceFilePath, String destFilePath) {
//        // 截取源文件文件名
//        String sourceFileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("\\") + 1);
//
//        if (StringUtils.isEmpty(destFilePath)) {
//            destFilePath = "D:\\suxin\\pdfFile";
//        }
//        return destFilePath + "\\" + sourceFileName.replaceAll("\\." + FileUtil.getFileSuffix(sourceFileName), ".pdf");
//
//    }
//
//    /**
//     * 转换文件
//     *
//     * @param sourceFile              原文件
//     * @param after_convert_file_path 转换后存放位置
//     * @param sourceFilePath          原文件路径
//     * @param converter               转换器
//     * @return
//     * @throws ConnectException
//     */
//    public static File convertFile(File sourceFile, String after_convert_file_path, String sourceFilePath,
//                                   DocumentConverter converter) throws OfficeException, ConnectException {
//        File outputFile = new File(after_convert_file_path);
//        if (!outputFile.getParentFile().exists()) {
//            // 如果上级目录不存在也就是E:/pdfFile这个文件夹不存在则创建一个
//            outputFile.getParentFile().mkdirs();
//        }
//        converter.convert(sourceFile, outputFile);
//        return outputFile;
//    }
//
//    public static File convertFile(File sourceFile, String after_convert_file_path, OfficeDocumentConverter converter) throws OfficeException, ConnectException {
//        File outputFile = new File(after_convert_file_path);
//        if (!outputFile.getParentFile().exists()) {
//            // 如果上级目录不存在也就是E:/pdfFile这个文件夹不存在则创建一个
//            outputFile.getParentFile().mkdirs();
//        }
//        converter.convert(sourceFile, outputFile);
//        return outputFile;
//    }
//
//
//    /**
//     * 转换文件
//     *
//     * @return
//     */
//    /*public static File convertFile(File sourceFile,
//                           String after_convert_file_path,String sourceFilePath,OfficeDocumentConverter converter) throws OfficeException {
//        File outputFile = new File(after_convert_file_path);
//        if(!outputFile.getParentFile().exists()){
//            //如果上级目录不存在也就是E:/pdfFile这个文件夹不存在则创建一个
//            outputFile.getParentFile().mkdirs();
//        }
//            converter.convert(sourceFile,outputFile);
//        return outputFile;
//    }*/
//    public static OfficeManager getOfficeManager() {
//        DefaultOfficeManagerBuilder builder = new DefaultOfficeManagerBuilder();
//        builder.setOfficeHome(getOfficeHome());
//        OfficeManager officeManager = builder.build();
//        try {
//            officeManager.start();
//        } catch (OfficeException e) {
//            //打印日志
//            System.out.println("start openOffice Fail!");
//            e.printStackTrace();
//        }
//        return officeManager;
//    }
//
//    /**
//     * 获取转换后文件存放的路径
//     *
//     * @param sourceFilePath 源文件
//     * @return
//     */
//    public static String getAfterConverFilePath(String sourceFilePath) {
//        //截取源文件文件名
//        String sourceFileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("\\") + 1);
//        return "C:/yzjg/pdfFile/" + sourceFileName.replaceAll("\\." + FileUtil.getFileSuffix(sourceFileName), ".pdf");
//    }
//
//    /**
//     * 获取openOffice的安装目录
//     *
//     * @return
//     */
//    public static String getOfficeHome() {
//        String osName = System.getProperty("os.name");
//        if (Pattern.matches("Windows.*", osName)) {
//            return "C:/Program Files (x86)/OpenOffice 4";
//        } else if (Pattern.matches("Linux.*", osName)) {
//            return "/usr/temp";
//        } else if (Pattern.matches("Mac.*", osName)) {
//            return "/Application/openOfficeSoft";
//        }
//        return null;
//    }
//
//    /**
//     * 将office格式的文件转为pdf
//     *
//     * @param sourceFilePath 源文件路径
//     * @return
//     */
////    public static String openOfficeToPDFapp(String sourceFilePath) {
////        return office2pdfapp(sourceFilePath);
////    }
////
////    /**
////     * 将office文档转换为pdf文档
////     *
////     * @param sourceFilePath 原文件路径
////     * @return
////     */
////    public static String office2pdfapp(String sourceFilePath) {
////        OpenOfficeConnection connection = null;
////        try {
////            if (StringUtils.isEmpty(sourceFilePath)) {
////                // 打印日志...
////                return null;
////            }
////            File sourceFile = new File(sourceFilePath);
////            if (!sourceFile.exists()) {
////                // 打印日志...
////                return null;
////            }
////            String after_convert_file_path = getAfterConverFilePathapp(sourceFilePath);
////            connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
////            connection.connect();
////            DocumentConverter converter = new CustomDocumentConverter(connection);
////            convertFile(sourceFile, after_convert_file_path, sourceFilePath, converter);
////            return after_convert_file_path;
////
////        } catch (Exception e) {
////            e.printStackTrace();
////            System.out.println("转换异常");
////        } finally {
////            if (connection != null) {
////                connection.disconnect();
////            }
////        }
////        return null;
////    }
//
//    /**
//     * 获取转换后文件存放的路径
//     *
//     * @param sourceFilePath 源文件
//     * @return
//     */
//    public static String getAfterConverFilePathapp(String sourceFilePath) {
//        // 截取源文件文件名
//        String sourceFileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("\\") + 1);
//        return "D:\\srpyPDF\\pdfFileapp\\"
//                + sourceFileName.replaceAll("\\." + FileUtil.getFileSuffix(sourceFileName), ".pdf");
//    }
//
//
//    public static File sqOffice2pdf(String sourceFilePath, String destFilePath) {
//        OpenOfficeConnection connection = null;
//        try {
//            if (StringUtils.isEmpty(sourceFilePath)) {
//                // 打印日志...
//                return null;
//            }
//            File sourceFile = new File(sourceFilePath);
//            if (!sourceFile.exists()) {
//                // 打印日志...
//                return null;
//            }
//            String after_convert_file_path = getAfterConverFilePath(sourceFilePath, destFilePath);
//            DefaultOfficeManagerBuilder builder = new DefaultOfficeManagerBuilder();
//            builder.setOfficeHome("C:/Program Files (x86)/OpenOffice 4");
//            OfficeManager officeManager = builder.build();
//            officeManager.start();
//            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
//            return convertFile(sourceFile, after_convert_file_path, converter);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("转换异常");
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
//        return null;
//    }
//}
