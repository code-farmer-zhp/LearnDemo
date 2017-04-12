package excelDemo;


import java.io.*;
import java.util.Scanner;

public class FileBatch {
    public static void main(String[] args) throws Exception {

        File infile = new File("D:\\zhoupeng\\ExcelDemo\\src\\main\\memGuids.txt");
        File outfile = new File("D:\\zhoupeng\\ExcelDemo\\src\\main\\result2.json");

        BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outfile), "UTF-8"));
        InputStreamReader inputStream = new InputStreamReader(
                new FileInputStream(infile), "UTF-8");
        Scanner scanner = new Scanner(inputStream);
        int count = 0;
        String str = "";
        while (scanner.hasNext()) {
            count++;
            String line = scanner.nextLine();
            String[] split = line.split(" ");
            System.out.println(split[0] + " " + Mcrypt3Des.desDecrypt(split[1]));
        }
        System.out.println(str);
        scanner.close();
        fileWriter.flush();
        fileWriter.close();

    }
}
