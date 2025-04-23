package com.satoripop.insurance;

import com.satoripop.insurance.config.AsyncSyncConfiguration;
import com.satoripop.insurance.config.EmbeddedElasticsearch;
import com.satoripop.insurance.config.EmbeddedSQL;
import com.satoripop.insurance.config.JacksonConfiguration;
import com.satoripop.insurance.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { InsuranceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class })
@EmbeddedElasticsearch
@EmbeddedSQL
public @interface IntegrationTest {
}
