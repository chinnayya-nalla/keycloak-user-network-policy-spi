package dev.ncn.keycloak.spi.unp.helper;

import jakarta.ws.rs.NotAuthorizedException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;

import java.util.Objects;

public class AuthenticationHelper {

    public static void checkRealmAccess(KeycloakSession session) {
        AuthenticationManager.AuthResult authResult = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
        if (Objects.isNull(authResult)) {
            throw new NotAuthorizedException("Bearer");
        }
    }

}
