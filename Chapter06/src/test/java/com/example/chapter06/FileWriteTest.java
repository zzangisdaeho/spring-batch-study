package com.example.chapter06;

import com.example.chapter06.util.ErrorWriter;
import org.junit.jupiter.api.Test;

public class FileWriteTest {

    @Test
    public void testWrite() {
        new ErrorWriter().write("돼냐?");
    }
}
