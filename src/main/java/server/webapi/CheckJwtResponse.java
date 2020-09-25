package server.webapi;

import io.jsonwebtoken.Claims;

public class CheckJwtResponse {
    private int responseCode;
    private Claims claims;
    private String errorResponse;

    public CheckJwtResponse(int responseCode, Claims claims) {
        this.responseCode = responseCode;
        this.claims = claims;
    }

    public CheckJwtResponse(int responseCode, String errorResponse) {
        this.responseCode = responseCode;
        this.errorResponse = errorResponse;
    }

    public int getResponseCode() {
        return responseCode;
    }


    public Claims getClaims() {
        return claims;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

}
