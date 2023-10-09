package dev.ncn.keycloak.spi.unp.connections.jpa.entityprovider;

import dev.ncn.keycloak.spi.unp.connections.jpa.entity.UserNetworkPolicyEntity;
import org.keycloak.connections.jpa.entityprovider.JpaEntityProvider;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:chinnayya.nalla.careers@gmail.com">Chinnayya Naidu Nalla</a>
 */
public class CustomJPAEntityProvider implements JpaEntityProvider {

    @Override
    public List<Class<?>> getEntities() {
        return Collections.singletonList(UserNetworkPolicyEntity.class);
    }

    @Override
    public String getChangelogLocation() {
        return "META-INF/custom-jpa-changelog-master.xml";
    }

    @Override
    public String getFactoryId() {
        return CustomJPAEntityProviderFactory.ID;
    }

    @Override
    public void close() { }

}
