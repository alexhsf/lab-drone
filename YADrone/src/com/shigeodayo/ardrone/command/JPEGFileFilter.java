package com.shigeodayo.ardrone.command;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

final class JPEGFileFilter implements FTPFileFilter {

	// TODO: refactor into utility class
	public static boolean endsWithIgnoreCase(final String s, final String suffix) {
		final int l1 = s.length();
		final int l2 = suffix.length();
		final String ext = s.substring(l1 - l2);
		return suffix.equalsIgnoreCase(ext);
	}

	@Override
	public boolean accept(final FTPFile f) {
		final String nm = f.getName();
		final int t = f.getType();
		return t == FTPFile.FILE_TYPE && endsWithIgnoreCase(nm, ".jpg");
	}
}