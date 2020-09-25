package com.jameszhao.rsbotFlipper.server.webapi;

import io.jsonwebtoken.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpApi {

//    private static Logger LOGGER = Logger.getLogger("HttpApi");
//
//    private static final int OK=200;
//    private static final int BAD_REQUEST=400;
//
//    private static final String OAUTH_GOOGLE_URL= "https://oauth2.googleapis.com/tokeninfo";
//
//    private static final String EMPTY_TOKEN_ERROR = "empty_token";
//    private static final String EMPTY_TOKEN_ERROR_DESCRIPTION = "Token cannot be empty or nonexistent";
//    private static final String INVALID_TOKEN_ERROR = "invalid_token";
//    private static final String INVALID_TOKEN_ERROR_DESCRIPTION = "Token is invalid";
//
//    private static final String INVALID_JWT_TOKEN_ERROR="invalid_jwt_token";
//    private static final String INVALID_JWT_TOKEN_ERROR_DESCRIPTION ="JWT signature is invalid";
//    private static final String EXPIRED_JWT_TOKEN_ERROR="expired_jwt_token";
//    private static final String EXPIRED_JWT_TOKEN_ERROR_DESCRIPTION="JWT token has expired";
//
//    private static final String CONTENT_TYPE_HEADER="Content-Type";
//    private static final String CONTENT_TYPE_JSON = "application/json";
//
//    private MainServer mainServer;
//    private int port;
//    private static ObjectMapper objectMapper = new ObjectMapper();
//
//
//    public HttpApi(int port, MainServer mainServer){
//        this.port = port;
//        this.mainServer = mainServer;
//        if(LOGGER.getHandlers().length == 0)
//            LOGGER.addHandler(new ConsoleHandler());
//        LOGGER.fine("this needs to print");
//    }
//
//    public void stop() {
//        stop();
//    }
//
//    public void start() {
//
//        port(port);
//        before((req, res) -> {
//            res.header("Access-Control-Allow-Origin", "*");
//            res.header("Access-Control-Allow-Headers", "*");
//            MainServer.LOGGER.info("Received request at URI " + req.uri() + " with method " + req.requestMethod());
//        });
//        options("/*", this::handleOptions);
//        get("/com.jameszhao.rsbotFlipper.api", this::handleApi);
//        get("/login", this::handleLogin);
//        get("/command", this::handleCommand);
//    }
//
//    private Object handleOptions(Request request, Response response) {
//        response.body(request.body());
//        return "*";
//    }
//
//    private Object handleCommand(Request request, Response response) {
//        // check the JWT
//        String jwt = request.queryParams("jwt");
//        LOGGER.fine(String.format("JWT: %s", jwt));
//        // process empty jwt
//        if( jwt== null || jwt.equals("")) {
//            LOGGER.fine("\tJWT cannot be null or empty!");
//            response.status(BAD_REQUEST);
//            response.header(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON);
//            return generateErrorJson(new ErrorResponse(EMPTY_TOKEN_ERROR, EMPTY_TOKEN_ERROR_DESCRIPTION));
//        }
//        // check jwt validity
//        CheckJwtResponse checkJwtResponse = checkJWT(jwt);
//        if(checkJwtResponse.getResponseCode() == BAD_REQUEST) {
//            LOGGER.fine("\tJWT is malformed or invalid!");
//            response.status(BAD_REQUEST);
//            return checkJwtResponse.getErrorResponse();
//        }
//        response.status(OK);
//        response.header(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON);
//
//        String command = request.queryParams("command");
//
//        LOGGER.info(String.format("Received command %s", command));
//
//        String responseMessage = "";
//        try {
//            responseMessage = mainServer.getCommandParser().parseAndRun(command);
//        } catch (CommandParser.ParsingException e) {
//            responseMessage = String.format("A ParsingException was thrown!: %s", e.getMessage())  ;
//        } catch (CommandMap.CommandNotFoundException e) {
//            responseMessage = String.format("A CommandNotFoundException was thrown: %s", e.getMessage());
//        } finally{
//            return responseMessage;
//        }
//
//    }
//
//    /**
//     * This function serves as a handler function for the com.jameszhao.rsbotFlipper.api endpoint. It takes in an http request
//     * and checks for the proper parameters. If a bad request is made, a BAD_REQUEST response code is put in response
//     * @param request the http request
//     * @param response the http response
//     * @return the http response
//     */
//    private Object handleApi(Request request, Response response) throws JsonProcessingException {
//        // TODO abstract jwt logic away
//        // obtain string jwt
//        String jwt = request.queryParams("jwt");
//        LOGGER.fine(String.format("JWT: %s", jwt));
//        // process empty jwt
//        if(jwt.equals("") || jwt== null) {
//            LOGGER.fine("\tJWT cannot be null or empty!");
//            response.status(BAD_REQUEST);
//            response.header(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON);
//            return generateErrorJson(new ErrorResponse(EMPTY_TOKEN_ERROR, EMPTY_TOKEN_ERROR_DESCRIPTION));
//        }
//        // check jwt validity
//        CheckJwtResponse checkJwtResponse = checkJWT(jwt);
//        if(checkJwtResponse.getResponseCode() == BAD_REQUEST) {
//            LOGGER.fine("\tJWT is malformed or invalid!");
//            response.status(BAD_REQUEST);
//            return checkJwtResponse.getErrorResponse();
//        }
//        response.status(OK);
//        response.header(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON);
//
//        Claims claims = checkJwtResponse.getClaims();
//
//
////        //TODO add implementation for com.jameszhao.rsbotFlipper.api
////        //TODO add implementation for authorized users
//
//        Set<Map.Entry<AccountIdentifier, AccountInformation>> entries = mainServer.getDataHandler().fetchAll();
//        ArrayNode arrayNode = AccountObjMapper.MAPPER.createArrayNode();
//        for(Map.Entry<AccountIdentifier, AccountInformation> entry : entries) {
//            JsonNode info = AccountObjMapper.MAPPER.valueToTree(entry.getValue());
//            JsonNode iden = AccountObjMapper.MAPPER.valueToTree(entry.getKey());
//
//            ObjectNode objNode = AccountObjMapper.MAPPER.createObjectNode();
//            objNode.putPOJO("iden", iden);
//            objNode.putPOJO("info", info);
//            arrayNode.add(objNode);
//        }
//        return arrayNode.toString();
//    }
//
//    /**
//     * This function serves as a handler function for the login endpoint. It takes in a parameter of a login request
//     * with a Google OAuth token. This token is validated, and if it is valid, a JWT issued by this com.jameszhao.rsbotFlipper.server is sent
//     * in response.
//     * @param request the http request
//     * @param response the http response
//     * @return the response to the request
//     */
//    private Object handleLogin(Request request, Response response) {
//        // obtain tokenId from client(after google login)
//        String tokenId = request.queryParams("tokenId");
//        LOGGER.fine(String.format("tokenId: %s", tokenId));
//
//        // send error 400 if invalid tokenId
//        if(tokenId == null || tokenId.equals("")) {
//            LOGGER.fine("\ttokenId is null or invalid!");
//            response.status(400);
//            response.header(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON);
//            return generateErrorJson(new ErrorResponse(EMPTY_TOKEN_ERROR, EMPTY_TOKEN_ERROR_DESCRIPTION));
//        }
//        // build httpRequest to validate with google com.jameszhao.rsbotFlipper.api
//        HttpGet httpRequest = new HttpGet(OAUTH_GOOGLE_URL);
//        try {
//            // build URI
//            URIBuilder uriBuilder= new URIBuilder(httpRequest.getURI());
//            uriBuilder.addParameter("id_token", request.queryParams("tokenId")).build();
//            httpRequest.setURI(uriBuilder.build());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        // send httpRequest to google validation
//        try (CloseableHttpClient httpClient = HttpClients.createDefault();
//             CloseableHttpResponse httpResponse = httpClient.execute(httpRequest)) {
//            int statusCode = httpResponse.getStatusLine().getStatusCode();
//            // if valid response
//            if(statusCode == OK) {
//                // set response code
//                response.status(OK);
//                // set headers
//                response.header(CONTENT_TYPE_HEADER,CONTENT_TYPE_JSON);
//                String responseBody = EntityUtils.toString(httpResponse.getEntity());
//
//                // log response body
//                LOGGER.fine("\tGoogle OAuth response body: " + responseBody);
//
//                // read json
//                JsonNode jsonNode = objectMapper.readTree(responseBody);
//
//                // generate a jwt
//                String token = generateToken(jsonNode.get("email").toString(), JwtConfig.ISSUER, JwtConfig.SUBJECT, 36000);
//                response.body(objectMapper.writeValueAsString(new JwtResponse(token)));
//                return response.body();
//            } else {
//                // if google response is not valid
//                response.status(BAD_REQUEST);
//                response.header(CONTENT_TYPE_HEADER, CONTENT_TYPE_JSON);
//                return generateErrorJson(new ErrorResponse(INVALID_TOKEN_ERROR, INVALID_TOKEN_ERROR_DESCRIPTION));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return "null";
//    }
//
//    private static String generateErrorJson(ErrorResponse errorResponse) {
//        try {
//            return objectMapper.writeValueAsString(errorResponse);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return "error";
//        }
//    }
//
//    private static String generateToken(String id, String issuer, String subject, long ttlMillis) {
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//        long now = System.currentTimeMillis();
//        Date nowDate = new Date(now);
//
//        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JwtConfig.SECRET_KEY);
//        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
//
//        JwtBuilder builder = Jwts.builder().setId(id)
//                .setIssuedAt(nowDate)
//                .setSubject(subject)
//                .setIssuer(issuer)
//                .signWith(signatureAlgorithm, signingKey);
//
//        if (ttlMillis > 0) {
//            long expMillis = now + ttlMillis;
//            Date exp = new Date(expMillis);
//            builder.setExpiration(exp);
//        }
//        String JWT = builder.compact();
//        MainServer.LOGGER.fine("Generated JWT: " + JWT);
//        return JWT;
//    }
//
//    private static Claims decodeJWT(String jwt) {
//        Claims claims = Jwts.com.jameszhao.rsbotFlipper.parser()
//                .setSigningKey(DatatypeConverter.parseBase64Binary(JwtConfig.SECRET_KEY))
//                .parseClaimsJws(jwt).getBody();
//        return claims;
//    }
//
//    private static CheckJwtResponse checkJWT(String jwt) {
//        Claims decodedJwt = null;
//        int responseCode = BAD_REQUEST;
//        try {
//            decodedJwt = decodeJWT(jwt);
//        } catch(SignatureException | MalformedJwtException | IllegalArgumentException e) {
//           String errorJson = generateErrorJson(new ErrorResponse(INVALID_JWT_TOKEN_ERROR, INVALID_JWT_TOKEN_ERROR_DESCRIPTION));
//           return new CheckJwtResponse(responseCode, errorJson);
//        } catch(ExpiredJwtException e) {
//            MainServer.LOGGER.fine(String.format("\tJWT %s is expired at %d", decodedJwt.getId(), System.currentTimeMillis()));
//            String errorJson = generateErrorJson(new ErrorResponse(EXPIRED_JWT_TOKEN_ERROR, EXPIRED_JWT_TOKEN_ERROR_DESCRIPTION));
//            return new CheckJwtResponse(responseCode, errorJson);
//        }
//        responseCode = OK;
//        return new CheckJwtResponse(responseCode, decodedJwt);
//    }
}
