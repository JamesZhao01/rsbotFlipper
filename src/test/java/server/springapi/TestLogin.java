package server.springapi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.springapi.security.CustomAuthenticationProvider;
import server.springapi.security.CustomTokenFilter;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestLogin {

    private Log logger = LogFactory.getLog(TestLogin.class);

    @Autowired
    RsbotRestController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void testFailedLoginBadFormat() {
        ResponseEntity<String> res = this.restTemplate.postForEntity("/login", "a/{", String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(res.getHeaders().get("message").get(0)).isEqualTo(CustomTokenFilter.BAD_TOKEN_ERROR);
    }

    @Test
    public void testFailedLoginMissingParams() {
        ResponseEntity<String> res = this.restTemplate.postForEntity("/login", "{}", String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(res.getHeaders().get("message").get(0)).isEqualTo(CustomTokenFilter.MISSING_TOKEN_ERROR);
    }

    @Test
    public void testFailedLoginEmptyToken() {
        ResponseEntity<String> res = this.restTemplate.postForEntity("/login", "{\"tokenId\":\"\"}", String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(res.getHeaders().get("message").get(0)).isEqualTo(CustomAuthenticationProvider.DEFAULT_BAD_CREDENTIALS);
    }

    @Test
    public void testFailedLoginAuth() {
        ResponseEntity<String> res = this.restTemplate.postForEntity("/login", "{\"username\": \"doom\", \"password\": \"doot\"}", String.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(res.getHeaders().get("message").get(0)).isEqualTo(CustomTokenFilter.BAD_TOKEN_ERROR);
    }

    @Test
    public void testLoginOptionsCors() {
        Set<HttpMethod> res = this.restTemplate.optionsForAllow("/login");
    }


}
