package excelDemo;


import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileBatch2 {
    public static void main(String[] args) throws Exception {

        File infile = new File("D:\\zhoupeng\\ExcelDemo\\src\\main\\score.info");
        File outfile = new File("D:\\zhoupeng\\ExcelDemo\\src\\main\\score2.info");

        BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outfile), "UTF-8"));
        InputStreamReader inputStream = new InputStreamReader(
                new FileInputStream(infile), "UTF-8");
        Scanner scanner = new Scanner(inputStream);
        Map<String, Integer> map = new HashMap<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] split = line.split("\\{", 2);
            String json = "{" + split[1];
            JSONObject jsonObject = JSONObject.parseObject(json);
            JSONObject applog = jsonObject.getJSONObject("applog");
            Integer aReturn = applog.getInteger("return");

            if (aReturn != null && aReturn > 0) {
                String params = applog.getString("params");
                //System.out.println(aReturn);
                //System.out.println(params);
                String[] paramsArray = params.split(",");
                String key = paramsArray[0] + ":" + paramsArray[1];
                map.put(key, aReturn);
            }

        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            fileWriter.write(entry.getKey() + ":" + entry.getValue());
            fileWriter.newLine();

        }
        scanner.close();
        fileWriter.flush();
        fileWriter.close();

    }
}
