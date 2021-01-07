package model;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import static com.mongodb.client.model.Filters.*;

/**
 * The model facade which interacts with the controller and the rest if the classes in
 * the model. Implements the interface "DatabaseMethods"
 *
 * @author sofia och micke
 */
public class Booksdb implements DatabaseMethods{
    private List<Book> books;
    private MongoClient mongoCon;
    private MongoDatabase db;
    private final String dbCollection = "books";

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
            db = mongoCon.getDatabase(database);
        }
        return true;
    }


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
     * @return a list of books that matches the search
     */
    @Override
    public synchronized List<Book> searchByTitle(String search) {
        MongoCollection<Document> collection = db.getCollection(dbCollection);
        List<Book> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
        MongoCursor<Document> cursor = collection.find(regex("title", pattern)).iterator();
        try {
            while (cursor.hasNext()) {
                list.add(docToBook((Document.parse(cursor.next().toJson()))));
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    /**
     * Asks database for a list of books with matching ISBN to the string given by user.
     *
     * @param search String of isbn
     * @return a list of books that matches the search

     */
    @Override
    public synchronized List<Book> searchByISBN(String search)  {
        MongoCollection<Document> collection = db.getCollection(dbCollection);
        List<Book> list = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find(regex("isbn", search)).iterator();
        try {
            while (cursor.hasNext()) {
                list.add(docToBook((Document.parse(cursor.next().toJson()))));
            }
        } finally {
            cursor.close();
        }
        return list;
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
        MongoCollection<Document> collection = db.getCollection(dbCollection);
        List<Book> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
        MongoCursor<Document> cursor = collection.find(regex("authors.name", pattern)).iterator();

        try {
            while (cursor.hasNext()) {
                list.add(docToBook((Document.parse(cursor.next().toJson()))));
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    /**
     * Adds a book to the database
     *
     * @param book the book to be added
     */
    @Override
    public boolean addBook(Book book) throws MongoException {
        MongoCollection collection = db.getCollection(dbCollection);
        Document doc = bookToDoc(book);
        try {
            collection.insertOne(doc);
        } catch (Exception mongoException) {
            throw mongoException;
        }

        return true;
    }

    /**
     * Adds an author to an already existing book
     * @param author the author to be added
     */
    @Override
    public boolean addAuthorToBook(Author author) {
        MongoCollection collection = db.getCollection(dbCollection);
        try {
            System.out.println(collection.updateOne(eq("isbn",author.getIsbn()),
                    Updates.addToSet("authors", authorToDoc(author))));

        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    /**
     * Converts a Book object to a Document object.
     *
     * @param book
     * @return
     */
    @Override
    public Document bookToDoc(Book book) {
        Document doc = new Document("isbn",book.getIsbn()).append("title",book.getTitle())
                .append("genre",book.getGenre().toString()).append("rating",book.getRating());
        doc.append("authors", new ArrayList<>());
        return doc;
    }

    /**
     * Converts an Author object to a Document object.
     *
     * @param author
     * @return
     */
    @Override
    public Document authorToDoc(Author author) {
        Document doc = new Document("name",author.getName()).append("dateOfBirth",author.getDateOfBirth());
        return doc;
    }

    /**
     * Converts a Document object to a Book object.
     *
     * @param doc
     * @return
     */
    @Override
    public Book docToBook(Document doc) {
        Book book = new Book(doc.getString("isbn"),doc.getString("title"),Genre.valueOf(doc.getString("genre").toUpperCase()),doc.getInteger("rating"));
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
