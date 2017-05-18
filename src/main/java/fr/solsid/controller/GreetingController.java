package fr.solsid.controller;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
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
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @CrossOrigin(origins = "*")
    @RequestMapping("/photos/export/all")
    public ResponseEntity<Resource> exportAllPhotos() throws IOException {

        byte[] imageBytes = fetchImage();
        FileOutputStream fos = zip(imageBytes);
        ByteArrayResource resource = new ByteArrayResource(fos.toString().getBytes());

        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private byte[] fetchImage() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://www.benebox.org/offres/image_inline_src/594/594_annuaire_2092676_L.jpg", byte[].class);

    }

    private FileOutputStream zip(byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream("photo_export_compressed.zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        ZipEntry zipEntry = new ZipEntry("photo_export");
        zipOut.putNextEntry(zipEntry);
        zipOut.write(bytes, 0, bytes.length);
        zipOut.close();
        fos.close();
        return fos;
    }
}