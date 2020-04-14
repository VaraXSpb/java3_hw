package hw3;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //createFile();
        //readFile("1.txt");
        //uniteFiles(createFile("result"));
        //runBookReader("book.txt");
    }

    private static File createFile(String fileName) {
        File file = new File(fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static void readFile(String fileName) {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName))) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int x;
            while ((x = in.read()) > 0) {
                out.write(x);
            }
            byte[] arr = out.toByteArray();
            System.out.println(Arrays.toString(arr));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void uniteFiles(File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);

        ArrayList<InputStream> list = new ArrayList<>();
        list.add(new FileInputStream("11.txt"));
        list.add(new FileInputStream("12.txt"));
        list.add(new FileInputStream("13.txt"));
        list.add(new FileInputStream("14.txt"));
        list.add(new FileInputStream("15.txt"));

        BufferedInputStream in = new BufferedInputStream(new SequenceInputStream(Collections.enumeration(list)));

        int x;
        while ((x = in.read()) > 0) {
            out.write(x);
        }
    }

    private static void readBook(String book, int page) {
        try (RandomAccessFile raf = new RandomAccessFile(book, "r")) {
            int delta = 1800 * (page-1);
            for (int i = delta; i < 1800 + delta; i++) {
                raf.seek(i);
                System.out.print((char)raf.read());
            }
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runBookReader(String book) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите номер страницы:");
            readBook(book, scanner.nextInt());
        }
    }
}
