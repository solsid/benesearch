package fr.solsid.model;

/**
 * Created by Arnaud on 07/06/2017.
 */
public class VolunteerTeamFilter implements VolunteerFilter {

    private String team;

    public VolunteerTeamFilter(String team) {
        if (team == null)
            throw new IllegalArgumentException();
        this.team = team;
    }

    @Override
    public boolean keep(Volunteer volunteer) {
        return volunteer != null && this.team.equals(volunteer.getAssignment().getTeam());
    }
}
