package com.trtm.sy.registerfile.file;


/**
 * 文件名大小限制异常类
 *
 * @author zww
 */
public class FileSizeLimitExceededFeilException extends BaseFeilException {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededFeilException(long defaultMaxSize)
    {
        super("upload.exceed.maxSize", new Object[] { defaultMaxSize });
    }
}
