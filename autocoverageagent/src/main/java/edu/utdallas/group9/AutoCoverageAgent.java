package edu.utdallas.group9;

import org.apache.maven.shared.invoker.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Collections;


public class AutoCoverageAgent {

    public static void main(String[] args){
        if (args.length != 1) {
            System.out.println("Invalid arguments. Usage: AutoCoverageAgent [repoUrl]");
            return;
        }

        String repoPath = downloadRepo(args[0]);

        if (repoPath == null) {
            System.out.println("Failed to download repo");
            return;
        }
        String pomPath = repoPath + File.separator + "pom.xml";
        System.out.println("Updating pom.xml...");
        modifyPom(pomPath);
        System.out.println("Updating pom.xml done!");

        System.out.println("Launch maven test...");
        invokeMavenTest(pomPath);
        System.out.println("Test done!");
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

    private static void modifyPom(String pomPath) {
        try {
            File fXmlFile = new File(pomPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getFirstChild().getChildNodes();
            if (nodeList == null || nodeList.getLength() == 0) return;
            Node node = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().equals("build")) {
                    node = nodeList.item(i);
                    break;
                }
            }

            if (node == null){
                System.out.println("No build node found");
                return;
            }

            NodeList pluginManagements = node.getChildNodes();
            Node pluginManagementRef = null;
            Node pluginsRef = null;
            for (int i = 0; i < pluginManagements.getLength(); i++) {
                Node pluginManagement = pluginManagements.item(i);

                if (pluginManagement.getNodeName().equals("pluginManagement")) {
                    pluginManagementRef = pluginManagement;
                    NodeList plugins = pluginManagement.getChildNodes();
                    for (int j = 0; j < pluginManagements.getLength(); j++) {
                        if(updatePlugin(doc, plugins.item(j).getChildNodes(), pomPath))
                            return;
                    }
                }
                else if (pluginManagement.getNodeName().equals("plugins")) {
                    pluginsRef = pluginManagement;
                    if(updatePlugin(doc, pluginManagement.getChildNodes(), pomPath))
                        return;
                }
            }

            if (pluginManagementRef != null) {
                Node plugins = doc.createElement("plugins");
                Node plugin = createMavenPlugin(doc);
                plugins.appendChild(plugin);
                pluginManagementRef.appendChild(plugins);
                save(doc, pomPath);
            }
            else if (pluginsRef != null) {
                Node plugin = createMavenPlugin(doc);
                pluginsRef.appendChild(plugin);
                save(doc, pomPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean updatePlugin(Document doc, NodeList plugins, String path) throws Exception {
        for (int j = 0; j < plugins.getLength(); j++) {
            Node plugin = plugins.item(j);
            //nodeToString(plugin);
            if (plugin.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) plugin;
                NodeList artifacts = eElement.getElementsByTagName("artifactId");
                if (artifacts.item(0).getTextContent().contains("maven-surefire-plugin")) {
                    NodeList config = eElement.getElementsByTagName("configuration");
                    if (config != null && config.getLength() > 0) {
                        try {
                            eElement.removeChild(config.item(0));
                        }
                        catch (DOMException ignored) {}
                    }

                    Node newConfig = generateNewEntry(doc);
                    eElement.appendChild(newConfig);
                    save(doc, path);
                    return true;
                }
            }
        }

        return false;
    }

    private static Node createMavenPlugin(Document doc) {
        Node plugin = doc.createElement("plugin");
        Node groupId = doc.createElement("groupId");
        Node artifactId = doc.createElement("artifactId");
        groupId.setTextContent("org.apache.maven.plugins");
        artifactId.setTextContent("maven-surefire-plugin");
        Node newConfig = generateNewEntry(doc);
        plugin.appendChild(groupId);
        plugin.appendChild(artifactId);
        plugin.appendChild(newConfig);

        return plugin;
    }

    private static Node generateNewEntry(Document doc) {
        Node newConfig = doc.createElement("configuration");
        Node argLine = doc.createElement("argLine");
        argLine.setTextContent("-javaagent:" + getFilePath());
        Node properties = doc.createElement("properties");
        Node property = doc.createElement("property");
        Node name = doc.createElement("name");
        name.setTextContent("listener");
        Node value = doc.createElement("value");
        value.setTextContent("edu.utdallas.group9.JUnitListener");

        property.appendChild(name);
        property.appendChild(value);
        properties.appendChild(property);
        newConfig.appendChild(argLine);
        newConfig.appendChild(properties);
        return newConfig;
    }

    private static void save(Document doc, String filepath) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filepath));
        transformer.transform(source, result);
    }

    private static String getFilePath() {
        File file = new File("Agent-1.0-SNAPSHOT.jar");
        return file.getAbsolutePath();
    }

    private static void nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        System.out.println(sw.toString());
        System.out.println("===========================================================");
    }

    private static void invokeMavenTest(String path) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( path ) );
        request.setGoals( Collections.singletonList( "test" ) );

        Invoker invoker = new DefaultInvoker();
        try {
            invoker.execute( request );
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
    }
}
