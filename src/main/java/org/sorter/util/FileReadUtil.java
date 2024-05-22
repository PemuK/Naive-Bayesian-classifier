package org.sorter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReadUtil {

    public static Map<Integer, List<String>> readData(String filePath) {
        Map<Integer, List<String>> dataMap = new HashMap<>();

        try (InputStream inputStream = FileReadUtil.class.getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "GBK"))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    continue; // 跳过第一行
                }
                String[] values = line.split("[,\\s]+"); // 使用逗号和空格分隔
                List<String> features = new ArrayList<>();
                for (String value : values) {
                    features.add(value);
                }
                dataMap.put(lineNumber - 1, features); // 从第二行开始计数
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataMap;
    }
}
