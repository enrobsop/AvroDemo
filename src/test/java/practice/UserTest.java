package practice;

import example.avro.User;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the generated {@link example.avro.User} class.
 *
 * Run 'mvn generate-sources' before running this test. 'mvn test' will do this automatically.
 *
 * @author Paul Osborne
 */
public class UserTest {

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

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

    @Test
    public void itSerializesWithCodeGen() throws Exception {

        final User user1 = new User("User1", 7, "red");
        final User user2 = new User("User2", 7, null);
        final User user3 = new User("User3", 7, "blue");
        final File storageFile = tmpDir.newFile("users.avro");

        // Write to disk
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter);
        dataFileWriter.create(user1.getSchema(), storageFile);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.append(user3);
        dataFileWriter.close();

        // Read from disk
        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        DataFileReader<User> dataFileReader = new DataFileReader<>(storageFile, userDatumReader);

        final List<User> users = new ArrayList<>();
        while (dataFileReader.hasNext()) {
            users.add(dataFileReader.next());
        }

        assertEquals("User1", users.get(0).getName().toString());
        assertEquals("User2", users.get(1).getName().toString());
        assertEquals("User3", users.get(2).getName().toString());

    }

    @Test
    public void itSerializesWithoutCodeGen() throws Exception {

        final Schema schema = new Schema.Parser().parse(resource("src/main/avro/user.avsc"));
        final User user1 = new User("User1", 7, "red");
        final User user2 = new User("User2", 7, null);

        // Write
        File file = tmpDir.newFile("users2.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.close();

        // Read
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader);

        final List<GenericRecord> users = new ArrayList<>();
        while (dataFileReader.hasNext()) {
            users.add(dataFileReader.next());
        }

        assertEquals(2, users.size());
        assertEquals("User1", users.get(0).get("name").toString());
        assertEquals("User2", users.get(1).get("name").toString());

    }

    private File resource(final String resourcePath) {
        final String projectRootPath = Class.class.getResource("/").getFile() + "../../";
        return new File(
            projectRootPath + resourcePath);
    }
}
