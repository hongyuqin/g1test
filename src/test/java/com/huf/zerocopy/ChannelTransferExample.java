package com.huf.zerocopy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.time.Instant;

/**
 * 零拷贝的sendfile  (kafka)
 */
public class ChannelTransferExample {

    public static void main(String[] args) throws Exception{
        Instant begin = Instant.now();
        String[] inputFiles = new String[]{"D:\\rubbish\\inputFile1.txt",
                "D:\\rubbish\\inputFile2.txt"};
        String outputFile = "D:\\rubbish\\outputFile.txt";
        //Get channel for output file
        FileOutputStream fos = new FileOutputStream(outputFile);
        WritableByteChannel targetChannel = fos.getChannel();
        for (int i = 0; i < inputFiles.length; i++) {
            //Get channel for input files
            FileInputStream fis = new FileInputStream(inputFiles[i]);
            FileChannel inputChannel = fis.getChannel();
            // 从输入流到输出流传送数据
            inputChannel.transferTo(0,inputChannel.size(),targetChannel);
            //关闭输入流
            inputChannel.close();
            fis.close();
        }
        //关闭流
        targetChannel.close();
        fos.close();
        Instant end = Instant.now();
        System.out.println("cost time : " + (end.toEpochMilli()-begin.toEpochMilli()));
    }
}
