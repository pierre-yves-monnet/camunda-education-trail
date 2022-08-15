package org.camunda.educational;

import org.camunda.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin;

public class SpringBootLdapPlugin extends LdapIdentityProviderPlugin{

    boolean enabled = true;

    public SpringBootLdapPlugin() {
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }



}

