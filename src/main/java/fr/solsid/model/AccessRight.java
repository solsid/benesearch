package fr.solsid.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arnaud on 14/06/2017.
 */
public enum AccessRight {

    PROD("P"),
    CLUB_VIP("C"),
    MEDIA_PRESSE("M"),
    LOGES_ARTISTES("L"),
    SCENES("S"),
    DEVANT_SCENE("D");

    private static Map<String, AccessRight> letterToAccessRight = new HashMap<>();

    static {
        letterToAccessRight = new HashMap<>();
        for (AccessRight accessRight : AccessRight.values()) {
            letterToAccessRight.put(accessRight.getLetter(), accessRight);
        }
    }

    private final String letter;

    AccessRight(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        return this.letter;
    }

    public static AccessRight fromLetter(String letter) {
        return letterToAccessRight.get(letter);
    }
}
