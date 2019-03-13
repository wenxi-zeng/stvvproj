package edu.utdallas.group9;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        String packageName = getPackgeName();
        if (packageName == null)
            System.out.println("Failed to read package name from pom.xml");
        else {
            CoverageManager.getInstance().setProgramName(packageName);
            inst.addTransformer(new ClassTransformer(packageName));
        }
    }

    private static String getPackgeName() {
        try {
            File fXmlFile = new File("pom.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getFirstChild().getChildNodes();
            if (nodeList == null || nodeList.getLength() == 0) return null;
            Node node = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().equals("groupId")) {
                    node = nodeList.item(i);
                    break;
                }
            }
            if (node == null) return null;
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element)node;
                return element.getTextContent();
            }
            return null;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return null;
    }
}
