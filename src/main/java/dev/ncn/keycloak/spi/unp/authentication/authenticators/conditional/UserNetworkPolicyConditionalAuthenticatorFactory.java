package dev.ncn.keycloak.spi.unp.authentication.authenticators.conditional;

import org.keycloak.Config;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:chinnayya.nalla.careers@gmail.com">Chinnayya Naidu Nalla</a>
 */

public class UserNetworkPolicyConditionalAuthenticatorFactory implements ConditionalAuthenticatorFactory  {

    public static final String CONF_NEGATE = "negate";
    public static final String DEFAULT_ALLOW_ALL = "default_network_policy";
    public static final String ID = "conditional-user-network-policy";

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED
    };

    @Override
    public ConditionalAuthenticator getSingleton() {
        return UserNetworkPolicyConditionalAuthenticator.SINGLETON;
    }

    @Override
    public String getDisplayType() {
        return "Condition - User Network Policy Authenticator";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Flow is executed only if user Network Policy matches.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {

        ProviderConfigProperty defaultPolicy = new ProviderConfigProperty();
        defaultPolicy.setDefaultValue(true);
        defaultPolicy.setName(DEFAULT_ALLOW_ALL);
        defaultPolicy.setLabel("Default Policy");
        defaultPolicy.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        defaultPolicy.setHelpText("When this is true, then the condition will evaluate Network Policy Match to true. When this is false, the condition will evaluate Network Policy Match to false");

        ProviderConfigProperty negateOutput = new ProviderConfigProperty();
        negateOutput.setName(CONF_NEGATE);
        negateOutput.setDefaultValue(false);
        negateOutput.setLabel("Negate output");
        negateOutput.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        negateOutput.setHelpText("Apply a not to the check result");

        return Arrays.asList(defaultPolicy, negateOutput);

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
