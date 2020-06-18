package com.elikill58.api;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public final class UniversalUtils {

	public static String truncate(String tooLongString, int limit) {
		char[] chars = tooLongString.toCharArray();

		if (chars.length < limit)
			return tooLongString;

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < limit; i++) {
			builder.append(chars[i]);
		}

		return builder.toString();
	}

	/**
	 * Check if a string is a number
	 * 
	 * @param obj to check if it's an integer
	 * @return true if it's an integer
	 */
	public static boolean isInteger(String obj) {
		try {
			Integer.parseInt(obj);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static <T> String toStringWithSplitter(List<T> list, String sp) {
		String s = "";
		for (T t : list) {
			s += t.toString() + sp;
		}
		return s;
	}

	/**
	 * Transform a size in a human readable message
	 * 
	 * @param size of a file
	 * @return a visual message of the size
	 */
    public static String getSize(long size) {
        String s = "";
        double kb = size / 1024;
        double mb = kb / 1024;
        double gb = mb / 1024;
        double tb = gb / 1024;
        if(size < 1024L) {
            s = size + " Bytes";
        } else if(size >= 1024 && size < (1024L * 1024)) {
            s =  String.format("%.2f", kb) + " KB";
        } else if(size >= (1024L * 1024) && size < (1024L * 1024 * 1024)) {
            s = String.format("%.2f", mb) + " MB";
        } else if(size >= (1024L * 1024 * 1024) && size < (1024L * 1024 * 1024 * 1024)) {
            s = String.format("%.2f", gb) + " GB";
        } else if(size >= (1024L * 1024 * 1024 * 1024)) {
            s = String.format("%.2f", tb) + " TB";
        }
        return s;
    }

	public static List<String> getClasseNamesInPackage(String jarName, String packageName) {
		ArrayList<String> classes = new ArrayList<>();

		packageName = packageName.replaceAll("\\.", "/");
		try {
			JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
			JarEntry jarEntry;
			while (true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				if ((jarEntry.getName().startsWith(packageName)) && (jarEntry.getName().endsWith(".class"))) {
					classes.add(jarEntry.getName().replaceAll("/", "\\."));
				}
			}
			jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

	public static OS os = null;

	public static OS getOs() {
		if (os == null)
			os = OS.getOs();
		return os;
	}

	public enum OS {
		WINDOWS(StandardCharsets.ISO_8859_1), MAC(StandardCharsets.UTF_16), LINUX(StandardCharsets.UTF_8), SOLARIS(
				StandardCharsets.UTF_8), OTHER(StandardCharsets.UTF_16);

		private Charset ch;

		OS(Charset ch) {
			this.ch = ch;
		}

		public Charset getCharset() {
			return ch;
		}

		private static OS getOs() {
			String os = System.getProperty("os.name").toLowerCase();
			if (isWindows(os))
				return WINDOWS;
			else if (isMac(os))
				return MAC;
			else if (isUnix(os))
				return LINUX;
			else if (isSolaris(os))
				return SOLARIS;
			else
				return OTHER;
		}

		private static boolean isWindows(String OS) {
			return (OS.indexOf("win") >= 0);
		}

		private static boolean isMac(String OS) {
			return (OS.indexOf("mac") >= 0);
		}

		private static boolean isUnix(String OS) {
			return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
		}

		private static boolean isSolaris(String OS) {
			return (OS.indexOf("sunos") >= 0);
		}
	}
}
