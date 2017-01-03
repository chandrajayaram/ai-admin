package com.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraTemplate;

@Configuration
@PropertySource(value = { "classpath:cassandra.properties" })
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
	  public CassandraSessionFactoryBean session() throws Exception {
	    CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
	    session.setCluster(cluster().getObject());
	    session.setKeyspaceName(env.getProperty("cassandra.keyspace"));
	    session.setSchemaAction(SchemaAction.NONE);
	    return session;
	  }

	  @Bean
	  public CassandraTemplate cassandraTemplate() throws Exception {
	    return new CassandraTemplate(session().getObject());
	  }
}
