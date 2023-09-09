package com.jhelper.store;

import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Store {

    private String storagePath;

    public Store() {
        this.storagePath = System.getProperty("java.io.tmpdir");
    }

    public Store(String storagePath) {
        this.storagePath = storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public Path getPath() {

        if (storagePath == null || "".equals(storagePath)) {
            return null;
        }

        Path p = Paths.get(storagePath);

        if (Files.isDirectory(p)) {
            return p;
        }

        return p.getParent();
    }

    public Path getPath(String path) throws AccessDeniedException {

        path = path.chars().mapToObj(i -> (char) i)
                .filter(c -> Character.isLetterOrDigit(c) || c == '-' || c == '_' || c == ':' || c == '\\' || c == '/'
                        || c == '.')
                .map(String::valueOf).collect(Collectors.joining());

        if (path.contains("..")) {
            throw new AccessDeniedException(path);
        }

        Path result = Paths.get(storagePath, path);

        if (!result.startsWith(storagePath)) {
            throw new AccessDeniedException("Invalid path constructed!");
        }

        return result;
    }
}