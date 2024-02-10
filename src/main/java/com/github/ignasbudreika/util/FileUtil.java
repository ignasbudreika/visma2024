package com.github.ignasbudreika.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public BufferedReader getReader(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path));
    }

    public void writeToFile(String content, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(content);

        writer.close();
    }
}
