package com.example.authorizationserver.scim.api;

import com.example.authorizationserver.scim.api.resource.CreateScimUserResource;
import com.example.authorizationserver.scim.api.resource.mapper.*;
import com.example.authorizationserver.scim.model.ScimEmailEntity;
import com.example.authorizationserver.scim.model.ScimGroupEntity;
import com.example.authorizationserver.scim.model.ScimUserEntity;
import com.example.authorizationserver.scim.model.ScimUserGroupEntity;
import com.example.authorizationserver.scim.service.ScimService;
import com.example.authorizationserver.security.client.RegisteredClientDetailsService;
import com.example.authorizationserver.security.user.EndUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(ScimRestController.class)
class ScimRestControllerIntegrationTest {

    @MockBean
    private EndUserDetailsService endUserDetailsService;

    @MockBean
    private RegisteredClientDetailsService registeredClientDetailsService;

    @MockBean
    private ScimService scimService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    static class TestConfig {
        @Bean
        ScimUserResourceMapper scimUserResourceMapper() {
            return new ScimUserResourceMapper();
        }

        @Bean
        ScimUserListResourceMapper scimUserListResourceMapper() {
            return new ScimUserListResourceMapper();
        }

        @Bean
        ScimGroupResourceMapper scimGroupResourceMapper() {
            return new ScimGroupResourceMapper();
        }

        @Bean
        ScimGroupListResourceMapper scimGroupListResourceMapper() {
            return new ScimGroupListResourceMapper();
        }
    }

    @BeforeEach
    public void setUp(
            WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation) {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(
                                documentationConfiguration(restDocumentation)
                                        .uris().withPort(9090).and().operationPreprocessors()
                                        .withRequestDefaults(prettyPrint())
                                        .withResponseDefaults(prettyPrint()))
                        .build();
    }

    @Test
    void users() throws Exception {
        ScimUserEntity scimUserEntity = new ScimUserEntity(
                UUID.randomUUID(),
                "mmuster",
                "Muster",
                "Max",
                true,
                "secret",
                Set.of(new ScimEmailEntity("test@example.com", "work", true)),
                null,
                Set.of("USER"));
        scimUserEntity.setGroups(
                Set.of(
                        new ScimUserGroupEntity(scimUserEntity,
                                new ScimGroupEntity(UUID.randomUUID(), "123", "test_group", null))));
        given(scimService.findAllUsers())
                .willReturn(
                        List.of(
                        ));
        mockMvc
                .perform(get(ScimRestController.USER_ENDPOINT))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("getAllUsers"));
    }

    @Test
    void user() throws Exception {
        UUID userIdentifier = UUID.randomUUID();
        ScimUserEntity scimUserEntity = new ScimUserEntity(
                userIdentifier,
                "mmuster",
                "Muster",
                "Max",
                true,
                "secret",
                Set.of(new ScimEmailEntity("test@example.com", "work", true)),
                null,
                Set.of("USER"));
        scimUserEntity.setGroups(
                Set.of(
                        new ScimUserGroupEntity(scimUserEntity,
                                new ScimGroupEntity(UUID.randomUUID(), "123", "test_group", null))));
        given(scimService.findUserByIdentifier(userIdentifier))
                .willReturn(
                        Optional.of(scimUserEntity));
        mockMvc
                .perform(get(ScimRestController.USER_ENDPOINT + "/{userid}", userIdentifier))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("getUser"));
    }

    @Test
    void create() throws Exception {
        UUID userIdentifier = UUID.randomUUID();
        ScimUserEntity scimUserEntity = new ScimUserEntity(
                userIdentifier,
                "mmuster",
                "Muster",
                "Max",
                true,
                "secret",
                Set.of(new ScimEmailEntity("test@example.com", "work", true)),
                null,
                Set.of("USER"));
        scimUserEntity.setGroups(
                Set.of(
                        new ScimUserGroupEntity(scimUserEntity,
                                new ScimGroupEntity(UUID.randomUUID(), "123", "test_group", null))));
        given(scimService.createUser(any())).willReturn(scimUserEntity);

        CreateScimUserResource createScimUserResource = new CreateScimUserResourceMapper().mapEntityToResource(scimUserEntity);
        mockMvc
                .perform(
                        post(ScimRestController.USER_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createScimUserResource)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("createUser"));
    }

    @Test
    void update() throws Exception {
        UUID userIdentifier = UUID.randomUUID();
        ScimUserEntity scimUserEntity = new ScimUserEntity(
                userIdentifier,
                "mmuster",
                "Muster",
                "Max",
                true,
                "secret",
                Set.of(new ScimEmailEntity("test@example.com", "work", true)),
                null,
                Set.of("USER"));
        scimUserEntity.setGroups(
                Set.of(
                        new ScimUserGroupEntity(scimUserEntity,
                                new ScimGroupEntity(UUID.randomUUID(), "123", "test_group", null))));
        given(scimService.updateUser(any())).willReturn(scimUserEntity);

        CreateScimUserResource createScimUserResource =  new CreateScimUserResourceMapper().mapEntityToResource(scimUserEntity);
        mockMvc
                .perform(
                        put(ScimRestController.USER_ENDPOINT + "/{userid}", userIdentifier)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createScimUserResource)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("updateUser"));
    }

    @Test
    void deleteUser() throws Exception {
        UUID userIdentifier = UUID.randomUUID();
        mockMvc
                .perform(delete(ScimRestController.USER_ENDPOINT + "/{userid}", userIdentifier))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("deleteUser"));
    }
}
