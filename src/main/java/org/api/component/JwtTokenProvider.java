package org.api.component;

import io.jsonwebtoken.*;
import org.api.annotation.LogExecutionTime;
import org.api.constants.ConstantJwt;
import org.api.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@LogExecutionTime
public class JwtTokenProvider {

    @Autowired
    private Environment evn;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateJwtToken(Authentication authentication) {
        CustomUserDetailsService userPrincipal = (CustomUserDetailsService) authentication.getPrincipal();
        String authority = userPrincipal.getAuthorities()
                .stream()
                .findFirst()
                .orElse(null)
                .getAuthority();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + ConstantJwt.ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .claim("role", authority)
                .signWith(SignatureAlgorithm.HS512, evn.getProperty(ConstantJwt.SIGNING_KEY))
                .compact();
    }

    public String generatePasswordResetToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date((new Date()).getTime() + ConstantJwt.PASSWORD_RESET_EXPIRATION_TIME * 1000))
                .signWith(SignatureAlgorithm.HS512, evn.getProperty(ConstantJwt.SIGNING_KEY))
                .compact();
    }

    public String getMailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(evn.getProperty(ConstantJwt.SIGNING_KEY)).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(evn.getProperty(ConstantJwt.SIGNING_KEY)).parseClaimsJws(token).getBody();
        Date tokenExpirationDate = claims.getExpiration();
        Date today = new Date();
        return tokenExpirationDate.before(today);
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(evn.getProperty(ConstantJwt.SIGNING_KEY)).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token" + ex);
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token" + ex);
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token" + ex);
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty." + ex);
        }
        return false;
    }
}