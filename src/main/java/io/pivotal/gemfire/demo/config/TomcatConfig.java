package io.pivotal.gemfire.demo.config;

import org.apache.catalina.Context;
import org.apache.geode.modules.session.catalina.ClientServerCacheLifecycleListener;
import org.apache.geode.modules.session.catalina.Tomcat8DeltaSessionManager;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by azwickey on 5/22/17.
 */
@Configuration
public class TomcatConfig {

    //@Autowired
    //TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory;

    @Bean
    public ClientServerCacheLifecycleListener clientServerCacheLifecycleListener() {
        return new ClientServerCacheLifecycleListener();
    }

    public Tomcat8DeltaSessionManager tomcat8DeltaSessionManager() {
        return new Tomcat8DeltaSessionManager();
    }

    @Bean
    public EmbeddedServletContainerFactory container(ClientServerCacheLifecycleListener clientServerCacheLifecycleListener) {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addContextLifecycleListeners(clientServerCacheLifecycleListener);
        tomcat.addContextCustomizers(new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.setManager(tomcat8DeltaSessionManager());
            }
        });
        return tomcat;
    }

}
