

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Launching a Sharded Distributed MongoDB
 */
public class UsersGenerator {
    private static final String MONGO_DB_URL = "mongodb://127.0.0.1:27023";
    private static final String DB_NAME = "videodb";
    private static final String COLLECTION_NAME = "users";

    private static final Random random = new Random();

    public static void main(String[] args) {
        MongoDatabase usersDb = connectToMongoDB(MONGO_DB_URL, DB_NAME);

        System.out.println("Successfully connected to " + DB_NAME);

        generateUsers(10000, usersDb, COLLECTION_NAME);
    }

    private static void generateUsers(int numberOfUsers, MongoDatabase usersDb, String collectionName) {
        List<Document> userDocuments = new ArrayList<>(numberOfUsers);

        System.out.println("Generating " + numberOfUsers + " users");
        for (int i = 0; i < numberOfUsers; i++) {
            Document userDocument = new Document();

            userDocument.append("user_name", generateUserName())
                    .append("favorite_genres", generateMovieGenres())
                    .append("watched_movies", generateMovieNames())
                    .append("subscription_month", generateSubscriptionMonth());

            userDocuments.add(userDocument);
        }

        MongoCollection<Document> collection = usersDb.getCollection(collectionName);

        System.out.println("Finished generating users");
        collection.insertMany(userDocuments);
    }

    private static MongoDatabase connectToMongoDB(String url, String dbName) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(url));
        return mongoClient.getDatabase(dbName);
    }

    /**
     * Returns a random user name
     */
    private static String generateUserName() {
        StringBuilder name = new StringBuilder();

        name.append(RandomStringUtils.randomAlphabetic(1).toUpperCase());
        name.append(RandomStringUtils.randomAlphabetic(5, 10).toLowerCase());

        return name.toString();
    }

    /**
     * Returns a random list of favorite movie genres
     */
    private static List<String> generateMovieGenres() {
        int numberGenres = random.nextInt(4);
        List<String> movies = new ArrayList<>(numberGenres);

        for (int i = 0; i < numberGenres; i++) {
            String movieName = RandomStringUtils.randomAlphabetic(5, 10);
            movies.add(movieName);
        }

        return movies;
    }

    /**
     * Returns a subsciption month
     */
    private static int generateSubscriptionMonth() {
        return random.nextInt(12) + 1;
    }

    /**
     * Returns a random list of movies
     *
     * @return
     */
    private static List<String> generateMovieNames() {
        int numberOfWatchedMovies = random.nextInt(100);
        List<String> movies = new ArrayList<>(numberOfWatchedMovies);

        for (int i = 0; i < numberOfWatchedMovies; i++) {
            String movieName = RandomStringUtils.randomAlphabetic(5, 25);
            movies.add(movieName);
        }

        return movies;
    }


}
