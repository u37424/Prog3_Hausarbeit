package de.medieninformatik.client.interfaces;

import de.medieninformatik.common.Book;

public interface IBookController extends IController {
    void displayBook(Book book);
    void returnToMain();
    void editBookTitle();
    void editBookISBN();
    void editBookYear();
    void editBookPages();
    void editBookRating(String rating);
    void editBookAuthors();
    void editBookPublisher();
    void editBookCategories();
    void submitChanges();
    void deleteBook();
}