package fr.solsid.controller;

import fr.solsid.model.*;
import fr.solsid.service.AccessRightsFileReader;
import fr.solsid.service.VolunteersCsvFileReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Arnaud on 14/06/2017.
 */
@RestController
public class BadgeController {

    private static final List<BadgeDatabaseInputColumn> badgeDatabaseInputColumns = new ArrayList<>();
    static {
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.ID_PARTICIPANT);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.CATEGORIE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PHOTO);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PHOTO_FILENAME);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.NOM);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PRENOM);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.SOCIETE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.TELEPHONE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.MOBILE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.EMAIL);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.CHEF_EQUIPE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.NOM_CHEF_EQUIPE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.ID_CHEF_EQUIPE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.EQUIPE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.RESPONSABLE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.COMMENTAIRES);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.MONTAGE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PROD);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.CLUB_VIP);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.MEDIA_PRESSE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.LOGES_ARTISTES);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.SCENES);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.DEVANT_DE_SCENE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PARK_BOULOGNE);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PARK_ARTISTES);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PARK_CIRCULATION);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PARK_LIVRAISON_MATIN);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PARK_TOUT_PUBLIC);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PARK_SECURITE_EXTERIEUR);
        badgeDatabaseInputColumns.add(BadgeDatabaseInputColumn.PARK_TECHNIQUE);
    }

    private final VolunteersCsvFileReader volunteersCsvFileReader;
    private final AccessRightsFileReader accessRightsFileReader;

    @Autowired
    public BadgeController(
            VolunteersCsvFileReader volunteersCsvFileReader,
            AccessRightsFileReader accessRightsFileReader
    ) {
        this.volunteersCsvFileReader = volunteersCsvFileReader;
        this.accessRightsFileReader = accessRightsFileReader;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getVolunteersWithAccessRights", method= RequestMethod.POST)
    public ResponseEntity<List<VolunteerWithAccessRights>> getVolunteersWithAssignment(
            @RequestParam("volunteers") MultipartFile volunteersFile,
            @RequestParam("teamLeadersAccessRights") MultipartFile teamLeadersAccessRightsFile,
            @RequestParam("nonTeamLeadersAccessRights") MultipartFile nonTeamLeadersAccessRightsFile
            ) throws Exception {

        if (!volunteersFile.isEmpty() && (!teamLeadersAccessRightsFile.isEmpty() && !nonTeamLeadersAccessRightsFile.isEmpty())) {

            List<Volunteer> volunteers = readVolunteersFile(volunteersFile.getInputStream());
            Map<Assignment, Set<AccessRight>> assigmentAccessRights = new HashMap<>();
            if (!teamLeadersAccessRightsFile.isEmpty()) {
                assigmentAccessRights.putAll(readAccessRightsFile(teamLeadersAccessRightsFile.getInputStream()));
            }
            if (!nonTeamLeadersAccessRightsFile.isEmpty()) {
                assigmentAccessRights.putAll(readAccessRightsFile(nonTeamLeadersAccessRightsFile.getInputStream()));
            }
            List<VolunteerWithAccessRights> result = associateAccessRightsToVolunteers(volunteers, assigmentAccessRights);

            System.out.println("Returning " + result.size() + " volunteers with access rights.");

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            return new ResponseEntity<>(result, headers,
                    HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("Vous devez fournir des fichiers à lire en entrée.");
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/badgeDatabase/inputFile/generate", method= RequestMethod.POST)
    public ResponseEntity<Resource> generateBadgeDatabaseInputFile(
            @RequestParam("volunteers") MultipartFile volunteersFile,
            @RequestParam("teamLeadersAccessRights") MultipartFile teamLeadersAccessRightsFile,
            @RequestParam("nonTeamLeadersAccessRights") MultipartFile nonTeamLeadersAccessRightsFile
    ) throws Exception {

        if (!volunteersFile.isEmpty() && (!teamLeadersAccessRightsFile.isEmpty() && !nonTeamLeadersAccessRightsFile.isEmpty())) {

            List<Volunteer> volunteers = readVolunteersFile(volunteersFile.getInputStream());
            Map<Assignment, Set<AccessRight>> assigmentAccessRights = new HashMap<>();
            if (!teamLeadersAccessRightsFile.isEmpty()) {
                assigmentAccessRights.putAll(readAccessRightsFile(teamLeadersAccessRightsFile.getInputStream()));
            }
            if (!nonTeamLeadersAccessRightsFile.isEmpty()) {
                assigmentAccessRights.putAll(readAccessRightsFile(nonTeamLeadersAccessRightsFile.getInputStream()));
            }

            List<VolunteerWithAccessRights> result = associateAccessRightsToVolunteers(volunteers, assigmentAccessRights);

            System.out.println("Returning " + result.size() + " volunteers with access rights.");

            ByteArrayResource resource = new ByteArrayResource(createFile(result));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/octet-stream");
            headers.add("content-disposition", "attachment; filename=badge_database_input.zip");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } else {
            throw new IllegalArgumentException("Vous devez fournir des fichiers à lire en entrée.");
        }
    }

    private List<Volunteer> readVolunteersFile(InputStream fileInputStream) throws IOException {
        return volunteersCsvFileReader.read(fileInputStream);
    }

    private Map<Assignment, Set<AccessRight>> readAccessRightsFile(InputStream fileInputStream) throws IOException, InvalidFormatException {
        return accessRightsFileReader.read(fileInputStream);
    }

    private List<VolunteerWithAccessRights> associateAccessRightsToVolunteers(List<Volunteer> volunteers, Map<Assignment, Set<AccessRight>> assignmentAccessRights) {
        List<VolunteerWithAccessRights> result = new ArrayList<>();
        for (Volunteer volunteer : volunteers) {
            Set<AccessRight> accessRights = assignmentAccessRights.get(volunteer.getAssignment());
            result.add(new VolunteerWithAccessRights(volunteer, accessRights));
        }
        return result;
    }

    private byte[] createFile(List<VolunteerWithAccessRights> volunteers) throws IOException {
        BadgeDatabaseInput excelFile = createExcel(volunteers);
        return zip(excelFile.getWorkbook(), createVolunteersWithoutAccessRightsFile(excelFile.getVolunteersWithoutAccessRights()));
    }

    private BadgeDatabaseInput createExcel(List<VolunteerWithAccessRights> volunteers) {
        List<VolunteerWithAccessRights> volunteersWithoutAccessRights = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Badge database input");

        int rowNum = 0;
        System.out.println("Creating excel");

        // Set Header
        Row row = sheet.createRow(rowNum++);
        int colNum = 0;
        for (BadgeDatabaseInputColumn column : badgeDatabaseInputColumns) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(column.getKey());
        }

        // Set values
        for (VolunteerWithAccessRights volunteer : volunteers) {
            if (volunteer.getAccessRights() != null) {
                row = sheet.createRow(rowNum++);
                colNum = 0;

                for (BadgeDatabaseInputColumn column : badgeDatabaseInputColumns) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(column.getValue(volunteer));
                }
            } else {
                volunteersWithoutAccessRights.add(volunteer);
            }
        }

        System.out.println("Done");
        return new BadgeDatabaseInput(workbook, volunteersWithoutAccessRights);

    }

    private byte[] createVolunteersWithoutAccessRightsFile(List<VolunteerWithAccessRights> volunteers) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        bos.write(("Id;Nom;Prénom;Email;Equipe\r\n").getBytes());

        for (VolunteerWithAccessRights volunteer : volunteers) {
            Volunteer vol = volunteer.getVolunteer();
            bos.write((vol.getId() + ";" + vol.getLastName() + ";" + vol.getFirstName() + ";" + vol.getEmail() + ";" + vol.getAssignment().getTeam() + "\r\n").getBytes());
        }

        return bos.toByteArray();
    }

    private byte[] zip(XSSFWorkbook badgeDatabaseInputExcel, byte[] volunteersWithoutAccessRights) throws IOException {

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipOutputStream zipOut = new ZipOutputStream(bos);

        ByteArrayOutputStream excelBos = new ByteArrayOutputStream();
        try {
            badgeDatabaseInputExcel.write(excelBos);
        } finally {
            excelBos.close();
        }
        byte[] excelBytes = excelBos.toByteArray();

        ZipEntry excelZipEntry = new ZipEntry("badge_database_input.xlsx");
        zipOut.putNextEntry(excelZipEntry);
        zipOut.write(excelBytes, 0, excelBytes.length);

        ZipEntry otherVolunteersZipEntry = new ZipEntry("volunteers_without_access_rights.txt");
        zipOut.putNextEntry(otherVolunteersZipEntry);
        zipOut.write(volunteersWithoutAccessRights, 0, volunteersWithoutAccessRights.length);

        zipOut.close();
        bos.close();

        return bos.toByteArray();
    }
}
