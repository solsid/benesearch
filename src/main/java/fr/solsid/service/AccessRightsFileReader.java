package fr.solsid.service;

import fr.solsid.model.AccessRight;
import fr.solsid.model.Assignment;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Arnaud on 14/06/2017.
 */
@Service
public class AccessRightsFileReader {

    private static final Map<Integer, AccessRight> columnToAccessRight = new HashMap<>();

    static {
        columnToAccessRight.put(2, AccessRight.PROD);
        columnToAccessRight.put(3, AccessRight.CLUB_VIP);
        columnToAccessRight.put(4, AccessRight.MEDIA_PRESSE);
        columnToAccessRight.put(5, AccessRight.LOGES_ARTISTES);
        columnToAccessRight.put(6, AccessRight.SCENES);
        columnToAccessRight.put(7, AccessRight.DEVANT_SCENE);
    }

    public Map<Assignment, Set<AccessRight>> read(
            InputStream inputStream)
            throws IOException, InvalidFormatException {

        Map<Assignment, Set<AccessRight>> result = new HashMap<>();

        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();

        // Skip header row.
        if (iterator.hasNext()) {
            iterator.next();
        }

        while (iterator.hasNext()) {

            Row currentRow = iterator.next();

            String team = getStringValueOf(currentRow, 0);
            if (StringUtils.isEmpty(team)) {
                break;
            }
            try {
                List<Assignment> assignments = extractAssigments(currentRow);
                Set<AccessRight> accessRights = extractAccessRights(currentRow);
                for (Assignment assignment : assignments) {
                    result.put(assignment, accessRights);
                }
            } catch(Exception e) {
                System.out.println("Problem extracting info from row: " + currentRow.getRowNum());
                throw e;
            }
        }

        System.out.println("Found " + result.size() + " assignements.");

        return result;
    }

    private List<Assignment> extractAssigments(Row row) {
        List<Assignment> assignments = new ArrayList<>();
        String team = getStringValueOf(row, 0);
        String leader = getStringValueOf(row, 1);

        if (StringUtils.isEmpty(leader)) {
            assignments.add(new Assignment(team, false));
            assignments.add(new Assignment(team, true));
        } else {
            boolean isLeader = "chef".equalsIgnoreCase(leader) ? true : false;
            assignments.add(new Assignment(team, isLeader));
        }
        return assignments;

    }

    private Set<AccessRight> extractAccessRights(Row row) {
        Set<AccessRight> accessRights = new HashSet<>();

        for (int i=2 ; i <= 7 ; i++) {
            String accessRightStr = getStringValueOf(row, i);
            if (!StringUtils.isEmpty(accessRightStr) && hasRight(accessRightStr)) {
                accessRights.add(columnToAccessRight.get(i));
            } else {
                int accessRightInt = getNumericValueOf(row, i);
                if (hasRight(accessRightInt)) {
                    accessRights.add(columnToAccessRight.get(i));
                }
            }
        }

        return accessRights;
    }

    private boolean hasRight(String value) {
        return "OUI".equalsIgnoreCase(value);
    }

    private boolean hasRight(int value) {
        return 1 == value;
    }

    private String getStringValueOf(Row row, int cellIndex) {
        try {
            return row.getCell(cellIndex).getStringCellValue();
        } catch(Exception e) {
            System.out.println("Couldn't get value from row: " + row.getRowNum() + " / cell: " + cellIndex);
            return "";
        }
    }

    private int getNumericValueOf(Row row, int cellIndex) {
        try {
            return (int) row.getCell(cellIndex).getNumericCellValue();
        } catch(Exception e) {
            System.out.println("Couldn't get value from row: " + row.getRowNum() + " / cell: " + cellIndex);
            return 0;
        }
    }
}
