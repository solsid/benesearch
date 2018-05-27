package fr.solsid.controller;

import fr.solsid.model.Greeting;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Arnaud on 10/09/2016.
 */
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    /*
    private BenevoleRepository benevoleRepository;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GreetingController(BenevoleRepository benevoleRepository, JdbcTemplate jdbcTemplate) {
        this.benevoleRepository = benevoleRepository;
        this.jdbcTemplate = jdbcTemplate;
    }
*/
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

//    @RequestMapping("/greeting/testDatabase")
//    public Benevole testDatabase() {
//        return benevoleRepository.findOne(1L);
//    }
//
//    @RequestMapping("/greeting/testDatabase2")
//    public Benevole testDatabase2() {
//        List<Benevole> benevoles = jdbcTemplate.query("SELECT id, nom, prenom, edition_annee FROM benevole",
//                (rs, rowNum) -> new Benevole(rs.getString("prenom"), rs.getString("nom"), rs.getInt("edition_annee"))
//        );
//        System.out.println(benevoles);
//        return benevoles.get(0);
//    }

    @RequestMapping(value="/greeting/authent", method= RequestMethod.POST)
    public ResponseEntity<String> authent(
            @RequestParam(value="login") String login,
            @RequestParam(value="mot_de_passe") String motDePasse) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("login", login);
        map.add("mot_de_passe", motDePasse);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://www.benebox.org/offres/gestion/login/controle_login.php",
                request ,
                String.class );

        return response;
    }

    @RequestMapping(value="/greeting/testAvecAuthent", method= RequestMethod.GET)
    public ResponseEntity<String> testAvecAuthent(@RequestHeader(value="Cookie") String cookie) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookie);

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://www.benebox.org/594_p_38242/ma-boite-a-outils.html",
                HttpMethod.GET, entity, String.class);

        String body = response.getBody();
        if (body.contains("Merci de bien vouloir vous identifier")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("KO");
        } else {
            return ResponseEntity.ok().body("OK");
        }
    }
}