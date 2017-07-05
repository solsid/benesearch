package fr.solsid.model;

/**
 * Created by Arnaud on 05/07/2017.
 */
public enum BadgeDatabaseInputColumn {

    ID_PARTICIPANT {
        @Override
        public String getKey() {
            return "id_participant";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getVolunteer().getId();
        }
    },
    CATEGORIE {
        @Override
        public String getKey() {
            return "categorie";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "Volontaires Bénévoles";
        }
    },
    PHOTO {
        @Override
        public String getKey() {
            return "photo";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "OUI"; //TODO:
        }
    },
    PHOTO_FILENAME {
        @Override
        public String getKey() {
            return "photo_filename";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getVolunteer().getId() + ".jpg";
        }
    },
    NOM {
        @Override
        public String getKey() {
            return "nom";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getVolunteer().getLastName();
        }
    },
    PRENOM {
        @Override
        public String getKey() {
            return "prenom";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getVolunteer().getFirstName();
        }
    },
    SOCIETE {
        @Override
        public String getKey() {
            return "societe";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    TELEPHONE {
        @Override
        public String getKey() {
            return "telephone";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    MOBILE {
        @Override
        public String getKey() {
            return "mobile";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    EMAIL {
        @Override
        public String getKey() {
            return "email";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    CHEF_EQUIPE {
        @Override
        public String getKey() {
            return "chef equipe";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getVolunteer().getAssignment().isLeader() ? "OUI" : "NON";
        }
    },
    NOM_CHEF_EQUIPE {
        @Override
        public String getKey() {
            return "nom chef equipe";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    ID_CHEF_EQUIPE {
        @Override
        public String getKey() {
            return "id_chef_equipe";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    EQUIPE {
        @Override
        public String getKey() {
            return "equipe";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getVolunteer().getAssignment().getTeam();
        }
    },
    RESPONSABLE {
        @Override
        public String getKey() {
            return "responsable";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    COMMENTAIRES {
        @Override
        public String getKey() {
            return "commentaires";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    MONTAGE {
        @Override
        public String getKey() {
            return "montage";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    PROD {
        @Override
        public String getKey() {
            return "Prod";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getAccessRights().contains(AccessRight.PROD) ? "OUI" : "NON";
        }
    },
    CLUB_VIP {
        @Override
        public String getKey() {
            return "Club VIP";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getAccessRights().contains(AccessRight.CLUB_VIP) ? "OUI" : "NON";
        }
    },
    MEDIA_PRESSE {
        @Override
        public String getKey() {
            return "Média Presse";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getAccessRights().contains(AccessRight.MEDIA_PRESSE) ? "OUI" : "NON";
        }
    },
    LOGES_ARTISTES {
        @Override
        public String getKey() {
            return "Loges Artistes";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getAccessRights().contains(AccessRight.LOGES_ARTISTES) ? "OUI" : "NON";
        }
    },
    SCENES {
        @Override
        public String getKey() {
            return "Scènes";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getAccessRights().contains(AccessRight.SCENES) ? "OUI" : "NON";
        }
    },
    DEVANT_DE_SCENE {
        @Override
        public String getKey() {
            return "Devant de Scène";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return volunteer.getAccessRights().contains(AccessRight.DEVANT_SCENE) ? "OUI" : "NON";
        }
    },
    PARK_BOULOGNE {
        @Override
        public String getKey() {
            return "Park boulogne";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    PARK_ARTISTES {
        @Override
        public String getKey() {
            return "Park artistes";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    PARK_CIRCULATION {
        @Override
        public String getKey() {
            return "Park circulation";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    PARK_LIVRAISON_MATIN {
        @Override
        public String getKey() {
            return "Park livraison matin";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    PARK_TOUT_PUBLIC {
        @Override
        public String getKey() {
            return "Park tout public";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    PARK_SECURITE_EXTERIEUR {
        @Override
        public String getKey() {
            return "Park sécurité extérieur";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    },
    PARK_TECHNIQUE {
        @Override
        public String getKey() {
            return "Park technique";
        }

        @Override
        public String getValue(VolunteerWithAccessRights volunteer) {
            return "";
        }
    };

    private BadgeDatabaseInputColumn() {
    }

    public abstract String getKey();
    public abstract String getValue(VolunteerWithAccessRights volunteer);
}
