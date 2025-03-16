package com.structure;

import com.structure.config.Config;
import com.structure.scanner.Scanner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

public class App {
    public static void main( String[] args ) throws Exception {
        System.out.println("┬─┐┬─┐┌─┐  ┬┬─┐┌─┐┌┐┐  ┐─┐┌┐┐┬─┐┬ ┐┌─┐┌┐┐┬ ┐┬─┐┬─┐\n" +
                "│─┘│┬┘│ │┌ │├─ │   │ ──└─┐ │ │┬┘│ ││   │ │ ││┬┘├─ \n" +
                "┆  ┆└┘┘─┘└─┆┴─┘└─┘ ┆   ──┘ ┆ ┆└┘┆─┘└─┘ ┆ ┆─┘┆└┘┴─┘");
        System.out.println("Author: v4por");
        System.out.println("Version: 1.0");
        System.out.println();
        long start = System.currentTimeMillis();
        init(args);
        new Scanner().start();
        long end = System.currentTimeMillis();
        System.out.printf(">>> 扫描完毕, 共耗时 %.2fs.%n%n", (end - start) / 1000.0);
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
