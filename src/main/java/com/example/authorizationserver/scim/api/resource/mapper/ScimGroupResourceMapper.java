package com.example.authorizationserver.scim.api.resource.mapper;

import com.example.authorizationserver.scim.api.resource.ScimGroupResource;
import com.example.authorizationserver.scim.api.resource.ScimMetaResource;
import com.example.authorizationserver.scim.model.ScimGroupEntity;
import org.springframework.stereotype.Component;

@Component
public class ScimGroupResourceMapper {

    public ScimGroupResource mapEntityToResource(ScimGroupEntity scimGroupEntity, String location) {
        return new ScimGroupResource(new
                ScimMetaResource("Group", scimGroupEntity.getCreatedDate().isPresent()? scimGroupEntity.getCreatedDate().get() : null,
                scimGroupEntity.getLastModifiedDate().isPresent() ? scimGroupEntity.getLastModifiedDate().get() : null,
                scimGroupEntity.getVersion().toString(), location), scimGroupEntity.getIdentifier(),
                scimGroupEntity.getExternalId(), scimGroupEntity.getDisplayName(), scimGroupEntity.getMembers());
    }

}
