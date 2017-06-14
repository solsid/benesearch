package fr.solsid.model;

import java.util.Set;

/**
 * Created by Arnaud on 14/06/2017.
 */
public class VolunteerWithAccessRights {

    private final Volunteer volunteer;
    private final Set<AccessRight> accessRights;

    public VolunteerWithAccessRights(Volunteer volunteer, Set<AccessRight> accessRights) {
        this.volunteer = volunteer;
        this.accessRights = accessRights;
    }
}
