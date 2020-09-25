package server.springapi.security;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.JwtConfig;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import server.webapi.SecurityConstants;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

public class CustomTokenFilter extends UsernamePasswordAuthenticationFilter {

    public static final String BAD_TOKEN_ERROR = "Error while parsing token";
    public static final String IO_EXCEPTION_ERROR = "IOException occured while parsing token";
    public static final String MISSING_TOKEN_ERROR = "Token missing from request";

    private static final String JWT_HEADER = "jwt";


    private Log logger = LogFactory.getLog(CustomTokenFilter.class);
    private AuthenticationManager authenticationManager;
    public CustomTokenFilter(AuthenticationManager manager) {
        this.authenticationManager = manager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("Attempting Authentication");
        try {
            TokenPojo token = new ObjectMapper().readValue(request.getInputStream(), TokenPojo.class);
            if(token.getTokenId() == null) {
                throw new BadCredentialsException(MISSING_TOKEN_ERROR);
            }
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token.getTokenId(), "", new ArrayList<>()));
        } catch (JsonParseException | JsonMappingException e) {
            throw new BadCredentialsException(BAD_TOKEN_ERROR);
        } catch (IOException e) {
            throw new BadCredentialsException(IO_EXCEPTION_ERROR);
        } catch (BadCredentialsException e) {
            throw e;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = generateToken(JwtConfig.ISSUER, (String)authResult.getPrincipal(), JwtConfig.TTL);
        logger.info(String.format("Successful authentication on %s with token %s", authResult.getPrincipal(), token));
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        logger.info("Failed authentication");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.addHeader("message", failed.getMessage());
    }

    private static String generateToken(String issuer, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long now = System.currentTimeMillis();
        Date nowDate = new Date(now);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JwtConfig.SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(nowDate)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        if (ttlMillis > 0) {
            long expMillis = now + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        String JWT = builder.compact();
        return JWT;
    }

}
