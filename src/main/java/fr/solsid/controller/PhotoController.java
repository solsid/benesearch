package fr.solsid.controller;

import com.opencsv.CSVReader;
import fr.solsid.model.*;
import fr.solsid.service.VolunteersCsvFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arnaud on 11/06/2017.
 */
@RestController
public class PhotoController {

    private final VolunteersCsvFileReader volunteersCsvFileReader;

    @Autowired
    public PhotoController(VolunteersCsvFileReader volunteersCsvFileReader) {
        this.volunteersCsvFileReader = volunteersCsvFileReader;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/exportAllPhotos", method= RequestMethod.POST)
    public ResponseEntity<Resource> exportAllPhotos(
            @RequestParam("file") MultipartFile file) throws Exception {

        if (!file.isEmpty()) {

            try {

                // Open thread executor
                ExecutorService executor = Executors.newFixedThreadPool(10);

                // Read CSV
                CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), "ISO-8859-1"), ';');
                String[] header = reader.readNext();

                String [] nextLine;

                PhotosZip photosZip = new PhotosZip();

                int lineCounter = 0;
                List<Volunteer> volunteersGrouped = new ArrayList<>();

                PhotoFetcher photoFetcher = new PhotoFetcher();

                // Read CSV and fetch Photos et Add to ZIP
                while ((nextLine = reader.readNext()) != null) {
                    String id = nextLine[0];
                    String lastname = nextLine[1];
                    String firstname = nextLine[2];
                    String email = nextLine[3];
                    String team = nextLine[4];

                    Volunteer volunteer = new Volunteer(id, lastname, firstname, email, team, false);
                    volunteersGrouped.add(volunteer);

                    if (lineCounter == 0 || lineCounter % 100 == 0) {
                        final List<Volunteer> volunteersToFetch = new ArrayList<>(volunteersGrouped);
                        volunteersGrouped.clear();

                        photoFetcher.fetchPhotosInThread(volunteersToFetch, photosZip, executor);
                    }

                    lineCounter++;
                }

                if (!volunteersGrouped.isEmpty()) {
                    final List<Volunteer> volunteersToFetch = new ArrayList<>(volunteersGrouped);
                    volunteersGrouped.clear();

                    photoFetcher.fetchPhotosInThread(volunteersToFetch, photosZip, executor);
                }

                // Shutdown threads
                executor.shutdown();
                try {
                    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    throw new Exception("epic fail", e);
                }
                System.out.println("Finished all threads");

                ByteArrayResource resource = new ByteArrayResource(photosZip.toByteArray());

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/octet-stream");
                headers.add("content-disposition", "attachment; filename=compressed_all_photo_export.zip");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(resource.contentLength())
                        .contentType(MediaType.parseMediaType("application/octet-stream"))
                        .body(resource);

            } catch (Exception e) {
                throw new Exception("epic fail", e);
            }
        } else {
            throw new Exception("epic fail");
        }
    }



    @CrossOrigin(origins = "*")
    @RequestMapping(value="/exportPhotosByHundred", method= RequestMethod.POST)
    public ResponseEntity<Resource> exportPhotosByHundred(
            @RequestParam("file") MultipartFile file,
            @RequestParam("part") Integer part)
            throws Exception {

        if (!file.isEmpty()) {

            try {

                // Open thread executor
                ExecutorService executor = Executors.newFixedThreadPool(5);

                Pools<Volunteer> volunteersPools = volunteersCsvFileReader.read(file.getInputStream(), 100, null);

                PhotosZip photosZip = new PhotosZip();

                PhotoFetcher photoFetcher = new PhotoFetcher();

                if (volunteersPools.size() >= part) {
                    photoFetcher.fetchPhotosInThread(volunteersPools.getPool(part - 1), photosZip, executor);

                    // Shutdown threads
                    executor.shutdown();
                    try {
                        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e) {
                        throw new Exception("epic fail", e);
                    }
                    System.out.println("Finished all threads");

                    ByteArrayResource resource = new ByteArrayResource(photosZip.toByteArray());

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Type", "application/octet-stream");
                    headers.add("content-disposition", "attachment; filename=compressed_all_photo_export.zip");

                    return ResponseEntity.ok()
                            .headers(headers)
                            .contentLength(resource.contentLength())
                            .contentType(MediaType.parseMediaType("application/octet-stream"))
                            .body(resource);
                } else {
                    return ResponseEntity.badRequest().body(null);
                }

            } catch (Exception e) {
                throw new Exception("epic fail", e);
            }
        } else {
            throw new Exception("epic fail");
        }
    }

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
                PhotoFetcher photoFetcher = new PhotoFetcher();

                // Read CSV and fetch Photos et Add to ZIP
                while ((nextLine = reader.readNext()) != null) {
                    String id = nextLine[0];
                    String lastname = nextLine[1];
                    String firstname = nextLine[2];
                    String email = nextLine[3];
                    String team = nextLine[4];

                    try {
                        photoFetcher.pingPhoto(id);

                    } catch (final HttpClientErrorException e) {
                        if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                            photosZip.addVolunteerWithoutPhoto(new Volunteer(id, lastname, firstname, email, team, false));
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
                throw new Exception("epic fail", e);
            }
        } else {
            throw new Exception("epic fail");
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/exportWithoutPhotoForTeam", method= RequestMethod.POST)
    public ResponseEntity<Map<String, List<Volunteer>>> exportVolunteersWithoutPhotoForTeam(
            @RequestParam("file") MultipartFile file, @RequestParam("team") String teamToExport) throws Exception {
        if (!file.isEmpty()) {

            try {
                // Open thread executor
                ExecutorService executor = Executors.newFixedThreadPool(10);

                Pools<Volunteer> volunteersPools = volunteersCsvFileReader.read(file.getInputStream(), 20, null, new VolunteerTeamFilter(teamToExport));

                PhotosZip photosZip = new PhotosZip();

                PhotoFetcher photoFetcher = new PhotoFetcher();

                for (Pool<Volunteer> volunteerPool : volunteersPools) {
                    photoFetcher.pingPhotosInThread(volunteerPool, photosZip, executor);
                }

                // Shutdown threads
                executor.shutdown();
                try {
                    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    throw new Exception("epic fail", e);
                }
                System.out.println("Finished all threads");

                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");

                return new ResponseEntity<>(photosZip.getVolunteersWithoutPhotoByTeam(), headers,
                        HttpStatus.OK);

            } catch (Exception e) {
                throw new Exception("epic fail", e);
            }
        } else {
            throw new Exception("epic fail");
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/exportTeamPhoto", method= RequestMethod.POST)
    public ResponseEntity<Resource> exportTeamPhoto(
            @RequestParam("file") MultipartFile file, @RequestParam("team") String teamToExport) throws Exception {
        //final String requestId = new BigInteger(130, random).toString(32);

        if (!file.isEmpty()) {

            try {

                // Open thread executor
                ExecutorService executor = Executors.newFixedThreadPool(5);

                Pools<Volunteer> volunteersPools = volunteersCsvFileReader.read(file.getInputStream(), 20, null, new VolunteerTeamFilter(teamToExport));

                PhotosZip photosZip = new PhotosZip();

                PhotoFetcher photoFetcher = new PhotoFetcher();

                for (Pool<Volunteer> volunteerPool : volunteersPools) {
                    photoFetcher.fetchPhotosInThread(volunteerPool, photosZip, executor);
                }

                // Shutdown threads
                executor.shutdown();
                try {
                    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    throw new Exception("epic fail", e);
                }
                System.out.println("Finished all threads");

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
                throw new Exception("epic fail", e);
            }
        } else {
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

    private String getExportFileFullPath(String requestId) {
        return "/tmp/" + requestId;
    }


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
