package com.example.authorizationserver.scim.api.resource.mapper;

import com.example.authorizationserver.scim.api.resource.*;
import com.example.authorizationserver.scim.model.ScimUserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

import static com.example.authorizationserver.scim.api.ScimRestController.GROUP_ENDPOINT;

@Component
public class ScimUserResourceMapper {

    public ScimUserResource mapEntityToResource(ScimUserEntity scimUserEntity, String location) {
        return new ScimUserResource(
                new ScimMetaResource("User", null, null,
                        scimUserEntity.getVersion().toString(), location),
                scimUserEntity.getIdentifier(), scimUserEntity.getExternalId(), scimUserEntity.getUserName(),
                scimUserEntity.getFamilyName(), scimUserEntity.getGivenName(), scimUserEntity.getMiddleName(),
                scimUserEntity.getHonorificPrefix(), scimUserEntity.getHonorificSuffix(), scimUserEntity.getNickName(),
                scimUserEntity.getProfileUrl(), scimUserEntity.getTitle(), scimUserEntity.getUserType(),
                scimUserEntity.getPreferredLanguage(), scimUserEntity.getLocale(), scimUserEntity.getTimezone(),
                scimUserEntity.isActive(),
                scimUserEntity.getEmails().stream().map(e -> new ScimEmailResource(e.getEmail(), e.getType(), e.isPrimaryEmail())).collect(Collectors.toSet()),
                scimUserEntity.getPhoneNumbers().stream().map(p -> new ScimPhoneNumberResource(p.getPhone(), p.getType())).collect(Collectors.toSet()),
                scimUserEntity.getIms().stream().map(i -> new ScimImsResource(i.getIms(), i.getType())).collect(Collectors.toSet()),
                scimUserEntity.getPhotos().stream().map(p -> new ScimPhotoResource(p.getPhotoUrl(), p.getType())).collect(Collectors.toSet()),
                scimUserEntity.getAddresses().stream().map(a -> new ScimAddressResource(a.getStreetAddress(), a.getLocality(), a.getRegion(), a.getPostalCode(), a.getCountry(), a.getType(), a.isPrimaryAddress())).collect(Collectors.toSet()),
                scimUserEntity.getGroups().stream().map(g -> {
                    URI groupLocation =
                            ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path(GROUP_ENDPOINT + "/{groupId}")
                                    .buildAndExpand(g.getGroup().getIdentifier())
                                    .toUri();
                    return new ScimGroupRefResource(g.getGroup().getIdentifier(), groupLocation, g.getGroup().getDisplayName());
                }).collect(Collectors.toSet()),
                scimUserEntity.getEntitlements(), scimUserEntity.getRoles(), scimUserEntity.getX509Certificates());
    }
}
