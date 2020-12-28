package controller;

import javafx.application.Platform;
import model.Author;
import model.Book;
import model.DatabaseMethods;
import model.SearchMode;
import view.AuthorDialog;
import view.BookDialog;
import view.BooksView;
import view.SearchDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.*;


/**
 * The controller is responsible for handling user requests and update the view
 *
 * @author Micke och Sofia
 */
public class Controller {

    private final BooksView booksView;
    private final DatabaseMethods booksDb;
    private List<Book> result;

    /**
     * Constructor
     *
     * @param booksDb an object of the model-class
     * @param booksView an object of the view-class
     */
    public Controller(DatabaseMethods booksDb, BooksView booksView) {
        this.booksDb = booksDb;
        this.booksView = booksView;
    }

    /**
     * Main search-method which handles search requests from a user to get
     * data from the database and then updates the UI.
     *
     * @param searchFor the input string to be searched for in the database
     * @param mode what search method the user has chosen
     */
    public void onSearchSelected(String searchFor, SearchMode mode) {
        if (searchFor != null && searchFor.length() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        switch (mode) {
                            case Title:
                                result = booksDb.searchByTitle(searchFor);
                                break;
                            case ISBN:
                                result = booksDb.searchByISBN(searchFor);
                                break;
                            case Author:
                                result = booksDb.searchByAuthor(searchFor);
                                break;
                            default:
                        }
                    }catch (Exception e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                booksView.showAlertAndWait("Database error.", ERROR);
                            }
                        });
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (result == null || result.isEmpty()) {
                                booksView.showAlertAndWait(
                                        "No results found.", INFORMATION);
                                booksView.displayBooks(new ArrayList<Book>());
                            } else {
                                booksView.displayBooks(result);
                            }
                        }
                    });
                }
            }).start();

        } else {
            booksView.showAlertAndWait(
                    "Enter a search string!", WARNING);
        }
    }

    /**
     * Closes the program by first closing the connection to the database.
     */
    public void onExitSelected() {
        if (this.onDisconnectSelected()) {
            System.exit(1);
        }
    }

    /**
     * Connects to the database
     * @return true if connected/false if connects is unsuccessful
     */
    public boolean onConnectSelected() {
        try {
            if(booksDb.connect("bookdb")){
                booksView.showAlertAndWait("Connected to database", CONFIRMATION);
                return true;
            }
            else{booksView.showAlertAndWait("No Connection", WARNING);}
        } catch (Exception e) {
            booksView.showAlertAndWait("No Connection", WARNING);
        }
        return false;
    }

    /**
     * Disconnects the program from the database.
     *
     * @return true if disconnected/false if unsuccessful
     */
    public boolean onDisconnectSelected() {
        try {
            booksDb.disconnect();
            booksView.showAlertAndWait("Disconnected", CONFIRMATION);
            return true;
        } catch (Exception e) {
            booksView.showAlertAndWait("Could not disconnect", ERROR);
        }
        return false;
    }

    /**
     * If the user has chosen to search on a title via the
     * header menu in UI this method will show a search dialog, take input from user and send info
     * to the main search method above to handle the request.
     */
    public void onSearchTitleSelected() {
        SearchDialog dialog = new SearchDialog("title");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            String s  = result.get();
            onSearchSelected(s, SearchMode.Title);
        }
    }
    /**
     * If the user has chosen to search on an ISBN via the
     * header menu in UI this method will show a search dialog, take input from user and send info
     * to the main search method above to handle the request.
     */
    public void onSearchIsbnSelected() {
        SearchDialog dialog = new SearchDialog("isbn");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            String s  = result.get();
            onSearchSelected(s, SearchMode.ISBN);
        }
    }
    /**
     * If the user has chosen to search on an author via the
     * header menu in UI this method will show a search dialog, take input from user and send info
     * to the main search method above to handle the request.
     */
    public void onSearchAuthorSelected() {
        SearchDialog dialog = new SearchDialog("author");
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            String s  = result.get();
            onSearchSelected(s,SearchMode.Author);
        }
    }

    /**
     * This method handles request to add a book.
     */
    public void onAddBookSelected() {
        BookDialog dialog = new BookDialog();
        Optional<Book> result = dialog.showAndWait();
        if (result.isPresent()) {
            Book book = result.get();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(booksDb.addBook(book)){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    booksView.showAlertAndWait("Book added", CONFIRMATION);
                                }
                            });
                        }else{
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    booksView.showAlertAndWait("Could not add book", CONFIRMATION);
                                }
                            });
                        }
                    } catch(Exception e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                booksView.showAlertAndWait("Could not add book ", ERROR);
                            }
                        });
                    }
                }
            }).start();
        }
    }

    /**
     * This method handles request to add an author to an already existing book.
     */
    public void onAddAuthorSelected() {
        AuthorDialog dialog = new AuthorDialog();
        Optional<Author> result = dialog.showAndWait();
        if (result.isPresent()) {
            Author author = result.get();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        if(booksDb.addAuthorToBook(author)){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    booksView.showAlertAndWait("Author added ", CONFIRMATION);
                                }
                            });
                        }
                        else{
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    booksView.showAlertAndWait("Could not add author ", ERROR);
                                }
                            });
                        }
                    }catch (Exception e){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                booksView.showAlertAndWait("Could not add author ", ERROR);
                            }
                        });
                    }
                }
            }).start();
        }
    }
}
