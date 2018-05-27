package fr.solsid.service;

import fr.solsid.exception.AuthenticationException;
import fr.solsid.model.User;
import fr.solsid.repository.UserRepository;
import fr.solsid.service.api.benebox.AuthenticationRequestPayload;
import fr.solsid.service.api.benebox.AuthenticationResponsePayload;
import fr.solsid.service.api.benebox.BeneboxApi;
import fr.solsid.service.api.benebox.BeneboxAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Arnaud on 06/09/2017.
 */
@Service
public class UserService {

//    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BeneboxApi beneboxApi;

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

        AuthenticationRequestPayload requestPayload = new AuthenticationRequestPayload();
        requestPayload.setLogin(user.getEmail());
        requestPayload.setPassword(user.getPassword());

        try {
            AuthenticationResponsePayload responsePayload = beneboxApi.authenticate(requestPayload);
            if ("OK".equalsIgnoreCase(responsePayload.getBody().getResult().getValue())) {
                // On renvoie l'ID de session
                return responsePayload.getSetCookieHeader();
            } else {
                throw new AuthenticationException("L'utilisateur n'a pas pu être authentifié.");
            }
        } catch (BeneboxAuthenticationException e) {
            throw new AuthenticationException("L'utilisateur n'a pas pu être authentifié.", e);
        }
    }
}
