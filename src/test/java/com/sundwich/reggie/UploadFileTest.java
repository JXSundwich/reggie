package com.sundwich.reggie;

import org.junit.jupiter.api.Test;

/**
 * @author JX Sun
 * @date 2023.07.25 16:33
 */
public class UploadFileTest {
    @Test
    public void test1(){
        String fileName="erea.jpg";
        String suffix=fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
    }
}
