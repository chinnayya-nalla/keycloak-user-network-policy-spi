package dev.ncn.keycloak.spi.unp.services.resource;

import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

/**
 * @author <a href="mailto:chinnayya.nalla.careers@gmail.com">Chinnayya Naidu Nalla</a>
 */
public class UserNetworkPolicyResourceProviderFactory implements RealmResourceProviderFactory {

    public static final String ID = "user-network-policy";

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        UserNetworkPolicyResourceProvider provider = new UserNetworkPolicyResourceProvider(session);
        ResteasyProviderFactory.getInstance().injectProperties(provider);
        return provider;
    }

    @Override
    public void init(Config.Scope config) { }

    @Override
    public void postInit(KeycloakSessionFactory factory) { }

    @Override
    public void close() { }

    @Override
    public String getId() {
        return ID;
    }

}
