package com.trtm.sy.registerfile.file;


/**
 * 文件名称超长限制异常类
 *
 * @author
 */
public class FileNameLengthLimitExceededFeilException extends BaseFeilException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededFeilException(int defaultFileNameLength)
    {
        super("upload.filename.exceed.length", new Object[] { defaultFileNameLength });
    }
}
