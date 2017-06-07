package fr.solsid.model;

import org.springframework.web.client.RestTemplate;

/**
 * Created by Arnaud on 07/06/2017.
 */
public class PhotoFetcher {

    // URL exemple: "http://www.benebox.org/offres/image_inline_src/594/594_annuaire_2092676_L.jpg"
    private static final String PHOTOS_TEMPLATE_URL = "http://www.benebox.org/offres/image_inline_src/594/594_annuaire_%s_L.jpg";

    public synchronized byte[] fetchPhoto(String volunteerIdString) {
        String url = String.format(PHOTOS_TEMPLATE_URL, volunteerIdString);
        System.out.println("Fetching photo: " + volunteerIdString + " to URL: " + url + "...");
        RestTemplate restTemplate = new RestTemplate();
        byte[] photoBytes = restTemplate.getForObject(url, byte[].class);
        System.out.println("Fetched photo: " + volunteerIdString + ".");
        return photoBytes;
    }

    public synchronized void pingPhoto(String volunteerIdString) {
        String url = String.format(PHOTOS_TEMPLATE_URL, volunteerIdString);
        System.out.println("Pinging photo: " + volunteerIdString + " to URL: " + url + "...");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.headForHeaders(url);
        System.out.println("Pinged photo: " + volunteerIdString + ".");
    }
}
