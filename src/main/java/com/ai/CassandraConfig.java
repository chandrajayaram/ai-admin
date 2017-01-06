package com.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;

@Configuration
@ComponentScan(basePackages={"com.ai.message","com.ai.message.ui","com.ai.message.service"})
@PropertySource(value = { "classpath:cassandra.properties","classpath:application.properties" })
public class CassandraConfig {
	
	  @Autowired
	  private Environment env;
	  
	  @Bean
	  public Session session(){
	  
	  	Cluster.Builder builder = Cluster.builder();
		builder.withClusterName("localCluster").addContactPoint("127.0.0.1");
		builder.withLoadBalancingPolicy(new TokenAwarePolicy(new RoundRobinPolicy()));
		Cluster cluster = builder.build();
		return cluster.connect("message_store_ks");
		
	  }
		
	  
	  /*@Bean
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
*/}
