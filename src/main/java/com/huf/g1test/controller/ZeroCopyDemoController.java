package com.huf.g1test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

@RestController
public class ZeroCopyDemoController {

    // 传统IO方式下载大文件
    @GetMapping("/download/traditional")
    public void downloadTraditional(HttpServletResponse response) throws IOException {
        String filePath = "/Users/hongyuqin/Downloads/goland-2024.3.5-aarch64.dmg";
        File file = new File(filePath);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        long start = System.currentTimeMillis();
        try (InputStream in = new FileInputStream(file);
             ServletOutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("[传统IO] 下载耗时: " + (end - start) + " ms");
    }

    // 零拷贝方式下载大文件
    @GetMapping("/download/zerocopy")
    public void downloadZeroCopy(HttpServletResponse response) throws IOException {
        String filePath = "/Users/hongyuqin/Downloads/goland-2024.3.5-aarch64.dmg";
        File file = new File(filePath);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        long start = System.currentTimeMillis();
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel fileChannel = fis.getChannel();
             ServletOutputStream out = response.getOutputStream();
             WritableByteChannel outChannel = Channels.newChannel(out)) {
            fileChannel.transferTo(0, file.length(), outChannel);
        }
        long end = System.currentTimeMillis();
        System.out.println("[零拷贝] 下载耗时: " + (end - start) + " ms");
    }
} 