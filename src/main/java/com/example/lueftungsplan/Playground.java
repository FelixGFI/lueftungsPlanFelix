package com.example.lueftungsplan;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;

public class Playground {

    public static void main(String[] args) throws URISyntaxException, IOException {
        Reader reader = Files.newBufferedReader(Paths.get(
                ClassLoader.getSystemResource("playgroundFile.csv").toURI()));
        try {
            System.out.println(readAll(reader).toString());
            List<String[]> myList = oneByOne(reader);
            for (String[] s : myList) {
                System.out.println("\n");
                for (String string : s) {
                    System.out.print(s + " ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> oneByOne(Reader reader) throws Exception {
        List<String[]> list = new ArrayList<>();
        CSVReader csvReader = new CSVReader(reader);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            list.add(line);
            System.out.println(("oneByOne() schleife"));
        }
        reader.close();
        csvReader.close();
        return list;
    }

    public static List<String[]> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }

}
