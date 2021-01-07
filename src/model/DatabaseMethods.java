package model;

import org.bson.Document;
import java.util.List;

/**
 * This interface declares methods for querying a Books database.
 * Different implementations of this interface handles the connection and
 * queries to a specific DBMS and database, for example a MySQL or a MongoDB
 * database.
 *
 * @author sofia och micke
 */
public interface DatabaseMethods {
    /**
     * Connect to the database.
     * @param database the name of the database to connect to
     * @return true on successful connection.
     */
    public boolean connect(String database);

    /**
     * Disconnects from the database
     *
     * @return true if disconnected
     */
    public boolean disconnect();

    /**
     * Searches the database for all books by a certain title
     *
     * @param search the title or substring of a title
     * @return the list of books which matches the search string

     */
    public List<Book> searchByTitle(String search);

    /**
     * Searches the database for all books by a certain ISBN
     *
     * @param search the ISBN, must be correct
     * @return
     */
    public List<Book> searchByISBN(String search);

    /**
     *  Searches the database for all books written by a certain author.
     *
     * @param search the name of the author
     * @return the list of books
     */
    public List<Book> searchByAuthor(String search);

    /**
     * Adds book to collection in database
     *
     * @param book the book to be added
     */
    public boolean addBook(Book book) ;

    /**
     * Adds author to a specific book.
     *
     * @param author the author to be added
     */
    public boolean addAuthorToBook(Author author);

    /**
     * Converts a Book object to a Document object.
     *
     * @param book
     * @return
     */
    public Document bookToDoc(Book book);
    /**
     * Converts an Author object to a Document object.
     *
     * @param author
     * @return
     */
    public Document authorToDoc(Author author);
    /**
     * Converts a Document object to a Book object.
     *
     * @param doc
     * @return
     */
    public Book docToBook(Document doc);
}
