package model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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
     */
    @Override
    public synchronized List<Book> searchByTitle(String search) {
        return null;
    }

    /**
     * Asks database for a list of books with matching ISBN to the string given by user.
     *
     * @param search String of isbn
     * @return a list of books that matches the search

     */
    @Override
    public synchronized List<Book> searchByISBN(String search)  {
        return null;
    }

    /**
     * Asks database for a list of books written by
     * the author provided by user.
     *
     * @param search the name of the author
     * @return a list of books that matches the search

     */
    @Override
    public synchronized List<Book> searchByAuthor(String search)  {
        return null;
    }

    /**
     * Adds a book to the database
     *
     * @param book the book to be added
     */
    @Override
    public boolean addBook(Book book){
        Document doc = bookToDoc(book);
        System.out.println(doc);
        return true;
    }

    /**
     * Adds an author to an already exciting book
     * @param author the author to be added
     */
    @Override
    public boolean addAuthorToBook(Author author) {
        return true;
    }

    @Override
    public Document bookToDoc(Book book) {
        Document doc = new Document("isbn",book.getIsbn()).append("title",book.getTitle())
                .append("genre",book.getGenre()).append("rating",book.getRating());
        return doc;
    }

    @Override
    public Document authorToDoc(Author author) {
        Document doc = new Document("name",author.getName()).append("dateOfBirth",author.getDateOfBirth());
        return doc;
    }

    @Override
    public Book docToBook(Document doc) {
        Book book = new Book(doc.getString("isbn"),doc.getString("title"),Genre.valueOf(doc.getString("genre")),doc.getInteger("rating"));
        return book;
    }


    @Override
    public String toString() {
        return "Booksdb{" +
                "books=" + books +
                ", con=" + mongoCon +
                '}';
    }
}
