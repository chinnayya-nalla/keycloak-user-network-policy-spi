package dev.ncn.keycloak.spi.unp.authentication.authenticators.conditional;

import dev.ncn.keycloak.spi.unp.connections.jpa.entity.UserNetworkPolicyEntity;
import dev.ncn.keycloak.spi.unp.services.UserNetworkPolicyResourceProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.SubnetUtils;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.common.util.CollectionUtil;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.List;
import java.util.Objects;

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
        boolean isNetworkAccessMatched = false;
        String userIPAddress = context.getConnection().getRemoteAddr();

        AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();
        if (Objects.nonNull(authConfig) && Objects.nonNull(authConfig.getConfig())) {

            boolean negateOutput = Boolean.parseBoolean(authConfig.getConfig().get(UserNetworkPolicyConditionalAuthenticatorFactory.CONF_NEGATE));
            boolean defaultAllowAll = Boolean.parseBoolean(authConfig.getConfig().get(UserNetworkPolicyConditionalAuthenticatorFactory.DEFAULT_ALLOW_ALL));

            List<UserNetworkPolicyEntity> userNetworkPolicyEntities = service.getUserNetworkPolicyForUser(context.getSession(), context.getUser().getId());
            if(CollectionUtil.isEmpty(userNetworkPolicyEntities) && defaultAllowAll) {
                isNetworkAccessMatched = true;
            }

            for (UserNetworkPolicyEntity userNetworkPolicyEntity : userNetworkPolicyEntities) {
                log.info("Validating User {} With User IP Address {} for Configured Network Address {} and Subnet Mask {}", user.getId(), userIPAddress, userNetworkPolicyEntity.getNetworkAddress(), userNetworkPolicyEntity.getSubnetMask());
                SubnetUtils subnetUtils = new SubnetUtils(userNetworkPolicyEntity.getNetworkAddress(), userNetworkPolicyEntity.getSubnetMask());
                subnetUtils.setInclusiveHostCount(true);
                if(subnetUtils.getInfo().isInRange(userIPAddress)) {
                    isNetworkAccessMatched = true;
                    break;
                }
            }

            return negateOutput != isNetworkAccessMatched;

        }

        return true;

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
