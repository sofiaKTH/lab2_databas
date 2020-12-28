import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import model.Booksdb;

class MongoTest {
    public static void main(String args[]) {
        Booksdb db = new Booksdb();
        db.connect("Test");

        /*
        // Creating a Mongo client
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "Test",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("Test");
        System.out.println("Credentials ::"+ credential);

        //Creating a collection
        database.createCollection("NUJÄVLAR");
        System.out.println("Collection created successfully");*/
    }
}

