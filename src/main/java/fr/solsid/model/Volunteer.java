package fr.solsid.model;

/**
 * Created by Arnaud on 31/05/2017.
 */
public class Volunteer implements Comparable<Volunteer>{

    private final String id;
    private final String lastName;
    private final String firstName;
    private final String email;
    private final Assignment assignment;

    public Volunteer(String id, String lastName, String firstName, String email, String team, boolean leader) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.assignment = new Assignment(team, leader);
    }

    public Volunteer(Volunteer volunteer) {
        this(volunteer.getId(), volunteer.getLastName(), volunteer.getFirstName(), volunteer.getEmail(), volunteer.getAssignment().getTeam(), volunteer.assignment.isLeader());
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Volunteer volunteer = (Volunteer) o;

        if (id != null ? !id.equals(volunteer.id) : volunteer.id != null) return false;
        if (lastName != null ? !lastName.equals(volunteer.lastName) : volunteer.lastName != null) return false;
        if (firstName != null ? !firstName.equals(volunteer.firstName) : volunteer.firstName != null) return false;
        if (email != null ? !email.equals(volunteer.email) : volunteer.email != null) return false;
        return !(assignment != null ? !assignment.equals(volunteer.assignment) : volunteer.assignment != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (assignment != null ? assignment.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Volunteer volunteer) {
        if (volunteer != null) {
            if (volunteer.getLastName() != null) {
                if (this.lastName != null) {
                    this.lastName.compareTo(volunteer.lastName);
                } else {
                    return -1;
                }
            }
        }
        return 1;
    }
}
