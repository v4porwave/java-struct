package com.structure.common;

import com.structure.config.Config;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static String getInternalName(String className) {
        return className.replaceAll("/", ".");
    }

    public static boolean isServlet(List<String> classNameList) {
        for (String className : classNameList) {
            if (isTargetClass(className, Config.getConfig().getServletClass())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFilter(List<String> classNameList) {
        for (String className : classNameList) {
            if (isTargetClass(className, Config.getConfig().getFilterClass())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTargetClass(String className, List<String> targets) {
        if (Config.getConfig().getBlackClass().contains(className)) {
            return false;
        }
        if (targets.contains(className)) {
            return true;
        } else {
            if (className.startsWith("@")) {
                //如果是注解，则获取注解的注解
                String substring = className.substring(1);
                Class aClass = getClass(substring);
                if ( aClass != null ) {
                    for (Annotation annotation : aClass.getAnnotations()) {
                        if( isTargetClass("@%s".formatted(getInternalName(annotation.annotationType().getName())), targets) ) {
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            } else {
                //如果不是注解，则获取父类、父接口
                Class aClass = getClass(className);
                if (aClass != null) {
                    //先判断父类
                    if (aClass.getSuperclass() != null && isTargetClass(aClass.getSuperclass().getName(), targets)) {
                        return true;
                    }
                    //再判断接口
                    if (aClass.getInterfaces() != null) {
                        for (Class clazz : aClass.getInterfaces()) {
                            if (isTargetClass(clazz.getName(), targets)) {
                                return true;
                            }
                        }
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static Class getClass(String className) {
        try {
            return Class.forName(className, false, Config.getConfig().getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static List<String> getClassInfo(ClassReader classReader) {
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        List<String> result = new ArrayList<>();

        //父类
        if (classNode.superName != null) {
            result.add(getInternalName(classNode.superName));
        }

        //接口
        if (classNode.interfaces != null && !classNode.interfaces.isEmpty()) {
            result.addAll(classNode.interfaces);
        }

        //注解
        if (classNode.visibleAnnotations != null && !classNode.visibleAnnotations.isEmpty()) {
            for (AnnotationNode visibleAnnotation : classNode.visibleAnnotations) {
                result.add("@%s".formatted(Util.getInternalName(visibleAnnotation.desc.substring(1, visibleAnnotation.desc.length() - 1))));
            }
        }
        return result;
    }

}
