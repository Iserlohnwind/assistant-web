package com.momassistant.utils;

public final class WrapUtils {

    public static int unwrap(Integer ori, int defaultValue) {
        if (ori == null) {
            return defaultValue;
        }
        return ori;
    }

    public static int unwrap(Short ori, int defaultValue) {
        if (ori == null) {
            return defaultValue;
        }
        return ori;
    }
    public static long unwrap(Long ori, long defaultValue) {
        if (ori == null) {
            return defaultValue;
        }
        return ori;
    }

    public static double unwrap(Double ori, double defaultValue) {
        if (ori == null) {
            return defaultValue;
        }
        return ori;
    }

}
