package dev.ncn.keycloak.spi.unp.connections.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author <a href="mailto:chinnayya.nalla.careers@gmail.com">Chinnayya Naidu Nalla</a>
 */
@Entity
@Table(name = "USER_NETWORK_POLICY")
@NamedQueries({
        @NamedQuery(name = "getAllUserNetworkPoliciesByUser", query = "SELECT u from UserNetworkPolicyEntity u where u.userId = :userId"),
        @NamedQuery(name = "removeAllUserNetworkPoliciesByUser", query = "DELETE FROM UserNetworkPolicyEntity u WHERE u.userId = :userId")
})
@Setter
@Getter
public class UserNetworkPolicyEntity implements Serializable {

    @Id
    @Column(name = "ID", length = 36)
    @Access(AccessType.PROPERTY)
    protected String id;

    @Column(name = "USER_ID")
    protected String userId;

    @Column(name = "NETWORK_ADDRESS")
    protected String networkAddress;

    @Column(name = "SUBNET_MASK")
    protected String subnetMask;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserNetworkPolicyEntity that = (UserNetworkPolicyEntity) o;
        if (!id.equals(that.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
