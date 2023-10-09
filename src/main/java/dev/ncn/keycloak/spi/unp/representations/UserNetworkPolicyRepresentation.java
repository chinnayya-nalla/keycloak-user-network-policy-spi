package dev.ncn.keycloak.spi.unp.representations;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author <a href="mailto:chinnayya.nalla.careers@gmail.com">Chinnayya Naidu Nalla</a>
 */

@Setter
@Getter
public class UserNetworkPolicyRepresentation implements Serializable {

    protected String id;
    protected String userId;
    protected String networkAddress;
    protected String subnetMask;


}
