package fr.solsid.repository;

import fr.solsid.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Arnaud on 06/09/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User save(User user);

    User findByEmail(String email);

}