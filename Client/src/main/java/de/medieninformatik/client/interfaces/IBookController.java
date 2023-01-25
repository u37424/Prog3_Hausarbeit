package de.medieninformatik.client.interfaces;

public interface IBookController extends IController {
    void returnToMain();
    void editBookInfo();
    void editBookAuthors();
    void editBookPublisher();
    void editBookCategories();
    void editBookDescription();
    void deleteBook();
}
