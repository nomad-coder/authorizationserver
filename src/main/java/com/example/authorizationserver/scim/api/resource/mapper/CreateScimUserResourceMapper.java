package com.example.authorizationserver.scim.api.resource.mapper;

import com.example.authorizationserver.scim.api.resource.*;
import com.example.authorizationserver.scim.model.ScimEmailEntity;
import com.example.authorizationserver.scim.model.ScimPhoneNumberEntity;
import com.example.authorizationserver.scim.model.ScimUserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

import static com.example.authorizationserver.scim.api.ScimRestController.GROUP_ENDPOINT;

@Component
public class CreateScimUserResourceMapper {

    public CreateScimUserResource mapEntityToResource(ScimUserEntity scimUserEntity) {
        return new CreateScimUserResource(
                new ScimMetaResource("User", null, null,
                        "0", null),
                scimUserEntity.getIdentifier(), scimUserEntity.getExternalId(), scimUserEntity.getUserName(),
                scimUserEntity.getFamilyName(), scimUserEntity.getGivenName(), scimUserEntity.getMiddleName(),
                scimUserEntity.getHonorificPrefix(), scimUserEntity.getHonorificSuffix(), scimUserEntity.getNickName(),
                scimUserEntity.getProfileUrl(), scimUserEntity.getTitle(), scimUserEntity.getUserType(),
                scimUserEntity.getPreferredLanguage(), scimUserEntity.getLocale(), scimUserEntity.getTimezone(),
                scimUserEntity.isActive(), scimUserEntity.getPassword(),
                scimUserEntity.getEmails() != null ? scimUserEntity.getEmails().stream().map(e -> new ScimEmailResource(e.getEmail(), e.getType(), e.isPrimaryEmail())).collect(Collectors.toSet()) : null,
                scimUserEntity.getPhoneNumbers() != null ? scimUserEntity.getPhoneNumbers().stream().map(p -> new ScimPhoneNumberResource(p.getPhone(), p.getType())).collect(Collectors.toSet()) : null,
                scimUserEntity.getIms() != null ? scimUserEntity.getIms().stream().map(i -> new ScimImsResource(i.getIms(), i.getType())).collect(Collectors.toSet()) : null,
                scimUserEntity.getPhotos() != null ? scimUserEntity.getPhotos().stream().map(p -> new ScimPhotoResource(p.getPhotoUrl(), p.getType())).collect(Collectors.toSet()) : null,
                scimUserEntity.getAddresses() != null ? scimUserEntity.getAddresses().stream().map(a -> new ScimAddressResource(a.getStreetAddress(), a.getLocality(), a.getRegion(), a.getPostalCode(), a.getCountry(), a.getType(), a.isPrimaryAddress())).collect(Collectors.toSet()) : null,
                scimUserEntity.getGroups() != null ? scimUserEntity.getGroups().stream().map(g ->
                    new ScimGroupRefResource(g.getGroup().getIdentifier(), null, g.getGroup().getDisplayName())
                ).collect(Collectors.toSet()) : null,
                scimUserEntity.getEntitlements(), scimUserEntity.getRoles(), scimUserEntity.getX509Certificates());
    }

    public ScimUserEntity mapResourceToEntity(CreateScimUserResource createScimUserResource) {

        return new ScimUserEntity(createScimUserResource.getIdentifier(),
                createScimUserResource.getExternalId(), createScimUserResource.getUserName(), createScimUserResource.getFamilyName(),
                createScimUserResource.getGivenName(), createScimUserResource.getMiddleName(), createScimUserResource.getHonorificPrefix(), createScimUserResource.getHonorificSuffix(),
                createScimUserResource.getNickName(), createScimUserResource.getProfileUrl(), createScimUserResource.getTitle(), createScimUserResource.getUserType(),
                createScimUserResource.getPreferredLanguage(), createScimUserResource.getLocale(), createScimUserResource.getTimezone(), createScimUserResource.isActive(),
                createScimUserResource.getPassword(),
                createScimUserResource.getEmails() != null ? createScimUserResource.getEmails().stream().map(e -> new ScimEmailEntity(e.getValue(), e.getType(), e.isPrimary())).collect(Collectors.toSet()) : null,
                createScimUserResource.getPhoneNumbers() != null ? createScimUserResource.getPhoneNumbers().stream().map(p -> new ScimPhoneNumberEntity(p.getValue(), p.getType())).collect(Collectors.toSet()) : null),

        )
    }
}
