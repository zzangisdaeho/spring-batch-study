package com.example.chapter06.util;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class ErrorWriter {

    public void write(String message) {
        try {
            // 1. 파일 객체 생성
            String rootDir = System.getProperty("user.dir");
            File file = new File(rootDir + "/writeFile.txt");
            // 2. 파일 존재여부 체크 및 생성
            if (!file.exists()) {
                file.createNewFile();
            }
            // 3. Buffer를 사용해서 File에 write할 수 있는 BufferedWriter 생성
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fw);
            // 4. 파일에 쓰기
            writer.write(message + "\n");
            // 5. BufferedWriter close
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
