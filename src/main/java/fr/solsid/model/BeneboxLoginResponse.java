package fr.solsid.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "root")
public class BeneboxLoginResponse {

    @JacksonXmlProperty(localName = "state")
    private BeneboxLoginReponseElement state;

    @JacksonXmlProperty(localName = "result")
    private BeneboxLoginReponseElement result;

    @JacksonXmlProperty(localName = "firstlog")
    private BeneboxLoginReponseElement firstLog;

    @JacksonXmlProperty(localName = "firstid")
    private BeneboxLoginReponseElement firstId;

    @JacksonXmlProperty(localName = "pagetoload")
    private BeneboxLoginReponseElement pageToLoad;

    @JacksonXmlProperty(localName = "menuforced")
    private BeneboxLoginReponseElement menuForced;

    @JacksonXmlProperty(localName = "mode")
    private BeneboxLoginReponseElement mode;

    @JacksonXmlProperty(localName = "listeids")
    private BeneboxLoginReponseElement listeIds;

    @JacksonXmlProperty(localName = "urlko")
    private BeneboxLoginReponseElement urlKo;

    @JacksonXmlProperty(localName = "type_ko")
    private BeneboxLoginReponseElement type;

    public BeneboxLoginReponseElement getState() {
        return state;
    }

    public void setState(BeneboxLoginReponseElement state) {
        this.state = state;
    }

    public BeneboxLoginReponseElement getResult() {
        return result;
    }

    public void setResult(BeneboxLoginReponseElement result) {
        this.result = result;
    }

    public BeneboxLoginReponseElement getFirstLog() {
        return firstLog;
    }

    public void setFirstLog(BeneboxLoginReponseElement firstLog) {
        this.firstLog = firstLog;
    }

    public BeneboxLoginReponseElement getFirstId() {
        return firstId;
    }

    public void setFirstId(BeneboxLoginReponseElement firstId) {
        this.firstId = firstId;
    }

    public BeneboxLoginReponseElement getPageToLoad() {
        return pageToLoad;
    }

    public void setPageToLoad(BeneboxLoginReponseElement pageToLoad) {
        this.pageToLoad = pageToLoad;
    }

    public BeneboxLoginReponseElement getMenuForced() {
        return menuForced;
    }

    public void setMenuForced(BeneboxLoginReponseElement menuForced) {
        this.menuForced = menuForced;
    }

    public BeneboxLoginReponseElement getMode() {
        return mode;
    }

    public void setMode(BeneboxLoginReponseElement mode) {
        this.mode = mode;
    }

    public BeneboxLoginReponseElement getListeIds() {
        return listeIds;
    }

    public void setListeIds(BeneboxLoginReponseElement listeIds) {
        this.listeIds = listeIds;
    }

    public BeneboxLoginReponseElement getUrlKo() {
        return urlKo;
    }

    public void setUrlKo(BeneboxLoginReponseElement urlKo) {
        this.urlKo = urlKo;
    }

    public BeneboxLoginReponseElement getType() {
        return type;
    }

    public void setType(BeneboxLoginReponseElement type) {
        this.type = type;
    }
}
