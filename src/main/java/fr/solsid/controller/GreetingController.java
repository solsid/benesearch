package fr.solsid.controller;

import com.opencsv.CSVReader;
import fr.solsid.model.Greeting;
import fr.solsid.model.PhotosZip;
import fr.solsid.model.Volunteer;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
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
    private SecureRandom random = new SecureRandom();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    // URL exemple: "http://www.benebox.org/offres/image_inline_src/594/594_annuaire_2092676_L.jpg"
    private static final String PHOTOS_TEMPLATE_URL = "http://www.benebox.org/offres/image_inline_src/594/594_annuaire_%s_L.jpg";

/*    @CrossOrigin(origins = "*")
    @RequestMapping("/photo/export/all")
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
    }*/

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/exportWithoutPhoto", method= RequestMethod.POST)
    public ResponseEntity<Resource> exportVolunteersWithoutPhoto(
            @RequestParam("file") MultipartFile file) throws Exception {
        if (!file.isEmpty()) {

            try {
                // Read CSV
                CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), "ISO-8859-1"), ';');
                String[] header = reader.readNext();

                String [] nextLine;

                PhotosZip photosZip = new PhotosZip();

                // Read CSV and fetch Photos et Add to ZIP
                while ((nextLine = reader.readNext()) != null) {
                    String id = nextLine[0];
                    String lastname = nextLine[1];
                    String firstname = nextLine[2];
                    String email = nextLine[3];
                    String team = nextLine[4];

                    try {
                        pingPhoto(id);

                    } catch (final HttpClientErrorException e) {
                        if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                            photosZip.addVolunteerWithoutPhoto(new Volunteer(id, lastname, firstname, email, team));
                        }
                    }

                }

                ByteArrayResource resource = new ByteArrayResource(photosZip.toByteArray());

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/octet-stream");
                headers.add("content-disposition", "attachment; filename=compressed_photo_export.zip");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(resource);

            } catch (Exception e) {
//                return "You failed to upload " + name + " => " + e.getMessage();
//                        this.interrupt();
                throw new Exception("epic fail", e);
            }
//                }
//            };

//            thread.start();

//            return requestId;
        } else {
//            return "You failed to upload " + name + " because the file was empty.";
            throw new Exception("epic fail");
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/exportTeamPhoto", method= RequestMethod.POST)
    public ResponseEntity<Resource> exportTeamPhoto(
            @RequestParam("file") MultipartFile file, @RequestParam("team") String teamToExport) throws Exception {
        //final String requestId = new BigInteger(130, random).toString(32);

        if (!file.isEmpty()) {

 //           Thread thread = new Thread(){
 //               public void run(){
                    try {
                        // Read CSV
                        CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), "ISO-8859-1"), ';');
                        String[] header = reader.readNext();

                        String [] nextLine;

                        PhotosZip photosZip = new PhotosZip(/*getExportFileFullPath(requestId)*/);

                        // Read CSV and fetch Photos et Add to ZIP
                        while ((nextLine = reader.readNext()) != null) {
                            String id = nextLine[0];
                            String lastname = nextLine[1];
                            String firstname = nextLine[2];
                            String email = nextLine[3];
                            String team = nextLine[4];

                            if (teamToExport.equals(team)) {
                                try {
                                    byte[] photoBytes = fetchPhoto(id);
                                    photosZip.addPhoto(id, photoBytes);

                                } catch (final HttpClientErrorException e) {
                                    System.out.println("No photo found for: " + id);
                                    if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                                        photosZip.addVolunteerWithoutPhoto(new Volunteer(id, lastname, firstname, email, team));
                                    }
                                }
                            }
                        }

                        ByteArrayResource resource = new ByteArrayResource(photosZip.toByteArray());

                        HttpHeaders headers = new HttpHeaders();
                        headers.add("Content-Type", "application/octet-stream");
                        headers.add("content-disposition", "attachment; filename=compressed_photo_" + teamToExport + "export.zip");

                       return ResponseEntity.ok()
                                .headers(headers)
                                .contentLength(resource.contentLength())
                                .contentType(MediaType.parseMediaType("application/octet-stream"))
                                .body(resource);

                    } catch (Exception e) {
//                return "You failed to upload " + name + " => " + e.getMessage();
//                        this.interrupt();
                        throw new Exception("epic fail", e);
                    }
//                }
//            };

//            thread.start();

//            return requestId;
        } else {
//            return "You failed to upload " + name + " because the file was empty.";
            throw new Exception("epic fail");
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/download", method= RequestMethod.GET)
    public ResponseEntity<Resource> fetchPhotoExport(
            @RequestParam("id") String requestId) throws Exception {

        final File file = new File(getExportFileFullPath(requestId));
        if (file.exists()) {
            final FileInputStream fis = new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fis);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/octet-stream");
            headers.add("content-disposition", "attachment; filename=compressed_photo_export.zip");

            ResponseEntity<Resource> response =  ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);

            file.delete();

            return response;
        }

        return null;
    }

/*    private byte[] fetchPhotosAndZip() throws IOException {

        PhotosZip photosZip = new PhotosZip();

        for (int i=2092000 ; i < 2093000 ; i++) {
            String volunteerIdString = String.valueOf(i);
            try {
                byte[] photoBytes = fetchPhoto(volunteerIdString);
                photosZip.addPhoto(volunteerIdString, photoBytes);

            } catch (final HttpClientErrorException e) {
                if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                    photosZip.addVolunteerWithoutPhoto(new Volunteer(volunteerIdString, null, null, null, null));
                }
            }
        }

        return photosZip.toByteArray();
    }*/

    private String getExportFileFullPath(String requestId) {
        return "/tmp/" + requestId;
    }

    private byte[] fetchPhoto(String volunteerIdString) {
        String url = String.format(PHOTOS_TEMPLATE_URL, volunteerIdString);
        System.out.println("Fetching photo: " + volunteerIdString + " to URL: " + url + "...");
        RestTemplate restTemplate = new RestTemplate();
        byte[] photoBytes = restTemplate.getForObject(url, byte[].class);
        System.out.println("Fetched photo: " + volunteerIdString + ".");
        return photoBytes;
    }

    private void pingPhoto(String volunteerIdString) {
        String url = String.format(PHOTOS_TEMPLATE_URL, volunteerIdString);
        System.out.println("Pinging photo: " + volunteerIdString + " to URL: " + url + "...");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.headForHeaders(url);
        System.out.println("Pinged photo: " + volunteerIdString + ".");
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

 /*   public static void main(String[] args) throws IOException {
        String filePath = "/Users/Arnaud/Downloads/BN SD17 avec date affectation 31 mai.csv";
        File file = new File(filePath);
        // Read CSV
        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"), ';');
        String[] header = reader.readNext();

        String [] nextLine;

        Set<String> teams = new HashSet<>();
        // Read CSV and fetch Photos et Add to ZIP
        while ((nextLine = reader.readNext()) != null) {
            String id = nextLine[0];
            String lastname = nextLine[1];
            String firstname = nextLine[2];
            String email = nextLine[3];
            String team = nextLine[4];

            teams.add(team);
        }
        List<String> teamsList = new ArrayList<String>(teams);
        Collections.sort(teamsList);
        for (String team : teamsList) {
            System.out.println("\"" + team + "\",");
        }
    }*/
}