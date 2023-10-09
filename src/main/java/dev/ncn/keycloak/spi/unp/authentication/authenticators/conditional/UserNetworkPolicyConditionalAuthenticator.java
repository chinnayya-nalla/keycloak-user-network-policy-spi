package dev.ncn.keycloak.spi.unp.authentication.authenticators.conditional;

import dev.ncn.keycloak.spi.unp.connections.jpa.entity.UserNetworkPolicyEntity;
import dev.ncn.keycloak.spi.unp.services.UserNetworkPolicyResourceProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.SubnetUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.utils.StringUtil;

import java.util.List;

/**
 * @author <a href="mailto:chinnayya.nalla.careers@gmail.com">Chinnayya Naidu Nalla</a>
 */

@Slf4j
public class UserNetworkPolicyConditionalAuthenticator implements ConditionalAuthenticator {

    public static final UserNetworkPolicyConditionalAuthenticator SINGLETON = new UserNetworkPolicyConditionalAuthenticator();
    private final UserNetworkPolicyResourceProviderService service = new UserNetworkPolicyResourceProviderService();

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {

        UserModel user = context.getUser();
        String userIPAddress = context.getConnection().getRemoteAddr();
        if(StringUtil.isBlank(userIPAddress)) {
            return false;
        }

        List<UserNetworkPolicyEntity> userNetworkPolicyEntities = service.getUserNetworkPolicyForUser(context.getSession(), context.getUser().getId());
        if(CollectionUtil.isEmpty(userNetworkPolicyEntities)) {
            return false;
        }

        boolean isInvalidNetworkAccess = true;
        for (UserNetworkPolicyEntity userNetworkPolicyEntity : userNetworkPolicyEntities) {
            log.info("Validating User {} With User IP Address {} for Configured Network Address {} and Subnet Mask {}", user.getId(), userIPAddress, userNetworkPolicyEntity.getNetworkAddress(), userNetworkPolicyEntity.getSubnetMask());
            SubnetUtils subnetUtils = new SubnetUtils(userNetworkPolicyEntity.getNetworkAddress(), userNetworkPolicyEntity.getSubnetMask());
            subnetUtils.setInclusiveHostCount(true);
            if(subnetUtils.getInfo().isInRange(userIPAddress)) {
                isInvalidNetworkAccess = false;
                break;
            }
        }

        return isInvalidNetworkAccess;

    }

    @Override
    public void action(AuthenticationFlowContext context) { }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) { }

    @Override
    public void close() { }

}
