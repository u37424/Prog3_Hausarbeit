package de.medieninformatik.client.interfaces;

public interface IViewController<T> extends IController {
    void setOptions();
    void setValues(T book);
    void returnToMain();
    void submitChanges();
    void deleteItem();
}
