package com.huf.zerocopy;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.Instant;

public class MMapExample {
    public static void main(String[] args) throws Exception{
        Instant begin = Instant.now();

        // 打开输出文件
        RandomAccessFile outFile = new RandomAccessFile("D:\\rubbish\\outputMmap.txt", "rw");
        // 获取输出文件通道
        FileChannel outChannel = outFile.getChannel();
        String[] inputFiles = new String[]{"D:\\rubbish\\inputFile1.txt",
                "D:\\rubbish\\inputFile2.txt"};
        for (int i = 0; i < inputFiles.length; i++) {
            // 打开输入文件
            RandomAccessFile inFile = new RandomAccessFile(inputFiles[i], "r");
            // 获取文件通道
            FileChannel inChannel = inFile.getChannel();
            // 获取文件大小
            long fileSize = inChannel.size();
            // 将文件映射到内存
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

            // 将内存中的数据写入新文件
            outChannel.write(buffer);

            // 关闭文件和通道
            inChannel.close();
        }
        outFile.close();
        Instant end = Instant.now();
        System.out.println("cost time : " + (end.toEpochMilli()-begin.toEpochMilli()));
    }

    public static void main2(String[] args) throws Exception {
        // 开启 mmap
        FileChannel fileChannel = new RandomAccessFile(new File("test.log"), "rw").getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size()+100000);

// 写
        byte[] data = new byte[4];
        int position = 8;
// 从 mmap 当前指针写入 4b 数据
        mappedByteBuffer.put(data);
// 指定 position 位置写入 4b 数据
        MappedByteBuffer subMmap = (MappedByteBuffer) mappedByteBuffer.slice();
        subMmap.position(position);
        subMmap.put(data);

// 读
        byte[] readData = new byte[4];
// 从当前指针读 4b
        mappedByteBuffer.get(data);
// 从指定位置读 4b
        subMmap.position(position);
        subMmap.get(data);
    }
}
