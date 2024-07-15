There is no possibility of parsing wfs 2.0. I found several options on how to parse 
FeatureType from xsd, but all options only work for wfs 1.0 - 1.1

```java
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
```

The second option does not allow you to change the wfs version, only 1 or 1.1


Both methods fail when parsing wfs2, the first one ends in an error and the second one misses the geometry field.

Is it currently possible to parse wfs2 or is this version not supported at the moment?