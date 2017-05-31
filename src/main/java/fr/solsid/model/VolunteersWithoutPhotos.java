package fr.solsid.model;

import java.util.*;

/**
 * Created by Arnaud on 31/05/2017.
 */
public class VolunteersWithoutPhotos {

    private Map<String, Set<Volunteer>> volunteersSetByTeam = new HashMap<>();

    public void add(Volunteer volunteer) {
        Set<Volunteer> volunteersInTeam;
        if (volunteersSetByTeam.containsKey(volunteer.team())) {
            volunteersInTeam = volunteersSetByTeam.get(volunteer.team());
        } else {
            volunteersInTeam = new HashSet<>();
            volunteersSetByTeam.put(volunteer.team(), volunteersInTeam);
        }
        volunteersInTeam.add(volunteer);
    }

    public Map<String, List<Volunteer>> getVolunteersByTeam() {
        Map<String, List<Volunteer>> volunteersListByTeam = new HashMap<>();
        for (String team : volunteersSetByTeam.keySet()) {
            Set<Volunteer> volunteers = volunteersSetByTeam.get(team);
            List<Volunteer> volunteersList = new ArrayList<>(volunteers);
            Collections.sort(volunteersList);
            volunteersListByTeam.put(team, volunteersList);
        }
        return volunteersListByTeam;
    }
}
