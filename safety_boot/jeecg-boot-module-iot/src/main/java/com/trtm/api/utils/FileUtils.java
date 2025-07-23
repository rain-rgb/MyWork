package com.trtm.api.utils;

import java.io.InputStream;
import java.lang.reflect.Field;

public class FileUtils {

	public FileUtils() {
	}

	public static String getExtName(String fileName) {
		if (fileName == null) {
			return null;
		} else {
			int extIndex = fileName.lastIndexOf(".");
			if (extIndex > 0 && extIndex + 1 < fileName.length()) {
				String ext = fileName.substring(extIndex + 1).toLowerCase();
				return ext;
			} else {
				return null;
			}
		}
	}

	public static String getFileName(InputStream inputStream) {
		if (inputStream == null) {
			return null;
		} else {
			try {
				Field field = inputStream.getClass().getDeclaredField("path");
				field.setAccessible(true);
				return  (String) field.get(inputStream);
			} catch (Exception e) {
				return null;
			}
		}
	}
}
