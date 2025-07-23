package com.trtm.iot.trkongdaogjdwm.controller;

import cn.hutool.json.JSONObject;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

public class UnCompressUtil {
    private static final Logger logger = Logger.getLogger(UnCompressUtil.class.getCanonicalName());

    @Autowired
    private IPushandreturnService pushandreturnService;
    /**
     * 解压rar
     *
     * @param file rar
     * @param extractPath 解压路径
     */
    public static void unCompress(File file, String extractPath) {
        try{
            RandomAccessFile randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "r");
            IInArchive archive = SevenZip.openInArchive(null,  new RandomAccessFileInStream(randomAccessFile));
            // 解压⽂件路径
            File extractDir = new File(extractPath);
            if (!extractDir.isDirectory()) {
                extractDir.mkdir();
            }
            int[] in = new int[archive.getNumberOfItems()];
            for(int i=0;i<in.length;i++){
                in[i] = i;
            }
            archive.extract(in, false, new ExtractCallback(archive, extractDir.getAbsolutePath()));
            archive.close();
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static class ExtractCallback implements IArchiveExtractCallback {
        private final IInArchive inArchive;

        private final String extractPath;

        public ExtractCallback(IInArchive inArchive, String extractPath) {
            this.inArchive = inArchive;
            if (!extractPath.endsWith("/") && !extractPath.endsWith("\\")) {
                extractPath += File.separator;
            }
            this.extractPath = extractPath;
        }

        @Override
        public void setTotal(long total) {

        }

        @Override
        public void setCompleted(long complete) {

        }

        @Override
        public ISequentialOutStream getStream(int index, ExtractAskMode extractAskMode) throws SevenZipException {
            return data -> {
                String filePath = inArchive.getStringProperty(index, PropID.PATH);
                FileOutputStream fos = null;
                try {
                    File path = new File(extractPath + filePath);

                    if (!path.getParentFile().exists()) {
                        path.getParentFile().mkdirs();
                    }

                    if (!path.exists()) {
                        path.createNewFile();
                    }
                    fos = new FileOutputStream(path, true);
                    fos.write(data);
                } catch (IOException e) {
                    logger.log(null, "IOException while extracting " + filePath);
                } finally {
                    try {
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    } catch (IOException e) {
                        logger.log(null, "Could not close FileOutputStream", e);
                    }
                }
                return data.length;
            };
        }

        @Override
        public void prepareOperation(ExtractAskMode extractAskMode) {

        }

        @Override
        public void setOperationResult(ExtractOperationResult extractOperationResult) {
        }

    }

}
