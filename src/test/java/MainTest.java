import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @Test
    void parse_1_WFS1() {
        String expected = "geom";

        assertEquals(Main.parse("xsd_wfs1.xsd"), expected);
    }

    @Test
    void parse_1_WFS2() {
        String expected = "geom";

        assertEquals(Main.parse("xsd_wfs2.xsd"), expected);
    }

    @Test
    void parse_2_WFS1() throws IOException {
        String expected = "geom";

        assertEquals(Main.parse("xsd_wfs1.xsd", "te", "test_table"), expected);
    }

    @Test
    void parse_2_WFS2() throws IOException {
        String expected = "geom";

        assertEquals(Main.parse("xsd_wfs2.xsd", "te", "test_table"), expected);
    }
}