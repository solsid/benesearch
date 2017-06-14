package fr.solsid.service;

import com.opencsv.CSVReader;
import fr.solsid.model.Pools;
import fr.solsid.model.Volunteer;
import fr.solsid.model.VolunteerFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Arnaud on 07/06/2017.
 */
@Service
public class VolunteersCsvFileReader {

    public Pools<Volunteer> read(InputStream inputStream, int maxPoolSize) throws IOException {

        return read(inputStream, maxPoolSize, null);
    }

    public Pools<Volunteer> read(InputStream inputStream, int maxPoolSize, VolunteerFilter... filters) throws IOException {

        // Read CSV
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream, "ISO-8859-1"), ';');
        String[] header = reader.readNext();

        String [] nextLine;

        Pools<Volunteer> volunteersPools = new Pools<>(maxPoolSize);

        // Read CSV and fetch Photos et Add to ZIP
        while ((nextLine = reader.readNext()) != null) {
            String id = nextLine[0];
            String lastname = nextLine[1];
            String firstname = nextLine[2];
            String email = nextLine[3];
            String team = nextLine[4];

            Volunteer volunteer = new Volunteer(id, lastname, firstname, email, team);
            if (filters != null) {
                for (VolunteerFilter filter : filters) {
                    if (filter.keep(volunteer)) {
                        volunteersPools.add(volunteer);
                        break;
                    }
                }
            } else {
                volunteersPools.add(volunteer);
            }
        }

        return volunteersPools;
    }

    public List<String> readTeams(InputStream inputStream) throws IOException {
        Set<String> teamsSet = new HashSet<>();

        // Read CSV
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream, "ISO-8859-1"), ';');
        String[] header = reader.readNext();

        String [] nextLine;

        // Read CSV and fetch Photos et Add to ZIP
        while ((nextLine = reader.readNext()) != null) {

            String team = nextLine[4];
            teamsSet.add(team);
        }

        List<String> teams = new ArrayList<>(teamsSet);
        Collections.sort(teams);

        return teams;
    }
}
