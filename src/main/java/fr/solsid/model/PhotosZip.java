package fr.solsid.model;

import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Arnaud on 31/05/2017.
 */
public class PhotosZip {

    private final ByteArrayOutputStream bos;
    private final ZipOutputStream zipOut;
    private final VolunteersWithoutPhotos volunteersWithoutPhotos;

    public PhotosZip() {
        this.bos = new ByteArrayOutputStream();
        this.zipOut = new ZipOutputStream(bos);
        this.volunteersWithoutPhotos = new VolunteersWithoutPhotos();
    }

    public void addPhoto(String volunteerId, byte[] photoBytes) throws IOException {
        ZipEntry zipEntry = new ZipEntry(volunteerId + ".jpg");
        zipOut.putNextEntry(zipEntry);
        zipOut.write(photoBytes, 0, photoBytes.length);
    }

    public void addVolunteerWithoutPhoto(Volunteer volunteer) {
        volunteersWithoutPhotos.add(volunteer);
    }

    public byte[] toByteArray() throws IOException {

        addVolunteersWithoutPhotosToZip();

        close();

        return bos.toByteArray();
    }

    private void addVolunteersWithoutPhotosToZip() throws IOException {
        final Map<String, List<Volunteer>> volunteersByTeam = volunteersWithoutPhotos.getVolunteersByTeam();

        for (String team : volunteersByTeam.keySet()) {
            byte[] byteArray = createFileWithVolunteersWithoutPhotos(volunteersByTeam.get(team));
            ZipEntry zipEntry = new ZipEntry("benevoles_" + team + "_sans_photos.txt");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(byteArray, 0, byteArray.length);
        }
    }

    private byte[] createFileWithVolunteersWithoutPhotos(List<Volunteer> volunteers) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(("Id;Nom;Prénom;Email;Equipe\r\n").getBytes());

        for (Volunteer volunteer : volunteers) {
            bos.write((volunteer.id() + ";" + volunteer.lastName() + ";" + volunteer.frstName() + ";" + volunteer.email() + ";" + volunteer.team() + "\r\n").getBytes());
        }

        return bos.toByteArray();
    }

    private void close() throws IOException {
        zipOut.close();
        bos.close();
    }


}
