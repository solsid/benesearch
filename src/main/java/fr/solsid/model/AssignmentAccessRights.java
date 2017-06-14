package fr.solsid.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Arnaud on 14/06/2017.
 */
public class AssignmentAccessRights {

    private final Assignment assignment;
    private final Set<AccessRight> accessRights;

    public AssignmentAccessRights(Assignment assignment, Collection<AccessRight> accessRights) {
        this.assignment = assignment;
        this.accessRights = new HashSet<>(accessRights);
    }

    public Assignment assignment() {
        return assignment;
    }

    public Set<AccessRight> accessRightsSet() {
        return new HashSet<>(this.accessRights);
    }

    public void addAccessRight(AccessRight accessRight) {
        this.accessRights.add(accessRight);
    }
}
