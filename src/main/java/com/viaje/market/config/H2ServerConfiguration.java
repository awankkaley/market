package com.viaje.market.config;

import com.zaxxer.hikari.HikariDataSource;
import org.h2.tools.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@Profile("local") // set Local DB server for h2
public class H2ServerConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.hikari") // set yml configs
    public DataSource dataSource() throws SQLException {

        System.out.println("ConfigurationProperties for h2 ...");

        Server.createTcpServer("-tcp",
                "-tcpPort",
                "9091",
                "-tcpAllowOthers",
                "-ifNotExists"
        ).start();

        return new HikariDataSource();
    }
}