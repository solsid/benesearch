package fr.solsid.service.api.benebox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class BeneboxApi {

    @Value("${app.api.benebox.fqdn}")
    private String FQDN;

    private static final String AUTH_LOGIN_PARAM_KEY = "login";
    private static final String AUTH_PASSWORD_PARAM_KEY = "mot_de_passe";
    private static final String AUTH_PATH = "/offres/gestion/login/controle_login.php";

    public AuthenticationResponsePayload authenticate(AuthenticationRequestPayload requestPayload) throws BeneboxAuthenticationException{

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add(AUTH_LOGIN_PARAM_KEY, requestPayload.getLogin());
        map.add(AUTH_PASSWORD_PARAM_KEY, requestPayload.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        String url = FQDN + AUTH_PATH;

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request,
                String.class);

        if (response.getStatusCode().value() == 200) {
            String xmlBodyString = response.getBody();

            ObjectMapper xmlMapper = new XmlMapper();
            try {
                AuthenticationResponsePayload responsePayload = new AuthenticationResponsePayload();

                AuthenticationResponseBody responseBody = xmlMapper.readValue(xmlBodyString, AuthenticationResponseBody.class);
                responsePayload.setBody(responseBody);

                String setCookieHeader = response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
                responsePayload.setSetCookieHeader(setCookieHeader);

                return responsePayload;

            } catch (IOException e) {
                throw new BeneboxAuthenticationException("L'utilisateur n'a pas pu être authentifié.", e);
            }
        } else {
            throw new BeneboxAuthenticationException("L'utilisateur n'a pas pu être authentifié.");
        }
    }
}
