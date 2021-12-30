import net.nosocial.traits.core.CharacterTraits;
import net.nosocial.traits.core.TraitsDb;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * @author ikh
 * @since 1/25/15
 */
public class TraitsDbTest {
    @Test
    public void containsParsedAmountOfTraits() throws IOException {
        TraitsDb db = new TraitsDb("src-test/traits-test.db");
        CharacterTraits traits = db.loadAll();

        assertEquals(5, traits.getCount());
        assertEquals(9, traits.getBehaviorsCount());
    }
}
