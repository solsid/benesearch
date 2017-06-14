package fr.solsid.controller;

import fr.solsid.model.Teams;
import fr.solsid.service.VolunteersCsvFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Arnaud on 11/06/2017.
 */
@RestController
public class TeamController {

    private final VolunteersCsvFileReader volunteersCsvFileReader;

    @Autowired
    public TeamController(VolunteersCsvFileReader volunteersCsvFileReader) {
        this.volunteersCsvFileReader = volunteersCsvFileReader;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllTeams", method= RequestMethod.POST)
    public ResponseEntity<Teams> exportTeamPhoto(
            @RequestParam("file") MultipartFile file) throws Exception {

        if (!file.isEmpty()) {

                List<String> teamsList = volunteersCsvFileReader.readTeams(file.getInputStream());

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");

                Teams teams = new Teams();
                teams.setTeams(teamsList.toArray(new String[teamsList.size()]));

                return new ResponseEntity<>(teams, headers,
                        HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("Vous devez fournir un fichier à lire en entrée.");
        }
    }
}
