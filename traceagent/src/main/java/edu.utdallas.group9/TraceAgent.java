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
import java.util.HashSet;

public class TraceAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        HashSet<String> allPackages = getAllPackages();
        String packageName = shortestPackage(allPackages);
        if (packageName == null || packageName.length() == 0)
            System.out.println("Failed to read package name from pom.xml");
        else {
            TraceManager.getInstance().setProgramName(getPackgeName());
            inst.addTransformer(new TraceClassTransformer(packageName));
        }
    }

    private static String shortestPackage(HashSet<String> packages) {
        String result = null;
        for (String packageName : packages) {
            if (result == null || packageName.length() < result.length())
                result = packageName;
        }

        return result;
    }

    private static HashSet<String> getAllPackages() {
        String prefix = "src" + File.separator + "main" + File.separator + "java";
        File root = new File(prefix);
        File[] list = root.listFiles();
        HashSet<String> packages = new HashSet<>();
        listAllPackages(list, packages, prefix);

        return packages;
    }

    private static void listAllPackages(File[] files, HashSet<String> packages, String prefix) {
        for (File file : files) {
            if (file.isDirectory()) {
                listAllPackages(file.listFiles(), packages, prefix);
            } else {
                String path = file.getParent();
                path = path.substring(path.lastIndexOf(prefix) + prefix.length() + 1);
                packages.add(path);
            }
        }
    }

    private static String getPackgeName() {
        try {
            File fXmlFile = new File("pom.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList rootList = doc.getChildNodes();
            NodeList nodeList = null;
            for (int i = 0; i < rootList.getLength(); i++) {
                if (rootList.item(i).getNodeName().equals("project")) {
                    nodeList = rootList.item(i).getChildNodes();
                    break;
                }
            }
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
