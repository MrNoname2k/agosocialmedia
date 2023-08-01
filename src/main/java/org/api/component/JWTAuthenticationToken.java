package org.api.component;

import org.api.entities.UserEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthenticationToken extends AbstractAuthenticationToken implements Authentication {
    private String token;
    private UserEntity principal;

    public JWTAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String token, UserEntity principal) {
        super(authorities);
        this.token = token;
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
