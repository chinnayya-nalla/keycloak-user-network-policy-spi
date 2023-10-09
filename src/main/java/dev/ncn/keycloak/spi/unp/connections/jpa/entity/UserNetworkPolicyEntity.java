package dev.ncn.keycloak.spi.unp.connections.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.models.jpa.entities.UserEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_NETWORK_POLICY")
@NamedQueries({
        @NamedQuery(name = "getAllUserNetworkPoliciesByUser", query = "SELECT u from UserNetworkPolicyEntity u where u.user.id = :userId"),
        @NamedQuery(name = "removeAllUserNetworkPoliciesByUser", query = "DELETE FROM UserNetworkPolicyEntity u WHERE u.user.id = :userId")
})
@Setter
@Getter
public class UserNetworkPolicyEntity implements Serializable {

    @Id
    @Column(name = "ID", length = 36)
    @Access(AccessType.PROPERTY)
    protected String id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected UserEntity user;

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
