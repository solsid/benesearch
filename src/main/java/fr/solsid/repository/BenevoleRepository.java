package fr.solsid.repository;

import fr.solsid.entity.Benevole;

import java.util.List;

/**
 * Created by Arnaud on 29/08/2017.
 */
public interface BenevoleRepository /*extends CrudRepository<Benevole, Long>*/ {

    List<Benevole> findByNom(String lasnomtName);
}