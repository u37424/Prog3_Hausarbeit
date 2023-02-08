package de.medieninformatik.client.model;

import jakarta.ws.rs.core.Response;

import java.util.ResourceBundle;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse implementiert alle relevanten Anfragen und Antwortverarbeitungen fuer Benutzereinstellungen.
 * </p>
 */
public class UserModel {
    private final Request request;

    //Path Attributes
    private final String userPath;
    private final String loginPath, logoutPath, resetPath;

    public UserModel(Request request) {
        this.request = request;
        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        //Set Path Attributes
        this.userPath = bundle.getString("User.Path");
        this.loginPath = bundle.getString("User.Login.Path");
        this.logoutPath = bundle.getString("User.Logout.Path");
        this.resetPath = bundle.getString("User.Reset.Path");
    }

    /**
     * Aendert den Namen des Hosts in der Verbindungsadresse.
     *
     * @param name neuer Hostname
     */
    public void changeHostName(String name) {
        request.changeHostName(name);
    }

    /**
     * Stellt eine Login-Anfrage an den REST Server.
     *
     * @return true, wenn Login beim Server erfolgreich
     */
    public boolean login() {
        Response response = request.serverRequest("GET", "/" + userPath + "/" + loginPath);
        return request.isOk(response);
    }

    /**
     * Stellt eine Logout-Anfrage an den REST Server.
     *
     * @return true, wenn Logout beim Server erfolgreich
     */
    public boolean logout() {
        Response response = request.serverRequest("GET", "/" + userPath + "/" + logoutPath);
        return request.isOk(response);
    }

    /**
     * Stellt eine Database-Reset-Anfrage an den REST Server.
     *
     * @return true, wenn Database-Reset beim Server erfolgreich
     */
    public boolean resetDatabase() {
        Response response = request.serverRequest("POST", "/" + userPath + "/" + resetPath);
        return request.isOk(response);
    }
}
