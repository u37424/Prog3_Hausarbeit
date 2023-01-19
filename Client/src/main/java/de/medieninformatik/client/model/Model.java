package de.medieninformatik.client.model;

public class Model {
    //Alle POJOS / Daten die vom Server abgefragt werden landen hier und werden im View dargestellt
    //Wie lange muessen Daten gespeichert werden? Es sind nie alle Daten auf einmal vonnoeten
    //Aber Daten muessen nicht glecih verworfen werden
    //Buch abfragen -> Temporaer bis Fenster schliessen / anderes Buch aufrufen
    //Listen erhalten, auch wenn Buch abgefragt
    //Objekte die variable Listen enthalten
    //Ein Display-Buch
    //Wann erhaelt man Arbeits-Objekte (Auswahl von Kategorien und Autoren im Erstellen / Bearbeiten || Kategorieauswahl fuer Kategorie Filter <-wann laden?)
    //Was, wenn erfragte Objekte aus Listen bei spezieller Anfrage schon geloescht sind?
    //Was, wenn Objekte falsch erstellt werden?
    //Wie koennen Modifizierungen oder erstellte Buecher / Autoren / Kategorien / Publisher an den Server uebermittelt werden?

    //Hier Anfragen von Controller mithilfe von Request erhalten und bearbeiten
    private boolean isMainUser = false;
    Request request = Request.getInstance();

    public Model(){
        startup();
    }

    private void startup() {

    }

    public boolean login(){
        if(isMainUser) return false;
        else {
            return isMainUser = request.login();
        }
    }

    public boolean logout(){
        if(!isMainUser) return false;
        else {
            return isMainUser = request.logout();
        }
    }

    public boolean isMainUser() {
        return isMainUser;
    }
}
