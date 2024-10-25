package com.lyf.dao;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/9/3 10:30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    public String name;
    public int age;
    public String hobby;

    public User(String name, String hobby) {
        this.name = name;
        this.hobby = hobby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }
}
