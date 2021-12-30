package net.nosocial.traits.core;

import java.io.*;

/**
 * @author ikh
 * @since 1/25/15
 */
public class TraitsDb {
    private InputStream resource;
    private File db;

    public TraitsDb(String fileName) throws IOException {
        this(fileName, false);
    }

    public TraitsDb(InputStream resource) {
        assert resource != null;
        this.resource = resource;
    }

    public TraitsDb(String fileName, boolean failIfNotExist) throws IOException {
        db = new File(fileName);
        if (!db.exists() && failIfNotExist) {
            throw new IllegalArgumentException("Database file doesn't exist: " + fileName);
        }
        if (!db.exists()) {
            boolean newFile = db.createNewFile();
            if (!newFile) {
                throw new IllegalArgumentException("Cannot create new file: " + fileName);
            }
        }
    }


    public void saveAll(CharacterTraits traits) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(db, "UTF-8")) {
            for (Trait trait : traits.getTraitList()) {
                writer.print(trait.getDbName());
                writer.println();
                for (Behavior behavior : trait.getBehaviors()) {
                    writer.print(behavior.getText());
                    writer.println();
                }
            }
        }
    }

    public CharacterTraits loadAll() throws IOException {
        CharacterTraits result = new CharacterTraits();
        if (resource != null) {
            try (InputStreamReader book = new InputStreamReader(resource, "UTF-8")) {
                readAll(result, book);
            }
        } else {
            assert db != null;
            try (Reader book = new InputStreamReader(new FileInputStream(db), "UTF-8")) {
                readAll(result, book);
            }
        }
        return result;
    }

    private void readAll(CharacterTraits result, Reader bookDb) throws IOException {
        BufferedReader reader = new BufferedReader(bookDb);
        Trait trait = null;
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("+") || line.startsWith("-")) {
                trait = new Trait(line.substring(1), line.startsWith("+"));
                result.add(trait);
            } else {
                assert trait != null; // by structure
                trait.addBehavior(new Behavior(line));
            }
        }
    }
}
