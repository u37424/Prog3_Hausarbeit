package de.medieninformatik.client.interfaces;

public interface IMainController extends IController{
    void loadItemList();
    void createItem();
    void bookPressed();
    void categoryPressed();
    void authorPressed();
    void publisherPressed();
    void setOrder();
    void updatePageSize(int size);
    void pageBackward();
    void pageForward();
    void updateFilter();
    void resetFilter();
    void returnToLogin();
    void inspectItem(String id);
    void editItem(String id);
    void deleteItem(String id);
    void resetDatabase();
}
