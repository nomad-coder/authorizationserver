package com.example.authorizationserver.scim.api.resource;

import com.example.authorizationserver.scim.model.ScimUserGroupEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ScimGroupResource extends ScimGroupListResource {

    private Set<ScimUserGroupEntity> members = new HashSet<>();

    public ScimGroupResource() {
    }

    public ScimGroupResource(ScimMetaResource meta, UUID identifier, String externalId, String displayName, Set<ScimUserGroupEntity> members) {
        super(meta, identifier, externalId, displayName);
        this.members = members;
    }

    public Set<ScimUserGroupEntity> getMembers() {
        return members;
    }

    public void setMembers(Set<ScimUserGroupEntity> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("members", members)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ScimGroupResource that = (ScimGroupResource) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(members, that.members)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(members)
                .toHashCode();
    }
}
