package fr.solsid.model;

/**
 * Created by Arnaud on 07/06/2017.
 */
public interface VolunteerFilter {

    boolean keep(Volunteer volunteer);
}
