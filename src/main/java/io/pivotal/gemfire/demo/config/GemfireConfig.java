package io.pivotal.gemfire.demo.config;

import io.pivotal.gemfire.demo.HttpSessionCachingWebappApplication;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.LocalRegionFactoryBean;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.config.xml.GemfireConstants;
import org.springframework.data.gemfire.support.ConnectionEndpoint;

import java.util.Properties;

/**
 * Created by azwickey on 5/22/17.
 */
@Configuration
public class GemfireConfig {

    protected static final String DEFAULT_GEMFIRE_LOG_LEVEL = "debug";

    protected String applicationName() {
        return HttpSessionCachingWebappApplication.class.getSimpleName();
    }

    protected String logLevel() {
        return System.getProperty("gemfire.log-level", DEFAULT_GEMFIRE_LOG_LEVEL);
    }

    public Properties gemfireProperties() {
        Properties gemfireProperties = new Properties();
        gemfireProperties.setProperty("name", applicationName());
        gemfireProperties.setProperty("log-level", logLevel());
        return gemfireProperties;
    }

    @Bean
    ClientCacheFactoryBean gemfireCache() {
        ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();
        gemfireCache.setClose(true);
        gemfireCache.setProperties(gemfireProperties());
        return gemfireCache;
    }

    @Bean(name = GemfireConstants.DEFAULT_GEMFIRE_POOL_NAME)
    PoolFactoryBean gemfirePool(@Value("${locator.host}") String host, @Value("${locator.port}") int port) {
        PoolFactoryBean gemfirePool = new PoolFactoryBean();
        gemfirePool.setKeepAlive(false);
        gemfirePool.setSubscriptionEnabled(true);
        gemfirePool.setThreadLocalConnections(false);
        gemfirePool.addLocators(new ConnectionEndpoint(host, port));
        return gemfirePool;
    }

    @Bean
    ClientRegionFactoryBean<String, String> clientRegionFactoryBean(final GemFireCache cache) {
        ClientRegionFactoryBean<String, String> r = new ClientRegionFactoryBean();
        r.setCache(cache);
        r.setName("gemfire_modules_sessions");
        r.setPersistent(false);
        return r;
    }

}
