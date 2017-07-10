package fr.solsid.model;

/**
 * Created by Arnaud on 14/06/2017.
 */
public class Assignment {

    private final String team;
    private final Boolean leader;

    public Assignment(String team, Boolean leader) {
        this.team = team;
        this.leader = leader;
    }

    public Assignment(Assignment assignment) {
        this.team = assignment.getTeam();
        this.leader = assignment.isLeader();
    }

    public String getTeam() {
        return this.team;
    }

    public Boolean isLeader() {
        return this.leader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assignment that = (Assignment) o;

        if (team != null ? !team.equals(that.team) : that.team != null) return false;
        return !(leader != null ? !leader.equals(that.leader) : that.leader != null);

    }

    @Override
    public int hashCode() {
        int result = team != null ? team.hashCode() : 0;
        result = 31 * result + (leader != null ? leader.hashCode() : 0);
        return result;
    }
}
