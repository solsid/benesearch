package fr.solsid.model;

/**
 * Created by Arnaud on 31/05/2017.
 */
public class Volunteer implements Comparable<Volunteer>{

    private final String id;
    private final String lastname;
    private final String firstname;
    private final String email;
    private final String team;

    public Volunteer(String id, String lastname, String firstName, String email, String team) {
        this.id = id;
        this.lastname = id;
        this.firstname = firstName;
        this.email = email;
        this.team = team;
    }

    public String id() {
        return id;
    }

    public String lastName() {
        return lastname;
    }

    public String frstName() {
        return firstname;
    }

    public String email() {
        return email;
    }

    public String team() {
        return team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Volunteer volunteer = (Volunteer) o;

        if (id != null ? !id.equals(volunteer.id) : volunteer.id != null) return false;
        if (lastname != null ? !lastname.equals(volunteer.lastname) : volunteer.lastname != null) return false;
        if (firstname != null ? !firstname.equals(volunteer.firstname) : volunteer.firstname != null) return false;
        if (email != null ? !email.equals(volunteer.email) : volunteer.email != null) return false;
        return !(team != null ? !team.equals(volunteer.team) : volunteer.team != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Volunteer volunteer) {
        if (volunteer != null) {
            if (volunteer.lastName() != null) {
                if (this.lastname != null) {
                    this.lastname.compareTo(volunteer.lastname);
                } else {
                    return -1;
                }
            }
        }
        return 1;
    }
}