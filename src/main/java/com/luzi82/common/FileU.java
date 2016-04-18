package com.luzi82.common;

import java.io.File;

public class FileU {

	static public File getFile(String... strings) {
		File ret = null;
		for (String s : strings) {
			if (ret == null) {
				ret = new File(s);
			} else {
				ret = new File(ret, s);
			}
		}
		return ret;
	}

	static public File getFile(File f, String... strings) {
		File ret = f;
		for (String s : strings) {
			ret = new File(ret, s);
		}
		return ret;
	}

}
