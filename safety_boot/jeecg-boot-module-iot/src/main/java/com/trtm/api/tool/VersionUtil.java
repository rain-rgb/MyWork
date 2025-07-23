package com.trtm.api.tool;

import org.jeecg.common.exception.JeecgBootException;

public class VersionUtil {
    private static final String VERSION_ = "VERSION_";

    public VersionUtil() {
    }

    public static Integer iteration(Integer version) {
        version = version + 1;
        return version;
    }
    public static void checkVersion(Integer oldVersion, Integer newVersion,String ypbh) {
        if (null == oldVersion) {
            throw new JeecgBootException( "version属性不存在");
        } else if (null == newVersion) {
            throw new JeecgBootException( "修改数据时，入参请携带version");
        } else if (!oldVersion.equals(newVersion)) {
            throw new JeecgBootException( ypbh+"更新数据失败，数据已发生变更，请重新获取数据");
        }
    }
    public static void checkVersion(Integer oldVersion, Integer newVersion) {
        if (null == oldVersion) {
            throw new JeecgBootException( "version属性不存在");
        } else if (null == newVersion) {
            throw new JeecgBootException( "修改数据时，入参请携带version");
        } else if (!oldVersion.equals(newVersion)) {
            throw new JeecgBootException( "更新数据失败，数据已发生变更，请重新获取数据");
        }
    }

    public static void checkVersion(String ver1, String ver2) {
        Integer integer1 = Integer.parseInt(ver1);
        Integer integer2 = Integer.parseInt(ver2);
        checkVersion(integer1, integer2);
    }

    public static void main(String[] args) {
        checkVersion("1", "1");
    }
}
