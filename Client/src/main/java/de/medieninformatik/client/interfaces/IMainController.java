package de.medieninformatik.client.interfaces;

public interface IMainController extends IController{
    void loadBookList();
    void createBook();
    void createCategory();
    void createAuthor();
    void createPublisher();
    void setOrder();
    void updatePageSize(int size);
    void pageBackward();
    void pageForward();
    void updateFilter();
    void resetFilter();
    void returnToLogin();
    void inspectBook(String isbn);
    void editBook(String isbn);
    void deleteBook(String isbn);
}
