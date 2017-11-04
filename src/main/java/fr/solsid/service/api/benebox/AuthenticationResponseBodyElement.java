package fr.solsid.service.api.benebox;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class AuthenticationResponseBodyElement {

    @JacksonXmlProperty(isAttribute = true, localName = "val")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
