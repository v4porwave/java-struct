package com.structure.common;

import com.structure.config.Config;
import com.structure.exception.ConfigLoadException;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class SimpleFileVisitor implements FileVisitor<Path> {

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        if (exc != null ) {
            throw new ConfigLoadException(exc.getMessage());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (exc != null ) {
            throw new ConfigLoadException(exc.getMessage());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        File f = file.toFile();
        String filename = f.getName();
        if (filename.endsWith(".class")) {
            Config.getConfig().getClassList().add(new ClassReader(new FileInputStream(f)));
        } else if (filename.endsWith(".jar")) {
            Config.getConfig().getJarList().add(f);
        }
        return FileVisitResult.CONTINUE;
    }
}
