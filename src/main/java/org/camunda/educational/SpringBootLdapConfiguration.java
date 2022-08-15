package org.camunda.educational;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin;
import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({SpringBootLdapProperties.class})
@Configuration
@AutoConfigureAfter({CamundaBpmAutoConfiguration.class})
public class SpringBootLdapConfiguration {
   
    @Autowired 
    SpringBootLdapProperties springBootLdapProperties;

    public SpringBootLdapConfiguration(){
    }

    @Bean
    @ConditionalOnProperty(
        name = {"enabled"},
        havingValue = "true",
        prefix = "camunda.bpm.run.ldap"
    )
    public SpringBootLdapPlugin springBootLdapPlugin(){
        return this.springBootLdapProperties.getLdap();
    }

    @Bean
    @ConditionalOnProperty(
            name = {"enabled"},
            havingValue = "true",
            prefix = "myapp.camunda.superadmin"
    )
    public AdministratorAuthorizationPlugin administratorAuthorizationPlugin(
            @Value("${myapp.camunda.superadmin.user:}") String adminUser,
            @Value("${myapp.camunda.superadmin.group:}") String adminGroup
            ){
        AdministratorAuthorizationPlugin administratorAuthorizationPlugin = new AdministratorAuthorizationPlugin();
        if(Objects.nonNull(adminUser) && !StringUtils.isEmpty(adminUser)) {
            System.out.println("******* admin user: " + adminUser);
            administratorAuthorizationPlugin.setAdministratorUserName(adminUser);
        }
        if(Objects.nonNull(adminGroup) && !StringUtils.isEmpty(adminGroup)){
            administratorAuthorizationPlugin.setAdministratorGroupName(adminGroup);
        }
        return administratorAuthorizationPlugin;
    }

}

