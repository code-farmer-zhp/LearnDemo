package excelDemo;


import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileBatch3 {
    public static void main(String[] args) throws Exception {

        File infile = new File("D:\\zhoupeng\\ExcelDemo\\src\\main\\submitError.01");


        InputStreamReader inputStream = new InputStreamReader(
                new FileInputStream(infile), "UTF-8");
        Scanner scanner = new Scanner(inputStream);
        Map<String, Integer> map = new HashMap<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            JSONObject jsonObject = JSONObject.parseObject(line);
            JSONObject applog = jsonObject.getJSONObject("applog");

            String params = applog.getString("params");
            JSONObject paramJson = JSONObject.parseObject(params);
            String memGuid = paramJson.getString("memGuid");
            Integer count = map.get(memGuid);
            if (count == null) {
                map.put(memGuid, 1);
            } else {
                map.put(memGuid, ++count);
            }

        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey()+"  "+entry.getValue());
        }
        scanner.close();

    }
}
