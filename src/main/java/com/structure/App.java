package com.structure;

import com.structure.config.Config;
import com.structure.scanner.Scanner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

public class App {
    public static void main( String[] args ) throws Exception {
        init(args);
        new Scanner().start();
    }

    public static void init(String[] args) throws Exception {
        InputStream configPath ;
        if (args.length < 1) {
            configPath = Objects.requireNonNull(App.class.getClassLoader().getResourceAsStream("config.properties"));
        } else {
            configPath = new FileInputStream(args[0]);
        }
        Config.getConfig().loadConfig(configPath);
    }
}
