package com.zsw.rpc.autoconfigure;

import com.zsw.rpc.support.stereotype.RpcClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author ZhangShaowei on 2019/6/6 14:43
 **/
@Slf4j
@Configuration
@ComponentScan("com")
@Import({RpcClienteAutoConfiguration.AutoConfiguredClientScannerRegistrar.class})
public class RpcClienteAutoConfiguration {


    /**
     * 尝试使用 spring 的方式生成 RPCClient
     */
    public static class AutoConfiguredClientScannerRegistrar
            implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

            ClassPathRpcClientScanner scanner = new ClassPathRpcClientScanner(registry);
            if (this.resourceLoader != null) {
                scanner.setResourceLoader(this.resourceLoader);
            }
            scanner.setAnnotationClass(RpcClient.class);
            scanner.registerFilters();

            // TODO  获取注解上的 basePackages
            scanner.doScan("com");

        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }

}
