package fr.solsid.service;

import fr.solsid.model.AccessRight;
import fr.solsid.model.Assignment;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

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
            throws IOException {

        Map<Assignment, Set<AccessRight>> result = new HashMap<>();

        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();

        // Skip header row.
        if (iterator.hasNext()) {
            iterator.next();
        }

        while (iterator.hasNext()) {

            Row currentRow = iterator.next();

            Assignment assignment = extractAssigment(currentRow);
            Set<AccessRight> accessRights = extractAccessRights(currentRow);

            result.put(assignment, accessRights);
        }

        return result;
    }

    private Assignment extractAssigment(Row row) {
        String team = getStringValueOf(row, 0);
        String leader = getStringValueOf(row, 1);
        boolean isLeader = "chef".equalsIgnoreCase(leader) ? true : false;
        return new Assignment(team, isLeader);
    }

    private Set<AccessRight> extractAccessRights(Row row) {
        Set<AccessRight> accessRights = new HashSet<>();

        for (int i=2 ; i <= 7 ; i++) {
            int accessRight = getNumericValueOf(row, i);
            if (hasRight(accessRight)) {
                accessRights.add(columnToAccessRight.get(i));
            }
        }

        return accessRights;
    }

    private boolean hasRight(int value) {
        return 1 == value;
    }

    private String getStringValueOf(Row row, int cellIndex) {
        return row.getCell(cellIndex).getStringCellValue();
    }

    private Integer getNumericValueOf(Row row, int cellIndex) {
        try {
            return (int) row.getCell(cellIndex).getNumericCellValue();
        } catch(IllegalStateException e) {
            System.out.println("Couldn't get value from row: " + row.getRowNum() + " / cell: " + cellIndex);
            return null;
        }
    }
}
