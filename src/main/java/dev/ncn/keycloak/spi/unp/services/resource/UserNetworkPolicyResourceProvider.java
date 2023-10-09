package dev.ncn.keycloak.spi.unp.services.resource;

import dev.ncn.keycloak.spi.unp.representations.UserNetworkPolicyRepresentation;
import dev.ncn.keycloak.spi.unp.services.UserNetworkPolicyResourceProviderService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import java.util.List;

/**
 * @author <a href="mailto:chinnayya.nalla.careers@gmail.com">Chinnayya Naidu Nalla</a>
 */
public class UserNetworkPolicyResourceProvider implements RealmResourceProvider {

    protected KeycloakSession session;
    protected UserNetworkPolicyResourceProviderService service = new UserNetworkPolicyResourceProviderService();

    public UserNetworkPolicyResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return this;
    }

    @Override
    public void close() { }

    @POST
    @NoCache
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUserNetworkPolicyForUser(@PathParam("userId") String userId, final List<UserNetworkPolicyRepresentation> userNetworkPolicyRepresentations) {
        service.saveUserNetworkPolicyForUser(session, userId, userNetworkPolicyRepresentations);
        return Response.ok().build();

    }

    @GET
    @NoCache
    @Path("users/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserNetworkPolicyRepresentation> fetchUserNetworkPolicyForUser(@PathParam("userId") String userId) {
        return service.fetchUserNetworkPolicyForUser(session, userId);
    }

}
