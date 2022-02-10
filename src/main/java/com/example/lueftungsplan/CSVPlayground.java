package com.example.lueftungsplan;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class CSVPlayground {

    /**
     * WICHTIG!!: EIN READER KANN NICHT ERNEUT VERWENDET WERDEN!
     * WENN EIN READER BEREITS FÜR DAS LESEN SEINES DOKUMENTS IN EINER
     * METHODE VERWENDET WURDE KANN ER SCHEINBAR NICHT OHNE WEITERES FÜR DAS LESEN DES SELBEN
     * DOKUMENTES VERWENDET WERDEN WEDER IN DER SELBEN NOCH EINER ANDEREN METHODE
     * EIN NEUER READER IST NÖTIG ES SEI DEN ES GIBT IRGENDEINE MÖGLICHEKEIT DEN
     * READER ANDERWEITEIG ZU RESETEN
     * @param args
     * @throws URISyntaxException
     * @throws IOException
     */

    public static void main(String[] args) throws URISyntaxException, IOException {
        Reader reader = Files.newBufferedReader(Paths.get(
                ClassLoader.getSystemResource("playgroundFile.csv").toURI()));
        Reader bufferedReader = Files.newBufferedReader(Paths.get(
                ClassLoader.getSystemResource("playgroundFile.csv").toURI()));
        try {
            List<String[]> l = readAll(reader);
            String[] sl1;
            System.out.println(l.size());
            for( String[] sList : l) {
                System.out.print("\n");
                for (String s : sList) {
                    System.out.print(s);
                }
            }
            System.out.println("\n---------------");
            List<String[]> myList2 = oneByOne(bufferedReader);
            for( String[] sList : myList2) {
                System.out.print("\n");
                for (String s : sList) {
                    System.out.print(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> oneByOne(Reader reader) throws Exception {
        List<String[]> list = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(0)
                .withCSVParser(parser)
                .build();
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            list.add(line);
        }

        removeAllLeadingSpaces(list);

        reader.close();
        csvReader.close();
        return list;
    }

    public static List<String[]> readAll(Reader reader) throws Exception {
        //CSVReader csvReader = new CSVReader(reader);

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(0)
                .withCSVParser(parser)
                .build();

        List<String[]> list = csvReader.readAll();

        removeAllLeadingSpaces(list);

        reader.close();
        csvReader.close();
        return list;
    }

    private static void removeAllLeadingSpaces(List<String[]> list) {
        for (String[] stringL : list) {
            for (int i = 0; i < stringL.length; i++) {
                stringL[i] = stringL[i].stripLeading();
            }
        }
    }

}
