import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.feature.NameImpl;
import org.geotools.wfs.GML;
import org.geotools.xml.XSISAXHandler;
import org.geotools.xml.gml.GMLComplexTypes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.nonNull;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("PARSE 1 WFS1 : " + parse("xsd_wfs1.xsd", "te", "test_table"));
        System.out.println("PARSE 2 WFS1 : " + parse("xsd_wfs1.xsd"));
        System.out.println("PARSE 1 WFS2 : " + parse("xsd_wfs2.xsd", "te", "test_table"));
        System.out.println("PARSE 2 WFS2 : " +parse("xsd_wfs2.xsd"));
    }

    public static String parse(String xsdName) {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(xsdName)) {

            XMLReader reader = getXMLReader();

            XSISAXHandler schemaHandler = new XSISAXHandler(URI.create("test"));

            reader.setContentHandler(schemaHandler);
            reader.parse(new InputSource(is));

            SimpleFeatureType featureType = GMLComplexTypes.createFeatureType(schemaHandler.getSchema().getElements()[0]);

            return getGeometryName(featureType);

        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String parse(String xsdPath, String namespace, String local) throws IOException {
        GML gml = new GML(GML.Version.WFS1_1);
        gml.setEncoding(StandardCharsets.UTF_8);

        URL url = Main.class.getClassLoader().getResource(xsdPath);

        Name typeName = new NameImpl("http://www.opengeospatial.net/" + namespace, local);

        SimpleFeatureType featureType = gml.decodeSimpleFeatureType(url, typeName);


        return getGeometryName(featureType);
    }

    private static String getGeometryName(SimpleFeatureType featureType) {
        //System.out.println("FeatureType size " + featureType.getTypes().size());

        return nonNull(featureType.getGeometryDescriptor())
                ? featureType.getGeometryDescriptor().getLocalName()
                : null;
    }

    private static XMLReader getXMLReader() throws ParserConfigurationException, SAXException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        SAXParser parser = parserFactory.newSAXParser();
        return parser.getXMLReader();
    }
}
