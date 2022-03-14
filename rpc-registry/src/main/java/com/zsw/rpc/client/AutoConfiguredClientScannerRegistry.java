package com.zsw.rpc.client;

import com.zsw.rpc.annotation.RpcReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 将 client 代理类托管给 spring
 *
 * @author Administrator
 **/
public class AutoConfiguredClientScannerRegistry
        implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {


    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        ClassPathRpcClientScanner scanner = new ClassPathRpcClientScanner(registry);
        if (this.resourceLoader != null) {
            scanner.setResourceLoader(this.resourceLoader);
        }
        scanner.setAnnotationClass(RpcReference.class);
        scanner.registerFilters();

        // TODO  获取注解上的 basePackages
        scanner.doScan("com.zsw.rpc");

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
