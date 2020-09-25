package server.springapi.security;

import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import server.webapi.CheckJwtResponse;
import server.webapi.SecurityConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomAuthorizationFilter extends BasicAuthenticationFilter {

    public static final String EXPIRED_JWT_ERROR = "Jwt has expired";
    public static final String MALFORMED_JWT_ERROR = "Jwt is malformed";

    public CustomAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(getAuthentication(request, response));
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("asdf", null, new ArrayList<>()));
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String jwt = request.getHeader(SecurityConstants.HEADER_STRING);
        JwtResponse res = checkJWT(jwt);
        switch(res.status) {
            case EXPIRED:
                response.addHeader("message", EXPIRED_JWT_ERROR);
                return null;
            case MALFORMED:
                response.addHeader("message", MALFORMED_JWT_ERROR);
                return null;
        }
        return new UsernamePasswordAuthenticationToken(res.user, null, new ArrayList<>());

    }

    private static JwtResponse checkJWT(String jwt) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(config.JwtConfig.SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        } catch(SignatureException | MalformedJwtException | IllegalArgumentException e) {
           return new JwtResponse(JwtResponse.Status.MALFORMED, null);
        } catch(ExpiredJwtException e) {
            return new JwtResponse(JwtResponse.Status.EXPIRED, null);
        }
        return new JwtResponse(JwtResponse.Status.OK, claims.getSubject());
    }

    private static class JwtResponse {
        enum Status {
            OK, MALFORMED, EXPIRED
        }
        private Status status;
        private String user;
        public JwtResponse(Status status, String user) {
            this.status = status;
            this.user = user;
        }
    }

}
