package practice;

import example.avro.User;
import org.junit.Test;

/**
 * Tests for the generated {@link example.avro.User} class.
 *
 * Run 'mvn generate-sources' before running this test. 'mvn test' will do this automatically.
 *
 * @author Paul Osborne
 */
public class UserTest {

    @Test
    public void itCreatesUsers() throws Exception {

        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);
        // Leave favorite color null

        // Alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Construct via builder
        User user3 = User.newBuilder()
            .setName("Charlie")
            .setFavoriteColor("blue")
            .setFavoriteNumber(null)
            .build();

    }



}
