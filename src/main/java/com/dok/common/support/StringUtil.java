package com.dok.common.support;

public class StringUtil {

    public static final String EMTY = "";

    private StringUtil() {
        // no instances
    }

    public static boolean isEmpty(String val) {
        return val == null || val.isEmpty();
    }

    public static boolean isNotEmpty(String val) {
        return val != null && !val.isEmpty();
    }

    public static String nvl(Object val) {
        return val == null ? EMTY : val.toString();
    }

    public static String lpad(String val, int len) {
        return pad(val, len, ' ', true);
    }

    public static String lpad(String val, int len, char fill) {
        return pad(val, len, fill, true);
    }

    public static String rpad(String val, int len) {
        return pad(val, len, ' ', false);
    }

    public static String rpad(String val, int len, char fill) {
        return pad(val, len, fill, false);
    }

    private static String pad(String val, int len, char fill, boolean leftPad) {
        if (val == null || val.isEmpty()) {
            return val;
        }

        byte[] b = val.getBytes();
        int margin = len - b.length;
        if(margin > 0) {
            StringBuilder sb = new StringBuilder();
            if(leftPad) {
                for (int i = 0; i < margin; i++) {
                    sb.append(fill);
                }
                sb.append(val);
            } else {
                sb.append(val);
                for (int i = 0; i < margin; i++) {
                    sb.append(fill);
                }
            }
            return sb.toString();
        } else if(margin == 0) {
            return val;
        } else {
            return val.substring(0, len);
        }
    }

	public static String substringFrom(String str, char fromSeperator) {
		if (str == null) {
			return str;
		}
		int pos = str.indexOf(fromSeperator);
		if (pos < 0) {
			return str;
		} else {
			return str.substring(pos+1);
		}
	}

	public static String substringTo(String str, char toSeperator) {
		if (str == null) {
			return str;
		}
		int pos = str.lastIndexOf(toSeperator);
		if (pos < 0) {
			return str;
		} else {
			return str.substring(0, pos);
		}
	}

}
