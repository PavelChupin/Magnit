package com.pavel_chupin;

import com.pavel_chupin.db_support.Data;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static Document createDocXML(List<Data> dataList) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();

        Element entries = doc.createElement("entries");
        doc.appendChild(entries);

        for (Data d : dataList) {
            Element entry = doc.createElement("entry");
            entries.appendChild(entry);

            Element field = doc.createElement("field");
            field.setTextContent(String.valueOf(d.getValue()));
            entry.appendChild(field);
        }
        return doc;
    }

    public static List<Data> parseDocXML(Path path) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        List<Data> data = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = factory.newDocumentBuilder().parse(path.toFile());

        Node entries = doc.getDocumentElement();
        NodeList nl = entries.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            //Node n = nl.item(i);
            NamedNodeMap d = nl.item(i).getAttributes();
            if (d != null) {
                data.add(new Data(Long.parseLong(nl.item(i).getAttributes().getNamedItem("field").getNodeValue())));
            }
        }
        return data;
    }

    public static void saveFile(Document doc, Path path) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(path.toFile()));
    }

    public static void transformXML(Path xsl, Path in, Path out) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        //XSL схема
        Source xslShema = new StreamSource(xsl.toFile());
        Transformer transformer = factory.newTransformer(xslShema);

        Source xmlFileIn = new StreamSource(in.toFile());
        transformer.transform(xmlFileIn, new StreamResult(out.toFile()));
    }
}
