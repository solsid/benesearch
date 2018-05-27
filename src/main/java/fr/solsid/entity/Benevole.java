package fr.solsid.entity;

/**
 * Created by Arnaud on 29/08/2017.
 */
//@Entity
public class Benevole {

//    @Id
    private Long id;
    private String prenom;
    private String nom;
    private int edition_annee;

    protected Benevole() {}

    public Benevole(String prenom, String nom, int edition_annee) {
        this.prenom = prenom;
        this.nom = nom;
        this.edition_annee = edition_annee;
    }

    public Long getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public int getEdition_annee() {
        return edition_annee;
    }
}