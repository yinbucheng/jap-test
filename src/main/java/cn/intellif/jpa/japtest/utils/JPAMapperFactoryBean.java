package cn.intellif.jpa.japtest.utils;

import cn.intellif.jpa.japtest.annotation.Column;
import cn.intellif.jpa.japtest.annotation.Select;
import cn.intellif.jpa.japtest.annotation.Update;
import cn.intellif.jpa.japtest.aware.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 采用spring的FactoryBean机制生成实现类
 * @param <T>
 */
public class JPAMapperFactoryBean<T> implements FactoryBean<T> {

    private Class<T> clazz;

    private static Logger logger = LoggerFactory.getLogger(JPAMapperFactoryBean.class);

    private T cache;

    public JPAMapperFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Nullable
    @Override
    public T getObject() throws Exception {
        if(cache!=null){
            return cache;
        }
        return (T) Proxy.newProxyInstance(JPAMapperFactoryBean.class.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                JdbcTemplate jdbcTemplate = ApplicationContextUtils.getBean(JdbcTemplate.class);
                Update update = method.getAnnotation(Update.class);
                if(update!=null) {
                  return   jdbcTemplate.update(update.sql(), args);
                }
                Select select = method.getAnnotation(Select.class);
                if(select!=null){
                    String sql = select.sql();
                     final Class returnType = method.getReturnType();
                    boolean signle = true;
                    if(List.class.isAssignableFrom(returnType)){
                        signle = false;
                    }
                    List<T> query = jdbcTemplate.query(sql, args, new RowMapper<T>() {

                        @Nullable
                        @Override
                        public T mapRow(ResultSet resultSet, int i) throws SQLException {
                            Class temp = select.returnBaseType();
                            return reflectToBean(temp,resultSet);
                        }
                    });
                    if(query==null||query.size()==0)
                        return null;
                    if(signle)
                        return query.get(0);
                    return query;
                }
                return null;
            }
        });
    }

    private <T> T reflectToBean(Class clazz,ResultSet resultSet){
        try {
            Field[] declaredFields = clazz.getDeclaredFields();
            T bean = (T) clazz.newInstance();
            for(Field field:declaredFields){
               Column column =  field.getAnnotation(Column.class);
               if(column==null||!column.exist())
                   continue;
               String name = column.name();
               if(name.equals("")){
                   name = field.getName();
               }
              Object value =   resultSet.getObject(name);
               if(value!=null){
                   field.setAccessible(true);
                   field.set(bean,value);
               }
            }
            return bean;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return clazz;
    }
}
