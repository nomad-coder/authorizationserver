package com.example.authorizationserver.security.user;

import com.example.authorizationserver.scim.model.ScimUserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<ScimUserEntity> {

    @Override
    public Optional<ScimUserEntity> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(ScimUserEntity.class::cast);
    }
}
