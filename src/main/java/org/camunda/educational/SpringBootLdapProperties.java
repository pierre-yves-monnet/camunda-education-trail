package org.camunda.educational;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("camunda.bpm.run")
public class SpringBootLdapProperties {

    @NestedConfigurationProperty
    private SpringBootLdapPlugin ldap = new SpringBootLdapPlugin();

    public SpringBootLdapPlugin getLdap() {
        return this.ldap;
    }

    public void setLdap(SpringBootLdapPlugin ldap) {
        this.ldap = ldap;
    }

}
