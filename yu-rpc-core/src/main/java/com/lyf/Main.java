package com.lyf;

import com.lyf.serializer.Serializer;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/18 16:37
 */
public class Main {
    public static void main(String[] args) {
        String name = "localhost:8080";
        System.out.println(name.hashCode());
        System.out.println((name + "#" + 1).hashCode());
    }
}