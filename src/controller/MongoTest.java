package controller;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Booksdb;
import org.bson.Document;

import java.util.Arrays;

class MongoTest {
    public static void main(String args[]) {
        Booksdb db = new Booksdb();
        db.connect("Test");

        // Creating a Mongo client
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        /*// Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "Test",
                "password".toCharArray());
        System.out.println("Connected to the database successfully");*/

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("Booksdb");
       // System.out.println("Credentials ::"+ credential);

        //Creating a collection
        database.createCollection("books");
        MongoCollection collection = database.getCollection("books");

        System.out.println("Collection created successfully");

        Document doc = new Document("isbn", "1234567890")
                .append("title", "Harry Potter")
                .append("genre", "horror")
                .append("rating", 1);

        collection.insertOne(doc);
    }
}

