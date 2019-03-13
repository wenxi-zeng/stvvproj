package edu.utdallas.group9;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;


public class AutoCoverageAgent {

    public static void main(String[] args){
        if (args.length != 1) {
            System.out.println("Invalid arguments. Usage: AutoCoverageAgent [repoUrl]");
            return;
        }

//        String repoPath = downloadRepo(args[0]);
//
//        if (repoPath == null) {
//            System.out.println("Failed to download repo");
//            return;
//        }
        String repoUrl = args[0];
        String dir = repoUrl.substring(repoUrl.lastIndexOf('/') + 1);
        String cloneDirectoryPath = "downloads" + File.separator + dir;
        modifyPom(cloneDirectoryPath);
    }

    private static String downloadRepo(String repoUrl) {
        String dir = repoUrl.substring(repoUrl.lastIndexOf('/') + 1);
        String cloneDirectoryPath = "downloads" + File.separator + dir;
        try {
            System.out.println("Cloning "+ repoUrl);
            Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(Paths.get(cloneDirectoryPath).toFile())
                    .call();
            System.out.println("Completed Cloning. Repo downloaded to: " + cloneDirectoryPath);
            return cloneDirectoryPath;
        } catch (GitAPIException e) {
            System.out.println("Exception occurred while cloning repo");
            e.printStackTrace();
        }

        return null;
    }

    private static void modifyPom(String repoPath) {
        try {
            File fXmlFile = new File(repoPath + File.separator + "pom.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("build");
            if (nodeList == null || nodeList.getLength() == 0) return;
            Node node = nodeList.item(0);

            NodeList pluginManagements = node.getChildNodes();
            for (int i = 0; i < pluginManagements.getLength(); i++) {
                Node pluginManagement = pluginManagements.item(i);

                if (pluginManagement.getTextContent().equals("pluginManagement")) {
                    NodeList plugins = pluginManagement.getChildNodes();
                    for (int j = 0; j < pluginManagements.getLength(); j++) {
                        updatePlugin(doc, plugins.item(j).getChildNodes(), repoPath + File.separator + "pom.xml");
                    }
                }
                else if (pluginManagement.getTextContent().equals("plugins")) {
                    updatePlugin(doc, pluginManagement.getChildNodes(), repoPath + File.separator + "pom.xml");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updatePlugin(Document doc, NodeList plugins, String path) throws Exception {
        for (int j = 0; j < plugins.getLength(); j++) {
            Node plugin = plugins.item(j);
            if (plugin.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) plugin;
                NodeList artifacts = eElement.getElementsByTagName("artifactId");
                if (artifacts.item(0).getTextContent().contains("maven-surefire-plugin")) {
                    NodeList config = eElement.getElementsByTagName("configuration");
                    if (config != null && config.getLength() > 0) {
                        eElement.removeChild(config.item(0));
                    }

                    Node newConfig = doc.createElement("configuration");
                    Node argLine = doc.createElement("argLine");
                    argLine.setNodeValue("-javaagent:[path-to-your-agent.jar]");
                    Node properties = doc.createElement("properties");
                    Node property = doc.createElement("property");
                    Node name = doc.createElement("name");
                    name.setNodeValue("listener");
                    Node value = doc.createElement("value");
                    value.setNodeValue("[YourListener]");

                    property.appendChild(name).appendChild(value);
                    properties.appendChild(property);
                    newConfig.appendChild(argLine).appendChild(properties);
                    eElement.appendChild(newConfig);
                    save(doc, path);
                }
            }
        }
    }

    private static void save(Document doc, String filepath) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filepath));
        transformer.transform(source, result);
    }
}
