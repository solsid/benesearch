package fr.solsid.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fr.solsid.exception.AuthenticationException;
import fr.solsid.model.BeneboxLoginResponse;
import fr.solsid.model.User;
import fr.solsid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Arnaud on 06/09/2017.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        counter++;
        user.setUserId(counter);
        USERS.add(user);
        return user; //TODO:
//        return userRepository.save(user);
    }

    private static final List<User> USERS = new ArrayList<>();    //TODO: Remove this
    static { //TODO: Remove this
        User user = new User();
        user.setUserId(0L);
        user.setEmail("arnaud.oisel@gmail.com");
        user.setPassword("test");
        user.setCreated(new Date());
        user.setFirstName("Arnaud");
        user.setLastName("Oisel");

        USERS.add(user);
    }
    private static long counter = 0;

    public User findByEmail(String email) {
        for (User user : USERS) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
        //TODO:
//        return userRepository.findByEmail(email);
    }

    public String authenticate(User user) throws AuthenticationException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("login", user.getEmail());
        map.add("mot_de_passe", user.getPassword());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://www.benebox.org/offres/gestion/login/controle_login.php",
                request,
                String.class);
        if (response.getStatusCode().value() == 200) {
            String xmlBodyString = response.getBody();

            ObjectMapper xmlMapper = new XmlMapper();
            try {
                BeneboxLoginResponse xmlBody = xmlMapper.readValue(xmlBodyString, BeneboxLoginResponse.class);
                if ("OK".equalsIgnoreCase(xmlBody.getResult().getValue())) {
                    return response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0);
                } else {
                    throw new AuthenticationException("L'utilisateur n'a pas pu être authentifié.");
                }
            } catch (IOException e) {
                throw new AuthenticationException("L'utilisateur n'a pas pu être authentifié.", e);
            }
        } else {
            throw new AuthenticationException("L'utilisateur n'a pas pu être authentifié.");
        }
    }
}
