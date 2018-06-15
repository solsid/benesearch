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

    // URL exemple: "https://www.benebox.org/offres/image_inline_src/594/594_annuaire_2092676_L.jpg"
    private static final String PHOTOS_TEMPLATE_URL = "https://www.benebox.org/offres/image_inline_src/594/594_annuaire_%s_L.%s";

    private static final String JPG = "jpg";
    private static final String PNG = "png";

    public synchronized byte[] fetchPhoto(String volunteerIdString) {
        byte[] photoBytes = new byte[0];
        try {
            photoBytes = fetchPhoto(volunteerIdString, JPG);
        } catch (final HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                photoBytes = fetchPhoto(volunteerIdString, PNG);
            }
        }
        System.out.println("Fetched photo: " + volunteerIdString + ".");

        return photoBytes;
    }

    private synchronized byte[] fetchPhoto(final String volunteerIdString, final String photoFormat) {
        String url = String.format(PHOTOS_TEMPLATE_URL, volunteerIdString, photoFormat);
        System.out.println("Fetching photo: " + volunteerIdString + " to URL: " + url + "...");
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, byte[].class);
    }

    public synchronized void pingPhoto(String volunteerIdString) {
        try {
            pingPhoto(volunteerIdString, JPG);
        } catch (final HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                pingPhoto(volunteerIdString, PNG);
            }
        }
        System.out.println("Pinged photo: " + volunteerIdString + ".");
    }

    private synchronized void pingPhoto(final String volunteerIdString, final String photoFormat) {
        String url = String.format(PHOTOS_TEMPLATE_URL, volunteerIdString, photoFormat);
        System.out.println("Pinging photo: " + volunteerIdString + " to URL: " + url + "...");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.headForHeaders(url);
    }

    public void fetchPhotosInThread(Iterable<Volunteer> volunteersToFetch, PhotosZip photosZip, ExecutorService executor) {

        Runnable worker = new Runnable() {
            @Override
            public void run() {

                for (Volunteer volunteerToFetch : volunteersToFetch) {
                    String volunteerId = volunteerToFetch.getId();

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
                    String volunteerId = volunteerToFetch.getId();

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
