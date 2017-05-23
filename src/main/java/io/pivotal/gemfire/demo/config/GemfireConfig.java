package io.pivotal.gemfire.demo.config;

import io.pivotal.gemfire.demo.HttpSessionCachingWebappApplication;
import org.apache.geode.cache.GemFireCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    protected static final String DEFAULT_GEMFIRE_LOG_LEVEL = "info";

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
    ClientCacheFactoryBean clientCacheFactoryBean() {
        ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();
        gemfireCache.setClose(true);
        gemfireCache.setProperties(gemfireProperties());
        return gemfireCache;
    }

    @Bean(name = GemfireConstants.DEFAULT_GEMFIRE_POOL_NAME)
    PoolFactoryBean gemfirePool(@Value("${locator.address}") String locator) {
        PoolFactoryBean gemfirePool = new PoolFactoryBean();
        gemfirePool.setKeepAlive(false);
        gemfirePool.setSubscriptionEnabled(true);
        gemfirePool.setThreadLocalConnections(false);
        gemfirePool.addLocators(ConnectionEndpoint.parse(locator));
        return gemfirePool;
    }

}
