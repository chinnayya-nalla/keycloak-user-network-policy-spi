package dev.ncn.keycloak.spi.unp.services;

import dev.ncn.keycloak.spi.unp.connections.jpa.entity.UserNetworkPolicyEntity;
import dev.ncn.keycloak.spi.unp.representations.UserNetworkPolicyRepresentation;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:chinnayya.nalla.careers@gmail.com">Chinnayya Naidu Nalla</a>
 */

@Slf4j
public class UserNetworkPolicyResourceProviderService {

    public Response fetchUserNetworkPolicyForUser(KeycloakSession session, String userId) {

        log.info("Executing Fetch Network Policy for User {} ", userId);

        UserModel userModel = session.users().getUserById(session.getContext().getRealm(), userId);
        if(Objects.isNull(userModel)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<UserNetworkPolicyRepresentation> userNetworkPolicyRepresentations = new ArrayList<>();
        List<UserNetworkPolicyEntity> userNetworkPolicyEntities = getUserNetworkPolicyForUser(session, userId);

        if(CollectionUtil.isEmpty(userNetworkPolicyEntities)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        userNetworkPolicyEntities.forEach(userNetworkPolicyEntity -> {
            UserNetworkPolicyRepresentation userNetworkPolicyRepresentation = new UserNetworkPolicyRepresentation();
            userNetworkPolicyRepresentation.setId(userNetworkPolicyEntity.getId());
            userNetworkPolicyRepresentation.setUserId(userNetworkPolicyEntity.getUserId());
            userNetworkPolicyRepresentation.setSubnetMask(userNetworkPolicyEntity.getSubnetMask());
            userNetworkPolicyRepresentation.setNetworkAddress(userNetworkPolicyEntity.getNetworkAddress());
            userNetworkPolicyRepresentations.add(userNetworkPolicyRepresentation);
        });


        return Response.ok().entity(userNetworkPolicyRepresentations).build();

    }


    public Response saveUserNetworkPolicyForUser(KeycloakSession session, String userId, List<UserNetworkPolicyRepresentation> userNetworkPolicyRepresentations) {

        log.info("Executing Save Network Policy for User {} ", userId);

        UserModel userModel = session.users().getUserById(session.getContext().getRealm(), userId);
        if(Objects.isNull(userModel) || CollectionUtil.isEmpty(userNetworkPolicyRepresentations)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        EntityManager entityManager = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        Map<String, UserNetworkPolicyEntity> userNetworkPolicyEntityById = getUserNetworkPolicyForUser(session, userId).stream().collect(Collectors.toMap(UserNetworkPolicyEntity::getId, Function.identity()));

        userNetworkPolicyRepresentations.forEach(userNetworkPolicyRepresentation -> {
            UserNetworkPolicyEntity userNetworkPolicyEntity = new UserNetworkPolicyEntity();

            if(userNetworkPolicyEntityById.containsKey(userNetworkPolicyRepresentation.getId())) {
                userNetworkPolicyEntity = userNetworkPolicyEntityById.get(userNetworkPolicyRepresentation.getId());
                userNetworkPolicyEntityById.remove(userNetworkPolicyRepresentation.getId());
            } else {
                userNetworkPolicyEntity.setUserId(userId);
                userNetworkPolicyEntity.setId(KeycloakModelUtils.generateId());
            }

            userNetworkPolicyEntity.setSubnetMask(userNetworkPolicyRepresentation.getSubnetMask());
            userNetworkPolicyEntity.setNetworkAddress(userNetworkPolicyRepresentation.getNetworkAddress());

            entityManager.persist(userNetworkPolicyEntity);
            entityManager.flush();
        });


        if(userNetworkPolicyEntityById.size() > 0) {
            userNetworkPolicyEntityById.forEach((key , value) -> {
                entityManager.remove(value);
            });
        }

        return Response.noContent().build();
    }


    public void removeUserNetworkPolicy(KeycloakSession session, String userId) {

        log.info("Executing Remove Network Policy for User {} ", userId);

        EntityManager entityManager = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        int response = entityManager.createNamedQuery("removeAllUserNetworkPoliciesByUser").setParameter("userId", userId).executeUpdate();

        log.info("Removed Network Policy for User {} - {}", userId, response);

    }

    public List<UserNetworkPolicyEntity> getUserNetworkPolicyForUser(KeycloakSession session, String userId) {
        EntityManager entityManager = session.getProvider(JpaConnectionProvider.class).getEntityManager();
        return entityManager.createNamedQuery("getAllUserNetworkPoliciesByUser", UserNetworkPolicyEntity.class).setParameter("userId", userId).getResultList();
    }


}
