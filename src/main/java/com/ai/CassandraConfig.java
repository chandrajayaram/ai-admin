package com.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;

@Configuration
@ComponentScan(basePackages={"com.ai.auditlog"})
@PropertySource(value = { "classpath:cassandra.properties" })
@EnableAutoConfiguration
@EnableConfigurationProperties
public class CassandraConfig {
	
	  @Autowired
	  private Environment env;
	  
	  @Bean
	  public CassandraClusterFactoryBean cluster() {
	    CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
	    cluster.setContactPoints(env.getProperty("cassandra.contactpoints"));
	    cluster.setPort(Integer.parseInt(env.getProperty("cassandra.port")));
	    return cluster;
	  }
	  @Bean
	  public CassandraMappingContext mappingContext() {
	    return new BasicCassandraMappingContext();
	  }

	  
	  @Bean
	  public CassandraConverter converter() {
	    return new MappingCassandraConverter(mappingContext());
	  }
	  
	  @Bean
	  public CassandraSessionFactoryBean session() throws Exception {
	    CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
	    session.setCluster(cluster().getObject());
	    session.setKeyspaceName(env.getProperty("cassandra.keyspace"));
	    session.setConverter(converter());
	    session.setSchemaAction(SchemaAction.NONE);
	    return session;
	  }

	  @Bean
	  public CassandraTemplate cassandraTemplate() throws Exception {
	    return new CassandraTemplate(session().getObject());
	  }
}
