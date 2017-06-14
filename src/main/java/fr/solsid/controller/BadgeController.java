package fr.solsid.controller;

import fr.solsid.model.AccessRight;
import fr.solsid.model.Assignment;
import fr.solsid.model.Volunteer;
import fr.solsid.model.VolunteerWithAccessRights;
import fr.solsid.service.AccessRightsFileReader;
import fr.solsid.service.VolunteersCsvFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Arnaud on 14/06/2017.
 */
@RestController
public class BadgeController {

    private final VolunteersCsvFileReader volunteersCsvFileReader;
    private final AccessRightsFileReader accessRightsFileReader;

    @Autowired
    public BadgeController(
            VolunteersCsvFileReader volunteersCsvFileReader,
            AccessRightsFileReader accessRightsFileReader
    ) {
        this.volunteersCsvFileReader = volunteersCsvFileReader;
        this.accessRightsFileReader = accessRightsFileReader;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getVolunteersWithAccessRights", method= RequestMethod.POST)
    public ResponseEntity<List<VolunteerWithAccessRights>> getVolunteersWithAssignment(
            @RequestParam("volunteersFile") MultipartFile volunteersFile,
            @RequestParam("accessRightsMatrixFile") MultipartFile accessRightsMatrixFile
            ) throws Exception {

        if (!volunteersFile.isEmpty() && !accessRightsMatrixFile.isEmpty()) {

            List<Volunteer> volunteers = readVolunteersFile(volunteersFile.getInputStream());
            Map<Assignment, Set<AccessRight>> assigmentAccessRights = readAccessRightsMatrixFile(accessRightsMatrixFile.getInputStream());

            List<VolunteerWithAccessRights> result = associateAccessRightsToVolunteers(volunteers, assigmentAccessRights);

            System.out.println("Returning " + result.size() + " volunteers with access rights.");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            return new ResponseEntity<>(result, headers,
                    HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("Vous devez fournir des fichiers à lire en entrée.");
        }
    }

    private List<Volunteer> readVolunteersFile(InputStream fileInputStream) throws IOException {
        return volunteersCsvFileReader.read(fileInputStream);
    }

    private Map<Assignment, Set<AccessRight>> readAccessRightsMatrixFile(InputStream fileInputStream) throws IOException {
        return accessRightsFileReader.read(fileInputStream);
    }

    private List<VolunteerWithAccessRights> associateAccessRightsToVolunteers(List<Volunteer> volunteers, Map<Assignment, Set<AccessRight>> assignmentAccessRights) {
        List<VolunteerWithAccessRights> result = new ArrayList<>();
        for (Volunteer volunteer : volunteers) {
            Set<AccessRight> accessRights = assignmentAccessRights.get(volunteer.assignment());
            result.add(new VolunteerWithAccessRights(volunteer, accessRights));
        }
        return result;
    }
}
