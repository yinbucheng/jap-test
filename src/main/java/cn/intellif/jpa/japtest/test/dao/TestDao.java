package cn.intellif.jpa.japtest.test.dao;

import cn.intellif.jpa.japtest.annotation.Select;
import cn.intellif.jpa.japtest.annotation.Update;
import cn.intellif.jpa.japtest.test.entity.Test;

import java.util.List;

public interface TestDao {
    @Update(sql="insert into yinchong.t_test(name) values(?)")
    void  save(String name);

    @Select(sql="select * from yinchong.t_test",returnBaseType = Test.class)
    List<Test> listAll();
}
