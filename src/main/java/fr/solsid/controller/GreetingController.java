package fr.solsid.controller;

import fr.solsid.model.Greeting;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Arnaud on 10/09/2016.
 */
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    // URL exemple: "http://www.benebox.org/offres/image_inline_src/594/594_annuaire_2092676_L.jpg"
    private static final String PHOTOS_TEMPLATE_URL = "http://www.benebox.org/offres/image_inline_src/594/594_annuaire_%s_L.jpg";

    @CrossOrigin(origins = "*")
    @RequestMapping("/photos/export/all")
    public ResponseEntity<Resource> exportAllPhotos() throws IOException {

        System.out.println("Started to export all photos.");

        byte[] zippedPhotos = fetchPhotosAndZip();
        ByteArrayResource resource = new ByteArrayResource(zippedPhotos);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("content-disposition", "attachment; filename=compressed_photo_export.zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private byte[] fetchPhotosAndZip() throws IOException {

        List<String> volunteersWithoutPhotos = new ArrayList<String>();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);

        for (int i=2092000 ; i < 2093000 ; i++) {
            String volunteerIdString = String.valueOf(i);
            try {
                byte[] photoBytes = fetchPhoto(volunteerIdString);

                ZipEntry zipEntry = new ZipEntry(volunteerIdString);
                zipOut.putNextEntry(zipEntry);
                zipOut.write(photoBytes, 0, photoBytes.length);

            } catch (final HttpClientErrorException e) {
                System.out.println("Error while fetching photo: " + volunteerIdString + ". Status code: " + e.getStatusCode());
                if ("404".equals(e.getStatusCode())) {
                    volunteersWithoutPhotos.add(volunteerIdString);
                }
            }
        }

        addVoluntersWithoutPhotosToZip(volunteersWithoutPhotos, zipOut);

        zipOut.close();
        bos.close();
        return bos.toByteArray();
    }

    private byte[] fetchPhoto(String volunteerIdString) {
        String url = String.format(PHOTOS_TEMPLATE_URL, volunteerIdString);
        System.out.println("Fetching photo: " + volunteerIdString + " to URL: " + url + "...");
        RestTemplate restTemplate = new RestTemplate();
        byte[] photoBytes = restTemplate.getForObject(url, byte[].class);
        System.out.println("Fetched photo: " + volunteerIdString + ".");
        return photoBytes;
    }

    private void addVoluntersWithoutPhotosToZip(List<String> volunteersWithoutPhotos, ZipOutputStream zipOut) throws IOException {

        byte[] byteArray = createFileWithVoluntersWithoutPhotos(volunteersWithoutPhotos);
        ZipEntry zipEntry = new ZipEntry("benevoles_sans_photos.txt");
        zipOut.putNextEntry(zipEntry);
        zipOut.write(byteArray, 0, byteArray.length);
    }

    private byte[] createFileWithVoluntersWithoutPhotos(List<String> volunteersWithoutPhotos) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (String volunteerId : volunteersWithoutPhotos) {
            bos.write((volunteerId + "\r\n").getBytes());
        }
        return bos.toByteArray();
    }

    /*
    private ByteArrayOutputStream zip(byte[] bytes) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);
        ZipEntry zipEntry = new ZipEntry("photo_export");
        zipOut.putNextEntry(zipEntry);
        zipOut.write(bytes, 0, bytes.length);
        zipOut.close();
        bos.close();
        return bos;
    }
    */
}