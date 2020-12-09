package com.example.authorizationserver.scim.api;

import com.example.authorizationserver.scim.api.resource.*;
import com.example.authorizationserver.scim.api.resource.mapper.*;
import com.example.authorizationserver.scim.model.ScimUserEntity;
import com.example.authorizationserver.scim.service.ScimService;
import com.example.authorizationserver.security.user.EndUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@RestController
public class ScimRestController {

    public static final String USER_ENDPOINT = "/api/Users";
    public static final String GROUP_ENDPOINT = "/api/Groups";
    public static final String ME_ENDPOINT = "/api/Me";

    private final ScimService scimService;

    private final ScimUserResourceMapper scimUserResourceMapper;

    private final ScimUserListResourceMapper scimUserListResourceMapper;

    private final ScimGroupResourceMapper scimGroupResourceMapper;

    private final ScimGroupListResourceMapper scimGroupListResourceMapper;

    private final CreateScimUserResourceMapper createScimUserResourceMapper;

    public ScimRestController(ScimService scimService, ScimUserResourceMapper scimUserResourceMapper, ScimUserListResourceMapper scimUserListResourceMapper, ScimGroupResourceMapper scimGroupResourceMapper, ScimGroupListResourceMapper scimGroupListResourceMapper, CreateScimUserResourceMapper createScimUserResourceMapper) {
        this.scimService = scimService;
        this.scimUserResourceMapper = scimUserResourceMapper;
        this.scimUserListResourceMapper = scimUserListResourceMapper;
        this.scimGroupResourceMapper = scimGroupResourceMapper;
        this.scimGroupListResourceMapper = scimGroupListResourceMapper;
        this.createScimUserResourceMapper = createScimUserResourceMapper;
    }

    @PostMapping(USER_ENDPOINT)
    public ResponseEntity<ScimUserListResource> createUser(@Valid @RequestBody CreateScimUserResource createScimUserResource) {
        ScimUserEntity scimUserEntity = createScimUserResourceMapper.mapResourceToEntity(createScimUserResource);
    }

    @GetMapping(USER_ENDPOINT)
    public List<ScimUserListResource> getAllUsers() {
        return scimService.findAllUsers().stream().map(ue -> {
            URI location =
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(USER_ENDPOINT + "/{userId}")
                        .buildAndExpand(ue.getIdentifier())
                        .toUri();
            return scimUserListResourceMapper.mapEntityToResource(ue, location.toASCIIString());}).collect(Collectors.toList());
    }

    @GetMapping(USER_ENDPOINT + "/{userId}")
    public ResponseEntity<ScimUserResource> getUser(@PathVariable("userId") UUID userIdentifier) {
        return scimService.findUserByIdentifier(userIdentifier).map(ue -> {
            URI location =
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path(USER_ENDPOINT + "/{userId}")
                            .buildAndExpand(ue.getIdentifier())
                            .toUri();
            return ResponseEntity.ok().location(location).body(scimUserResourceMapper.mapEntityToResource(ue, location.toASCIIString()));})
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(ME_ENDPOINT)
    public ResponseEntity<ScimUserResource> getAuthenticatedUser(@AuthenticationPrincipal EndUserDetails user) {
        return scimService.findUserByIdentifier(user.getIdentifier()).map(ue -> {
            URI location =
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path(USER_ENDPOINT + "/{userId}")
                            .buildAndExpand(ue.getIdentifier())
                            .toUri();
            return ResponseEntity.ok().location(location).body(scimUserResourceMapper.mapEntityToResource(ue, location.toASCIIString()));})
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(GROUP_ENDPOINT)
    public List<ScimGroupListResource> getAllGroups() {
        return scimService.findAllGroups().stream().map(ue -> {
            URI location =
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path(GROUP_ENDPOINT + "/{groupId}")
                            .buildAndExpand(ue.getIdentifier())
                            .toUri();
            return scimGroupListResourceMapper.mapEntityToResource(ue, location.toASCIIString());}).collect(Collectors.toList());
    }

    @GetMapping(GROUP_ENDPOINT + "/{groupId}")
    public ResponseEntity<ScimGroupResource> getGroup(@PathVariable("groupId") UUID groupIdentifier) {
        return scimService.findGroupByIdentifier(groupIdentifier).map(ue -> {
            URI location =
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path(GROUP_ENDPOINT + "/{groupId}")
                            .buildAndExpand(ue.getIdentifier())
                            .toUri();
            return ResponseEntity.ok().location(location).body(
                    scimGroupResourceMapper.mapEntityToResource(ue, location.toASCIIString()));})
                .orElse(ResponseEntity.notFound().build());
    }

}
