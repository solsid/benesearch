package fr.solsid.model;

/**
 * Created by Arnaud on 14/06/2017.
 */
public class Assignment {

    private final String team;
    private final boolean leader;

    public Assignment(String team, boolean leader) {
        this.team = team;
        this.leader = leader;
    }

    public Assignment(Assignment assignment) {
        this.team = assignment.team();
        this.leader = assignment.leader();
    }

    public String team() {
        return this.team;
    }

    public boolean leader() {
        return this.leader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assignment that = (Assignment) o;

        if (leader != that.leader) return false;
        return !(team != null ? !team.equals(that.team) : that.team != null);

    }

    @Override
    public int hashCode() {
        int result = team != null ? team.hashCode() : 0;
        result = 31 * result + (leader ? 1 : 0);
        return result;
    }
}
