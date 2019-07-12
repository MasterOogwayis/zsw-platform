package com.zsw.rpc.autoconfigure;

import com.zsw.rpc.discovery.RegistryCenter;
import com.zsw.rpc.discovery.ZookeeperRegistryCenter;
import com.zsw.rpc.support.stereotype.RpcClient;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author ZhangShaowei on 2019/6/6 14:43
 **/
@Slf4j
@Configuration
@ComponentScan("com")
@PropertySource({"classpath:zookeeper.properties"})
@Import({RpcClientAutoConfiguration.AutoConfiguredClientScannerRegistrar.class})
public class RpcClientAutoConfiguration {

    @Value("${zookeeper.server.addresses}")
    private String zookeeperAddresses;

    @Value("${zookeeper.server.namespace}")
    private String namespace;


    @Bean
    public RegistryCenter registryCenter() {
        return new ZookeeperRegistryCenter(zookeeperAddresses, namespace);
    }
    /**
     * 尝试使用 spring 的方式生成 RPCClient
     */
    public static class AutoConfiguredClientScannerRegistrar
            implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        ResourceLoader resourceLoader;


        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

            ClassPathRpcClientScanner scanner = new ClassPathRpcClientScanner(registry);
            if (this.resourceLoader != null) {
                scanner.setResourceLoader(this.resourceLoader);
            }
            scanner.setAnnotationClass(RpcClient.class);
            scanner.registerFilters();

            // TODO  获取注解上的 basePackages
            scanner.doScan("com.zsw.rpc");

        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }

}
