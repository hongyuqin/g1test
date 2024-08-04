package com.huf.zerocopy;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.Instant;

public class MMapExample {
    public static void main(String[] args) throws Exception{
        Instant begin = Instant.now();

        // ������ļ�
        RandomAccessFile outFile = new RandomAccessFile("D:\\rubbish\\outputMmap.txt", "rw");
        // ��ȡ����ļ�ͨ��
        FileChannel outChannel = outFile.getChannel();
        String[] inputFiles = new String[]{"D:\\rubbish\\inputFile1.txt",
                "D:\\rubbish\\inputFile2.txt"};
        for (int i = 0; i < inputFiles.length; i++) {
            // �������ļ�
            RandomAccessFile inFile = new RandomAccessFile(inputFiles[i], "r");
            // ��ȡ�ļ�ͨ��
            FileChannel inChannel = inFile.getChannel();
            // ��ȡ�ļ���С
            long fileSize = inChannel.size();
            // ���ļ�ӳ�䵽�ڴ�
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);

            // ���ڴ��е�����д�����ļ�
            outChannel.write(buffer);

            // �ر��ļ���ͨ��
            inChannel.close();
        }
        outFile.close();
        Instant end = Instant.now();
        System.out.println("cost time : " + (end.toEpochMilli()-begin.toEpochMilli()));
    }

    public static void main2(String[] args) throws Exception {
        // ���� mmap
        FileChannel fileChannel = new RandomAccessFile(new File("test.log"), "rw").getChannel();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size()+100000);

// д
        byte[] data = new byte[4];
        int position = 8;
// �� mmap ��ǰָ��д�� 4b ����
        mappedByteBuffer.put(data);
// ָ�� position λ��д�� 4b ����
        MappedByteBuffer subMmap = (MappedByteBuffer) mappedByteBuffer.slice();
        subMmap.position(position);
        subMmap.put(data);

// ��
        byte[] readData = new byte[4];
// �ӵ�ǰָ��� 4b
        mappedByteBuffer.get(data);
// ��ָ��λ�ö� 4b
        subMmap.position(position);
        subMmap.get(data);
    }
}
