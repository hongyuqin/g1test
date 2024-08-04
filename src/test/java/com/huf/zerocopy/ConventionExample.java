package com.huf.zerocopy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Instant;

public class ConventionExample {
    public static void main(String[] args) throws Exception {
        Instant begin = Instant.now();
        String[] inputFiles = new String[]{"D:\\rubbish\\inputFile1.txt",
                "D:\\rubbish\\inputFile2.txt"};
        String outputFile = "D:\\rubbish\\outputFileConvention.txt";
        //Get channel for output file
        FileOutputStream fos = new FileOutputStream(outputFile);
        for (int i = 0; i < inputFiles.length; i++) {
            //Get channel for input files
            FileInputStream fis = new FileInputStream(inputFiles[i]);
            byte[] input = new byte[500];
            while(fis.read(input,0,500)!=-1){
                fos.write(input);
            }
            // 从输入流到输出流传送数据
            //关闭输入流
            fis.close();
        }
        //关闭流
        fos.close();
        Instant end = Instant.now();
        System.out.println("cost time : " + (end.toEpochMilli()-begin.toEpochMilli()));
    }
}
