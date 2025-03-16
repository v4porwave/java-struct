package com.structure;

import java.net.URL;
import java.net.URLClassLoader;

public class StructClassLoader extends URLClassLoader {

    private URL[] urls ;

    public StructClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.urls = urls;
    }

    public Class findClass(String className) {
        try {
            return super.findClass(className);
        } catch (Exception e) {
            return null;
        }

    }

}
