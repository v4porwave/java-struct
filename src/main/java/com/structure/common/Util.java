package com.structure.common;

import com.structure.config.Config;

public class Util {

    public static String getInternalName(String className) {
        return className.replaceAll("/", ".");
    }

    public static boolean isServlet(String className) {
        if (className.equals("java.lang.Object")) {
            return false;
        }
        if (Config.getConfig().getServletClass().contains(className)) {
            return true;
        } else {
            Class aClass = getClass(className);
            if (aClass != null) {
                return isServlet(aClass.getSuperclass().getName());
            } else {
                return false;
            }
        }
    }

    public static Class getClass(String className) {
        try {
            return Class.forName(className, false, Config.getConfig().getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static boolean isFilter(String className) {
        if (className.equals("java.lang.Object")) {
            return false;
        }
        if (Config.getConfig().getFilterClass().contains(className)) {
            return true;
        } else {
            Class aClass = getClass(className);
            if (aClass != null) {
                return isServlet(aClass.getSuperclass().getName());
            } else {
                return false;
            }
        }
    }

}
