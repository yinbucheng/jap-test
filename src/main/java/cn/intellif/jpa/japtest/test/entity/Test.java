package cn.intellif.jpa.japtest.test.entity;

import cn.intellif.jpa.japtest.annotation.Column;

import java.io.Serializable;

public class Test implements Serializable{
    @Column(name="id")
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name = "age",exist = false)
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
