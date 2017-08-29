package fr.solsid.controller;

import fr.solsid.entity.Benevole;
import fr.solsid.model.Greeting;
import fr.solsid.repository.BenevoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Arnaud on 10/09/2016.
 */
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private BenevoleRepository benevoleRepository;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GreetingController(BenevoleRepository benevoleRepository, JdbcTemplate jdbcTemplate) {
        this.benevoleRepository = benevoleRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping("/greeting/testDatabase")
    public Benevole testDatabase() {
        return benevoleRepository.findOne(1L);
    }

    @RequestMapping("/greeting/testDatabase2")
    public Benevole testDatabase2() {
        List<Benevole> benevoles = jdbcTemplate.query("SELECT id, nom, prenom, edition_annee FROM benevole",
                (rs, rowNum) -> new Benevole(rs.getString("prenom"), rs.getString("nom"), rs.getInt("edition_annee"))
        );
        System.out.println(benevoles);
        return benevoles.get(0);
    }
}