package fr.solsid.model;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnaud on 05/07/2017.
 */
public class BadgeDatabaseInput {

    private XSSFWorkbook workbook;
    private List<VolunteerWithAccessRights> volunteersWithoutAccessRights = new ArrayList<>();

    public BadgeDatabaseInput(XSSFWorkbook workbook, List<VolunteerWithAccessRights> volunteers) {
        this.workbook = workbook;
        this.volunteersWithoutAccessRights = volunteers;
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public List<VolunteerWithAccessRights> getVolunteersWithoutAccessRights() {
        return new ArrayList<>(volunteersWithoutAccessRights);
    }

    public void setVolunteersWithoutAccessRights(List<VolunteerWithAccessRights> volunteersWithoutAccessRights) {
        this.volunteersWithoutAccessRights.addAll(volunteersWithoutAccessRights);
    }
}
