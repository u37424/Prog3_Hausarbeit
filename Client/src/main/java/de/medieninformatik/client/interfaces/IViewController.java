package de.medieninformatik.client.interfaces;

public interface IViewController extends IController {
    void setOptions();
    void displayValues();
    void returnToMain();
    boolean validateItem();
    void submitChanges();
    void deleteItem();
}
