package dev.ncn.keycloak.spi.unp.connections.jpa.entityprovider;

import org.keycloak.Config;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserModel;

/**
 * @author <a href="mailto:chinnayya.nalla.careers@gmail.com">Chinnayya Naidu Nalla</a>
 */
public class CustomJPAEntityProviderFactory implements JpaEntityProviderFactory {

    protected static final String ID = "CUSTOM_JPA";

    @Override
    public JpaEntityProvider create(KeycloakSession session) {
        return new CustomJPAEntityProvider();
    }

    @Override
    public void init(Config.Scope config) { }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

        factory.register((providerEventListener) -> {

            if(providerEventListener instanceof UserModel.UserRemovedEvent) {
                UserModel.UserRemovedEvent userRemovedEvent = (UserModel.UserRemovedEvent) providerEventListener;
                // TODO: Implement the Logic to Remove the Records for the Deleted User
            }

        });

    }

    @Override
    public void close() { }

    @Override
    public String getId() {
        return ID;
    }

}
