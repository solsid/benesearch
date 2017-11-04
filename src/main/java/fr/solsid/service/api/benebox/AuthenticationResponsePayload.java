package fr.solsid.service.api.benebox;

public class AuthenticationResponsePayload {

    private AuthenticationResponseBody body;
    private String setCookieHeader;

    public AuthenticationResponseBody getBody() {
        return body;
    }

    public void setBody(AuthenticationResponseBody body) {
        this.body = body;
    }

    public String getSetCookieHeader() {
        return setCookieHeader;
    }

    public void setSetCookieHeader(String setCookieHeader) {
        this.setCookieHeader = setCookieHeader;
    }
}
