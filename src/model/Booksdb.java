package model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;

/**
 * The model facade which interacts with the controller and the rest if the classes in
 * the model. Implements the interface "DatabaseMethods"
 *
 * @author sofia och micke
 */
public class Booksdb implements DatabaseMethods{
    private List<Book> books;
    //private Connection con;
    private MongoClient mongoCon;

    public Booksdb(){
        books = new ArrayList<Book>();
    }

    /**
     * Connects to the database.
     *
     * @param database the name of the database to connect to
     * @return true if connection is established.
     * @throws SQLException
     */
    @Override
    public boolean connect(String database) {
        if(database==null) {
            return false;
        } else {
            mongoCon = new MongoClient( "localhost" , 27017 );
            MongoDatabase db = mongoCon.getDatabase(database);
        }
        return true;
    }

    /**
     * Loads the driver so that communication with the server in SQL is possible
     *
     * @return true if load of JAR-file is successful
     */
    private boolean loadDriver(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (Exception e){
            return false;
        }
        return true;
    };

    /**
     * Effectively disconnects from the database
     *
     * @return true if successful
     * @throws SQLException
     */
    @Override
    public boolean disconnect() {
        mongoCon.close();
        return true;
    }

    /**
     * Asks database for a list of books with matching title to the string given by user.
     *
     * @param search String of title
     * @return a list of books that matches the serach
     * @throws SQLException
     */
    @Override
    public synchronized List<Book> searchByTitle(String search) throws SQLException {
        return null;
    }

    /**
     * Asks database for a list of books with matching ISBN to the string given by user.
     *
     * @param search String of isbn
     * @return a list of books that matches the search
     * @throws SQLException
     */
    @Override
    public synchronized List<Book> searchByISBN(String search) throws SQLException {
        return null;
    }

    /**
     * Asks database for a list of books written by
     * the author provided by user.
     *
     * @param search the name of the author
     * @return a list of books that matches the search
     * @throws SQLException
     */
    @Override
    public synchronized List<Book> searchByAuthor(String search) throws SQLException {
        return null;
    }

    /**
     * Adds a book to the database
     *
     * @param book the book to be added
     * @throws SQLException
     */
    @Override
    public boolean addBook(Book book) throws SQLException {
        return true;
    }

    /**
     * Adds an author to an already exciting book
     * @param author the author to be added
     * @throws SQLException
     */
    @Override
    public boolean addAuthorToBook(Author author) throws SQLException  {
        return true;
    }

    @Override
    public String toString() {
        return "Booksdb{" +
                "books=" + books +
                ", con=" + mongoCon +
                '}';
    }
}
