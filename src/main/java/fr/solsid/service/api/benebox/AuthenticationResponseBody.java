package fr.solsid.service.api.benebox;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "root")
public class AuthenticationResponseBody {

    @JacksonXmlProperty(localName = "state")
    private AuthenticationResponseBodyElement state;

    @JacksonXmlProperty(localName = "result")
    private AuthenticationResponseBodyElement result;

    @JacksonXmlProperty(localName = "firstlog")
    private AuthenticationResponseBodyElement firstLog;

    @JacksonXmlProperty(localName = "firstid")
    private AuthenticationResponseBodyElement firstId;

    @JacksonXmlProperty(localName = "pagetoload")
    private AuthenticationResponseBodyElement pageToLoad;

    @JacksonXmlProperty(localName = "menuforced")
    private AuthenticationResponseBodyElement menuForced;

    @JacksonXmlProperty(localName = "mode")
    private AuthenticationResponseBodyElement mode;

    @JacksonXmlProperty(localName = "listeids")
    private AuthenticationResponseBodyElement listeIds;

    @JacksonXmlProperty(localName = "urlko")
    private AuthenticationResponseBodyElement urlKo;

    @JacksonXmlProperty(localName = "type_ko")
    private AuthenticationResponseBodyElement type;

    public AuthenticationResponseBodyElement getState() {
        return state;
    }

    public void setState(AuthenticationResponseBodyElement state) {
        this.state = state;
    }

    public AuthenticationResponseBodyElement getResult() {
        return result;
    }

    public void setResult(AuthenticationResponseBodyElement result) {
        this.result = result;
    }

    public AuthenticationResponseBodyElement getFirstLog() {
        return firstLog;
    }

    public void setFirstLog(AuthenticationResponseBodyElement firstLog) {
        this.firstLog = firstLog;
    }

    public AuthenticationResponseBodyElement getFirstId() {
        return firstId;
    }

    public void setFirstId(AuthenticationResponseBodyElement firstId) {
        this.firstId = firstId;
    }

    public AuthenticationResponseBodyElement getPageToLoad() {
        return pageToLoad;
    }

    public void setPageToLoad(AuthenticationResponseBodyElement pageToLoad) {
        this.pageToLoad = pageToLoad;
    }

    public AuthenticationResponseBodyElement getMenuForced() {
        return menuForced;
    }

    public void setMenuForced(AuthenticationResponseBodyElement menuForced) {
        this.menuForced = menuForced;
    }

    public AuthenticationResponseBodyElement getMode() {
        return mode;
    }

    public void setMode(AuthenticationResponseBodyElement mode) {
        this.mode = mode;
    }

    public AuthenticationResponseBodyElement getListeIds() {
        return listeIds;
    }

    public void setListeIds(AuthenticationResponseBodyElement listeIds) {
        this.listeIds = listeIds;
    }

    public AuthenticationResponseBodyElement getUrlKo() {
        return urlKo;
    }

    public void setUrlKo(AuthenticationResponseBodyElement urlKo) {
        this.urlKo = urlKo;
    }

    public AuthenticationResponseBodyElement getType() {
        return type;
    }

    public void setType(AuthenticationResponseBodyElement type) {
        this.type = type;
    }
}
