package io.pivotal.gemfire.demo.config;

import org.apache.catalina.Context;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.modules.session.catalina.ClientServerCacheLifecycleListener;
import org.apache.geode.modules.session.catalina.Tomcat8DeltaSessionManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.cache.GemfireCache;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;

import javax.annotation.PostConstruct;

/**
 * Created by azwickey on 5/22/17.
 */
@Configuration
public class TomcatConfig {

    @Bean
    public ClientServerCacheLifecycleListener clientServerCacheLifecycleListener() {
        return new ClientServerCacheLifecycleListener();
    }

    @Bean
    public Tomcat8DeltaSessionManager tomcat8DeltaSessionManager(GemFireCache gemFireCache) {
        return new Tomcat8DeltaSessionManager();
    }

    @Bean
    public EmbeddedServletContainerFactory container(ClientServerCacheLifecycleListener clientServerCacheLifecycleListener, Tomcat8DeltaSessionManager tomcat8DeltaSessionManager) {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addContextLifecycleListeners(clientServerCacheLifecycleListener);
        tomcat.addContextCustomizers(new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.setManager(tomcat8DeltaSessionManager);
            }
        });
        return tomcat;
    }

}
