package org.sorter;

import org.sorter.train.Bayes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.sorter.util.FileReadUtil.readData;

public class Main {
    public static void main(String[] args) {

        String filePath = "/data/data.txt";

        Map<Integer, List<String>> dataMap =readData(filePath) ;
        Bayes bayes=new Bayes(readData(filePath));
        bayes.train();
        List<String> newSample = List.of("青绿", "蜷缩", "沉闷", "清晰", "凹陷", "硬滑", "0.608", "0.318");
        String prediction = bayes.predict(newSample);
        System.out.println("新样本的预测结果: " + prediction);
        // 输出数据
//        for (Map.Entry<Integer, List<String>> entry : dataMap.entrySet()) {
//            Integer lineNumber = entry.getKey();
//            List<String> values = entry.getValue();
//            System.out.print(lineNumber + ": ");
//            for (String value : values) {
//                System.out.print(value + " ");
//            }
//            System.out.println();
//        }
    }
}
