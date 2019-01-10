package com.itmuch.cloud.util;

/**
 * @author Robbie
 * @date 2017年8月19日 下午11:44:30
 * @describe TODO
 */
public class StringUtil {

	public static String trimFileName(String srcValue) {
		char[] charsNotForFileName = { '\\', '/', ':', '*', '?', '"', '>', '<', '|' };
		return trimFileName(srcValue, charsNotForFileName);
	}

	public static String trimFileName(String srcValue, char[] chars) {
		if (srcValue == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < srcValue.length(); i++) {
			boolean isIllegalChar = false;
			char current = srcValue.charAt(i);
			for (int k = 0; k < chars.length; k++) {
				if (current == chars[k]) {
					isIllegalChar = true;
					break;
				}
			}
			if (isIllegalChar) {
				buffer.append(" ");
			} else {
				buffer.append(current);
			}
		}

		return buffer.toString();
	}
}
