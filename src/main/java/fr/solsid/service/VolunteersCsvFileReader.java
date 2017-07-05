package fr.solsid.service;

import com.opencsv.CSVReader;
import fr.solsid.model.*;
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

    public Pools<Volunteer> read(
            InputStream inputStream,
            int maxPoolSize,
            Boolean teamLeaders,
            VolunteerFilter... filters
    ) throws IOException {

        Pools<Volunteer> volunteersPools = new Pools<>(maxPoolSize);

        readAndAddToCollection(inputStream, volunteersPools, teamLeaders, filters);

        return volunteersPools;
    }

    public List<Volunteer> read(
            InputStream inputStream,
            Boolean teamLeaders,
            VolunteerFilter... filters
    ) throws IOException {

        CustomList<Volunteer> volunteersCollection = new CustomList<>();

        readAndAddToCollection(inputStream, volunteersCollection, teamLeaders, filters);

        System.out.println("Found " + volunteersCollection.size() + " volunteers");
        return volunteersCollection;
    }

    private void readAndAddToCollection(
            InputStream inputStream,
            Incrementable<Volunteer> volunteersCollection,
            Boolean teamLeaders,
            VolunteerFilter... filters)
            throws IOException {

        // Read CSV
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream, "ISO-8859-1"), ';');
        String[] header = reader.readNext();

        String [] nextLine;

        // Read CSV and fetch Photos et Add to ZIP
        while ((nextLine = reader.readNext()) != null) {
            String id = nextLine[0];
            String lastname = nextLine[1];
            String firstname = nextLine[2];
            String email = nextLine[3];
            String team = nextLine[4];

            Volunteer volunteer = new Volunteer(id, lastname, firstname, email, team, teamLeaders);

            if (filters != null && filters.length > 0) {
                for (VolunteerFilter filter : filters) {
                    if (filter.keep(volunteer)) {
                        volunteersCollection.add(volunteer);
                        break;
                    }
                }
            } else {
                volunteersCollection.add(volunteer);
            }
        }
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
