package org.closure.laser.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, org.closure.laser.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, org.closure.laser.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, org.closure.laser.domain.User.class.getName());
            createCache(cm, org.closure.laser.domain.Authority.class.getName());
            createCache(cm, org.closure.laser.domain.User.class.getName() + ".authorities");
            createCache(cm, org.closure.laser.domain.Article.class.getName());
            createCache(cm, org.closure.laser.domain.UserApplication.class.getName());
            createCache(cm, org.closure.laser.domain.UserApplication.class.getName() + ".tripsDeals");
            createCache(cm, org.closure.laser.domain.UserApplication.class.getName() + ".shipmentDeals");
            createCache(cm, org.closure.laser.domain.Deal.class.getName());
            createCache(cm, org.closure.laser.domain.Deal.class.getName() + ".shipments");
            createCache(cm, org.closure.laser.domain.Transaction.class.getName());
            createCache(cm, org.closure.laser.domain.Location.class.getName());
            createCache(cm, org.closure.laser.domain.Location.class.getName() + ".tripDestinations");
            createCache(cm, org.closure.laser.domain.Location.class.getName() + ".tripSources");
            createCache(cm, org.closure.laser.domain.Location.class.getName() + ".shipmentDestinations");
            createCache(cm, org.closure.laser.domain.Location.class.getName() + ".shipmentSources");
            createCache(cm, org.closure.laser.domain.Shipment.class.getName());
            createCache(cm, org.closure.laser.domain.Connection.class.getName());
            createCache(cm, org.closure.laser.domain.Trip.class.getName());
            createCache(cm, org.closure.laser.domain.Trip.class.getName() + ".deals");
            createCache(cm, org.closure.laser.domain.AccountProvider.class.getName());
            createCache(cm, org.closure.laser.domain.AccountProvider.class.getName() + ".transactions");
            createCache(cm, org.closure.laser.domain.ShipmentType.class.getName());
            createCache(cm, org.closure.laser.domain.ShipmentType.class.getName() + ".shipments");
            createCache(cm, org.closure.laser.domain.DealStatus.class.getName());
            createCache(cm, org.closure.laser.domain.DealStatus.class.getName() + ".deals");
            createCache(cm, org.closure.laser.domain.Offers.class.getName());
            createCache(cm, org.closure.laser.domain.Constants.class.getName());
            createCache(cm, org.closure.laser.domain.Countries.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
