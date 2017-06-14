package fr.solsid.model;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Arnaud on 31/05/2017.
 */
public class PhotosZip {

//    private final File file;
//    private final FileOutputStream fos;
    private final ByteArrayOutputStream bos;
    private final ZipOutputStream zipOut;
    private final VolunteersWithoutPhotos volunteersWithoutPhotos;

    public PhotosZip() throws FileNotFoundException {
 //       this.file = new File(fileFullPath + "_tmp");
 //       this.fos = new FileOutputStream(file);
        this.bos = new ByteArrayOutputStream();
        this.zipOut = new ZipOutputStream(bos);
        this.volunteersWithoutPhotos = new VolunteersWithoutPhotos();
    }

    public synchronized void addPhoto(String volunteerId, byte[] photoBytes) throws IOException {
        ZipEntry zipEntry = new ZipEntry(volunteerId + ".jpg");
        zipOut.putNextEntry(zipEntry);
        zipOut.write(photoBytes, 0, photoBytes.length);
    }

    public synchronized void addVolunteerWithoutPhoto(Volunteer volunteer) {
        volunteersWithoutPhotos.add(volunteer);
    }

    public synchronized byte[] toByteArray() throws IOException {

        addVolunteersWithoutPhotosToZip();

        close();

        return bos.toByteArray();
    }

    public synchronized Map<String, List<Volunteer>> getVolunteersWithoutPhotoByTeam() {
        return volunteersWithoutPhotos.getVolunteersByTeam();
    }

    private void addVolunteersWithoutPhotosToZip() throws IOException {
        final Map<String, List<Volunteer>> volunteersByTeam = volunteersWithoutPhotos.getVolunteersByTeam();

        for (String team : volunteersByTeam.keySet()) {
            System.out.println("Team: " + team);
            byte[] byteArray = createFileWithVolunteersWithoutPhotos(volunteersByTeam.get(team));
            ZipEntry zipEntry = new ZipEntry("benevoles_" + team + "_sans_photos.txt");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(byteArray, 0, byteArray.length);
        }
    }

    private byte[] createFileWithVolunteersWithoutPhotos(List<Volunteer> volunteers) throws IOException {
        System.out.println("Volunteers without photos: " + volunteers.size());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(("Id;Nom;Prénom;Email;Equipe\r\n").getBytes());

        for (Volunteer volunteer : volunteers) {
            bos.write((volunteer.getId() + ";" + volunteer.getLastName() + ";" + volunteer.getFirstName() + ";" + volunteer.getEmail() + ";" + volunteer.getAssignment().getTeam() + "\r\n").getBytes());
        }

        return bos.toByteArray();
    }

    private void close() throws IOException {
       // addVolunteersWithoutPhotosToZip();

        zipOut.close();
        bos.close();
        //file.delete();
    }


}
