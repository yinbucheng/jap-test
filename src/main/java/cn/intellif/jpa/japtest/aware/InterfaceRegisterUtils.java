package cn.intellif.jpa.japtest.aware;

import cn.intellif.jpa.japtest.annotation.JAPScan;
import cn.intellif.jpa.japtest.utils.JPAMapperFactoryBean;
import cn.intellif.jpa.japtest.utils.ScannerClassUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

/**
 * 完成对接口的注册
 */
public class InterfaceRegisterUtils  implements ImportBeanDefinitionRegistrar{
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(JAPScan.class.getName()));
        ScannerClassUtils scannerClassUtils = new ScannerClassUtils(registry);
        String[] pacakges = attributes.getStringArray("packages");
        if(pacakges!=null&&pacakges.length>0){
            Set<BeanDefinitionHolder> beanDefinitionHolders = scannerClassUtils.doScan(pacakges);
            if(beanDefinitionHolders!=null&&beanDefinitionHolders.size()>0){
                for(BeanDefinitionHolder beanDefinitionHolder:beanDefinitionHolders){
                   GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
                   definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
                   definition.setBeanClass(JPAMapperFactoryBean.class);
                }
            }
        }
    }
}
