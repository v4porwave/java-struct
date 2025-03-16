package com.structure.scanner;

import com.structure.common.Util;
import com.structure.config.Config;
import org.objectweb.asm.ClassReader;

public class Scanner {

    public void start() {
        for (ClassReader classReader : Config.getConfig().getClassList()) {
            String className = Util.getInternalName(classReader.getClassName());
            String superName = Util.getInternalName(classReader.getSuperName());
            if (Util.isServlet(superName)) {
                System.out.printf(">>> [Serlvet]: %s%n", className);
            }
            if (Util.isFilter(superName)) {
                System.out.printf(">>> [发现filter]: %s%n", className);
            }
        }
    }

}
