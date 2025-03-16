package com.structure.config;

import com.structure.StructClassLoader;
import com.structure.common.SimpleFileVisitor;
import com.structure.exception.ConfigLoadException;
import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {
    private Config() {}

    @Getter
    public final Logger LOGGER = Logger.getLogger(Config.class.getName());

    @Getter
    @Setter
    private List<String> servletClass;

    @Getter
    @Setter
    private List<String> filterClass;

    @Getter
    @Setter
    private List<String> blackClass;

    @Getter
    @Setter
    private String targetPath;

    @Getter
    @Setter
    private EType targetType;

    @Getter
    @Setter
    private List<ClassReader> classList;

    @Getter
    @Setter
    private List<File> jarList;

    @Getter
    @Setter
    private StructClassLoader classLoader;

    @Getter
    public static final Config config = new Config();

    public void loadConfig(InputStream configPath) {
        this.classList = new ArrayList<>();
        this.jarList = new ArrayList<>();

        Properties properties = new Properties();
        try {
            properties.load(configPath);
            this.servletClass = Arrays.asList(properties.getProperty("api.class").split(","));
            this.filterClass = Arrays.asList(properties.getProperty("filter.class").split(","));
            this.blackClass = Arrays.asList(properties.getProperty("black.class").split(","));

            String targetPath = properties.getProperty("target.path");
            File file = new File(targetPath);

            if (!file.exists()) {
                throw new ConfigLoadException("目标不存在: %s%n".formatted(targetPath));
            }
            if (( this.targetType = getType(file)) != null ){
                this.targetPath = file.getAbsolutePath();
            } else {
                throw new RuntimeException("未知目标类型: %s".formatted(targetPath));
            }
            System.out.printf(">>> 目标地址: %s, 目标类型: %s%n", this.targetPath, this.targetType);
            walkFile();
            this.classLoader = new StructClassLoader(listClassPath(), Thread.currentThread().getContextClassLoader());
        }catch (Exception e) {
            throw new ConfigLoadException("配置读取失败, 原因: %s".formatted(e.getMessage()));
        }
    }

    private EType getType(File file) {
        if (file.isDirectory()) {
            return EType.DIR;
        } else {
            if (file.getAbsolutePath().endsWith(".jar")) {
                return EType.JAR;
            } else if (file.getAbsolutePath().endsWith(".war")){
                return EType.WAR;
            } else {
                return null;
            }
        }
    }

    private void walkFile() throws Exception{
        if (targetType == EType.DIR) {
            Files.walkFileTree(Paths.get(Config.getConfig().getTargetPath()), new SimpleFileVisitor());
        } else if (targetType == EType.JAR) {
            System.out.println(1);
        } else if (targetType == EType.WAR) {
            System.out.println(1);
        }
    }

    private URL[] listClassPath() throws MalformedURLException {
        URL[] result = new URL[jarList.size()];
        for (int i = 0;i < jarList.size(); i ++ ) {
            result[i] = jarList.get(i).toURI().toURL();
        }
        return result;
    }
}
