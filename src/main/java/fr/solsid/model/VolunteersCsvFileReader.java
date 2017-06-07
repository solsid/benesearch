package fr.solsid.model;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

/**
 * Created by Arnaud on 07/06/2017.
 */
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
}
