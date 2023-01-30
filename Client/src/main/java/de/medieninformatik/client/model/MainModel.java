package de.medieninformatik.client.model;

public class MainModel {
    private final Request request;
    private final BookRequest bookRequest;
    private final CategoryRequest categoryRequest;
    private final AuthorRequest authorRequest;
    private final PublisherRequest publisherRequest;

    private boolean mainUser;
    private boolean editMode;
    private boolean createMode;

    public MainModel() {
        this.request = new Request();
        this.bookRequest = new BookRequest(request);
        this.categoryRequest = new CategoryRequest(request);
        this.authorRequest = new AuthorRequest(request);
        this.publisherRequest = new PublisherRequest(request);
    }

    public boolean login() {
        return mainUser = request.login();
    }

    public boolean logout() {
        return !(mainUser = !request.logout());
    }

    public void changeHostName(String name){
        request.changeHostName(name);
    }

    //---------RESET


    public BookRequest getBookRequest() {
        return bookRequest;
    }

    public CategoryRequest getCategoryRequest() {
        return categoryRequest;
    }

    public AuthorRequest getAuthorRequest() {
        return authorRequest;
    }

    public PublisherRequest getPublisherRequest() {
        return publisherRequest;
    }

    public boolean resetDatabase() {
        return request.resetDatabase();
    }

    //-------------User Settings im Model, um Anmeldung ueber mehrere Views / Controller zu behalten

    public boolean isMainUser() {
        return mainUser;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public boolean isCreateMode() {
        return createMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;   //Set Edit Status
    }

    public void setCreateMode(boolean createMode) {
        this.editMode = createMode;
        this.createMode = createMode; //Set Create Status
    }
}
