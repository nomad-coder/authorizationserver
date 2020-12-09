package com.example.authorizationserver.scim.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class ScimUserGroupEntity extends AbstractPersistable<Long> implements Serializable {

    @NotNull
    @ManyToOne(optional = false)
    private ScimUserEntity user;

    @NotNull
    @ManyToOne(optional = false)
    private ScimGroupEntity group;

    public ScimUserGroupEntity() {
    }

    public ScimUserGroupEntity(ScimUserEntity user, ScimGroupEntity group) {
        this.user = user;
        this.group = group;
    }

    public ScimUserEntity getUser() {
        return user;
    }

    public void setUser(ScimUserEntity user) {
        this.user = user;
    }

    public ScimGroupEntity getGroup() {
        return group;
    }

    public void setGroup(ScimGroupEntity group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("user", user)
                .append("group", group)
                .toString();
    }
}
