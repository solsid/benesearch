package fr.solsid.service;

import fr.solsid.service.api.benebox.BeneboxApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SynchronizationService {

    @Autowired
    private BeneboxApi beneboxApi;

    /**
     * Start the synchronization of the database (i.e. update data of local database by fetching data from Benebox)
     */
    public void start() {

        //TODO:
//        curl "https://www.benebox.org/offres/gestion/annuaire/module_export.php"
//                -H "Cookie: PHPSESSID=361c31f20fe10e3c4ffd8b9ffc20fa28"
//                -H "Origin: https://www.benebox.org"
//                -H "Accept-Encoding: gzip, deflate"
//                -H "Upgrade-Insecure-Requests: 1"
//                -H "Content-Type: application/x-www-form-urlencoded"
//                -H "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"
//                --data "choix_de_filtrage=liste^&select_liste=12079^&favsearch=^&format_file=csv^&formposted=yoman^&type=CSV^&choix_list=12079^&choix_search=^&choix_filtre=1^&reponse^%^5B^%^5D=2^&reponse^%^5B^%^5D=3^&reponse^%^5B^%^5D=14^&reponse^%^5B^%^5D=89^&reponse^%^5B^%^5D=221" --compressed

    }
}
