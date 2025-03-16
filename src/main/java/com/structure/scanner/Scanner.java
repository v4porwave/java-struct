package com.structure.scanner;

import com.structure.common.Util;
import com.structure.config.Config;
import org.objectweb.asm.ClassReader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scanner {

    public void start() {
        Set<String> apiSet = new HashSet<>();
        Set<String> filterSet = new HashSet<>();
        for (ClassReader classReader : Config.getConfig().getClassList()) {
            String className = Util.getInternalName(classReader.getClassName());
            List<String> classNameList = Util.getClassInfo(classReader);

            if (Util.isServlet(classNameList)) {
                apiSet.add(className);
            }
            if (Util.isFilter(classNameList)) {
                filterSet.add(className);
            }
        }
        for (String api : apiSet) {
            System.out.printf("[API]: %s%n", api);
        }
        for (String filter : filterSet) {
            System.out.printf("[Filter]: %s%n", filter);
        }
    }

}
