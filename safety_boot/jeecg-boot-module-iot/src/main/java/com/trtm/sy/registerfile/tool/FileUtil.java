package com.trtm.sy.registerfile.tool;



import org.jeecg.common.exception.JeecgBootException;

import java.io.*;

/**
 * @author wh
 * @Description
 * @program core
 * @Create by H
 */
public class FileUtil {

    /**
     * 复制文件
     *
     * @param source 源文件
     * @param target 目标文件
     * @param cover  当目标文件存在时是否覆盖 值为true时覆盖 值为false时抛出 目标文件以已存在
     * @throws IOException
     */
    private static void copyFileUsingStream(File source, File target, boolean cover) throws IOException {

        if (!source.canExecute()) {
            throw new JeecgBootException("源文件不存在");
        }

        if (!cover && target.canExecute()) {
            throw new JeecgBootException("目标文件以已存在");
        }
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }

        if (!target.canExecute()) {
            throw new JeecgBootException("文件复制失败,未找到对应生成的文件信息！");
        }

    }


    /**
     * 替换文件中的内容
     *
     * @param source     修改的文件
     * @param oldContent 修改之前的内容
     * @param newContent 修改之后的内容
     */
    private static void setText(File source, String oldContent, String newContent) {
        BufferedReader br = null;
        PrintWriter pw = null;
        StringBuffer buff = new StringBuffer();
        String line = System.getProperty("line.separator");
        try {
            br = new BufferedReader(new FileReader(source));
            for (String str = br.readLine(); str != null; str = br.readLine()) {
                if (str.contains(oldContent))
                    str = str.replaceAll(oldContent, newContent);
                buff.append(str + line);
            }
            pw = new PrintWriter(new FileWriter(source), true);
            pw.println(buff);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new JeecgBootException("源文件不存在!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new JeecgBootException("系统异常,替换文件内容失败!");
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (pw != null)
                pw.close();
        }
    }

    public static String getFileSuffix(String fileName){
        if(StringUtils.isEmpty(fileName) || fileName.lastIndexOf(".")<0 ){
            return "error";
        }
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }


}
