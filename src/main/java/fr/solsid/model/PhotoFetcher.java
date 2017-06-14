package fr.solsid.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

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

    public void fetchPhotosInThread(Iterable<Volunteer> volunteersToFetch, PhotosZip photosZip, ExecutorService executor) {

        Runnable worker = new Runnable() {
            @Override
            public void run() {

                for (Volunteer volunteerToFetch : volunteersToFetch) {
                    String volunteerId = volunteerToFetch.id();

                    try {
                        byte[] photoBytes = fetchPhoto(volunteerId);
                        photosZip.addPhoto(volunteerId, photoBytes);

                    } catch (final HttpClientErrorException e) {
                        System.out.println("No photo found for: " + volunteerId);
                        if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                            photosZip.addVolunteerWithoutPhoto(volunteerToFetch);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        executor.execute(worker);
    }

    public void pingPhotosInThread(Iterable<Volunteer> volunteersToFetch, PhotosZip photosZip, ExecutorService executor) {

        Runnable worker = new Runnable() {
            @Override
            public void run() {

                for (Volunteer volunteerToFetch : volunteersToFetch) {
                    String volunteerId = volunteerToFetch.id();

                    try {
                        pingPhoto(volunteerId);

                    } catch (final HttpClientErrorException e) {
                        System.out.println("No photo found for: " + volunteerId);
                        if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                            photosZip.addVolunteerWithoutPhoto(volunteerToFetch);
                        }
                    }
                }

            }
        };
        executor.execute(worker);
    }
}
