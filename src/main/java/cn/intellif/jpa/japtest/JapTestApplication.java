package cn.intellif.jpa.japtest;

import cn.intellif.jpa.japtest.annotation.JAPScan;
import cn.intellif.jpa.japtest.test.dao.TestDao;
import cn.intellif.jpa.japtest.test.entity.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
@JAPScan(packages = "cn.intellif.jpa.japtest.test.dao")
public class JapTestApplication {

	public static void main(String[] args) {
	   ApplicationContext applicationContext =  SpringApplication.run(JapTestApplication.class, args);
	   TestDao testDao = applicationContext.getBean(TestDao.class);
	   testDao.save("test_for_all");
		System.out.println(">>>>>>>>>>>>>>>>>>>>执行完成");
		List<Test> datas = testDao.listAll();
		System.out.println(datas.size());
		System.out.println(testDao.toString());
	}
}
