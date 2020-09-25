package server.springapi.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jndi.toolkit.url.Uri;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import sun.security.x509.OtherName;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    public static final String DEFAULT_BAD_CREDENTIALS = "Bad credentials";

    private static final String OAUTH_GOOGLE_URL= "https://oauth2.googleapis.com/tokeninfo";


    private Log logger = LogFactory.getLog(CustomAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpPost httpRequest = new HttpPost(OAUTH_GOOGLE_URL);
        try {
            // build URI
            URIBuilder uriBuilder= new URIBuilder(httpRequest.getURI());
            uriBuilder.addParameter("id_token", authentication.getName()).build();
            httpRequest.setURI(uriBuilder.build());
        } catch (URISyntaxException e) {
            return null;
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse httpResponse = httpClient.execute(httpRequest)) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            // if valid response
            if(statusCode == HttpStatus.SC_OK) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                // log response body
                logger.info("\tGoogle OAuth response body: " + responseBody);

                // read json
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                // generate a jwt
                return new UsernamePasswordAuthenticationToken(jsonNode.get("email"), "", Collections.emptyList());
            } else {
                logger.info(String.format("Failed to Authenticate: %s", authentication.getName()));
                return null;
            }
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    public boolean supports(Class<?> aClass) {

        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
