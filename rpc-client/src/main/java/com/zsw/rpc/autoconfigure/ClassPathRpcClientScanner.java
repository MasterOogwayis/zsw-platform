package com.zsw.rpc.autoconfigure;

import com.zsw.rpc.client.RpcClientFactoryBean;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.MultiValueMap;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * @author ZhangShaowei on 2019/6/6 14:53
 **/
@Slf4j
@Setter
public class ClassPathRpcClientScanner extends ClassPathBeanDefinitionScanner {


    private Class<? extends Annotation> annotationClass;

    private Class<? extends FactoryBean> rpcClientFactoryBeanClass = RpcClientFactoryBean.class;


    public ClassPathRpcClientScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }


    public void registerFilters() {
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }

        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }


    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (beanDefinitions.isEmpty()) {
            log.warn("No MyBatis mapper was found in '{}' package. Please check your configuration.", Arrays.toString(basePackages));
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }


    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        ScannedGenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (ScannedGenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(this.rpcClientFactoryBeanClass);
            MultiValueMap<String, Object> annotationAttributes =
                    definition.getMetadata().getAllAnnotationAttributes("com.zsw.rpc.support.stereotype.RpcClient");

            Object host = annotationAttributes.get("host").get(0);
            Object port = annotationAttributes.get("port").get(0);
            Object version = annotationAttributes.get("version").get(0);
            Class<?> target = (Class<?>) annotationAttributes.get("target").get(0);
            if (target != void.class) {
                beanClassName = target.getName();
            }

            definition.getPropertyValues().add("host", host);
            definition.getPropertyValues().add("port", port);
            // 默认使用当前接口
            definition.getPropertyValues().add("target", beanClassName);
            definition.getPropertyValues().add("version", version);

            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }


    /**
     * 是要将这个接口扫描到容器 - 实际上会使用 代理类代理 RpcClientFactoryBean
     *
     * @param beanDefinition
     * @return
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

}
