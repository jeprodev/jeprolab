package com.jeprolab.assets.tools;

import jdk.internal.org.xml.sax.Attributes;
import org.w3c.dom.Attr;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * Created by jeprodev on 02/05/2014.
 */
public class JeproLabUpdater {
    public enum Mode {FILE, URL}
    public enum Action {MOVE, DELETE, EXECUTE}
    private final static String versionUrl = "http://localhost/jeprotest/product.html"; //index.php?option=com_jeproshop&view=product&task=view&product_id=1";
    private final static String historyUrl = "https://localhost/jeprotest/product.html"; //index.php?option=com_jeproshop&view=product&task=view&product_id=1";

    public static String getLatestVersion() throws Exception{
        String data = getData(versionUrl);

        return data.substring(data.indexOf("[version]") + 9, data.indexOf("[/version]"));
    }

    public static String getWhatsNew() throws Exception{
        String data = getData(historyUrl);
        return data.substring(data.indexOf("[history]") + 9, data.indexOf("[/history]"));
    }

    public static String getData(String address){
        try {
            URL appUrl = new URL(address);

            InputStream html = appUrl.openStream();

            int c = 0;
            StringBuilder builder = new StringBuilder("");

            while (c != -1) {
                c = html.read();
                builder.append((char) c);
            }
            return builder.toString();
        }catch (IOException ex){
            return "";
        }
    }

    public static void checkForNewVersion(String currentVersion){
        try {
            String lastVersion = getLatestVersion();
            System.out.println(lastVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void update(String xmlInstructionPath, String tmp, Mode mode){
        Iterator parserIterator;
        parserIterator = parse(tmp + File.separator + xmlInstructionPath, mode).iterator();
        JeproLabUpdaterInstruction instruction;

        while(parserIterator.hasNext()){
            instruction = (JeproLabUpdaterInstruction)parserIterator.next();
            switch (instruction.getAction()){
                case MOVE:
                    copy(tmp + File.separator + instruction.getFileName(), instruction.getTargetPath());
                    break;
                case DELETE:
                    delete(instruction.getTargetPath());
                    break;
                case EXECUTE:
                    try {
                        Runtime.getRuntime().exec("java -jar " + tmp + File.separator + instruction.getFileName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public static void update(String xmlInstructions, String tmp, String targetDirectory, Mode mode){
        Iterator parserIterator = parse(tmp + File.separator + xmlInstructions, mode).iterator();
        JeproLabUpdaterInstruction instruction;
        while(parserIterator.hasNext()){
            instruction = (JeproLabUpdaterInstruction) parserIterator.next();
            switch (instruction.getAction()){
                case MOVE:
                    copy(tmp + File.separator + instruction.getFileName(), targetDirectory + File.separator + instruction.getTargetPath());
                    break;
                case DELETE:
                    delete(targetDirectory + File.separator + instruction.getTargetPath());
                    break;
                case EXECUTE:
                    try {
                        Runtime.getRuntime().exec("java -jar " + tmp + File.separator + instruction.getFileName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private static void copy(String sourcePath, String destinationPath){
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        try {
            InputStream inputStream = new FileInputStream(sourceFile);
            OutputStream outputStream = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[512];
            int length;

            while((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void delete(String filePath){
        File targetFile = new File(filePath);
        targetFile.deleteOnExit();
    }

    public static ArrayList<JeproLabUpdaterInstruction> parse(String fileName, Mode mode){
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            JeproLabUpdaterParserHandler handler = new JeproLabUpdaterParserHandler();
            reader.setContentHandler(handler);
            reader.setErrorHandler(handler);

            if(mode == Mode.FILE){
                try {
                    reader.parse(new InputSource(new FileReader(new File(fileName))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    URL url = new URL(fileName);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    reader.parse(new InputSource(inputStream));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return handler.getInstructions();
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class JeproLabUpdaterParserHandler extends DefaultHandler{
        private String currentElement = "";
        private ArrayList<JeproLabUpdaterInstruction> instructions = new ArrayList<>();
        private JeproLabUpdaterInstruction instruction = new JeproLabUpdaterInstruction();
        private boolean inInstruction =  false;

        public JeproLabUpdaterParserHandler(){
            super();
        }


        public void startElement(String uri, String localName, String qName, Attributes attributes){
            currentElement = qName;
            inInstruction = true;
        }

        @Override
        public void endElement(String nameSpaceUri, String localName, String qName){
            inInstruction = false;

            if(qName.equals("instruction")){
                instructions.add(instruction);
                instruction = new JeproLabUpdaterInstruction();
                currentElement = "";
            }
        }

        @Override
        public void characters(char ch[], int start, int length){
            String value = "";
            if(!currentElement.equals("")){
                value = String.copyValueOf(ch, start, length).trim();
            }

            if(inInstruction){
                if(currentElement.equals("action")){
                    instruction.setAction(value);
                }else if(currentElement.equals("destination")){
                    instruction.setDestination(value);
                }else if(currentElement.equals("file")){
                    instruction.setFileName(value);
                }
                currentElement = "";
            }
        }
        public ArrayList<JeproLabUpdaterInstruction> getInstructions(){
            return instructions;
        }
    }

    private static class JeproLabUpdaterInstruction{
        private Action action;
        private String targetPath, fileName;

        public Action getAction(){
            return action;
        }

        public String getTargetPath(){
            return targetPath;
        }

        public String getFileName(){
            return fileName;
        }

        public void setAction(Action action) {
            this.action = action;
        }

        public void setAction(String value) {
            if (value.equalsIgnoreCase("MOVE")) {
                this.action = Action.MOVE;
            } else if (value.equalsIgnoreCase("DELETE")) {
                this.action = Action.DELETE;
            } else if (value.equalsIgnoreCase("EXECUTE")) {
                this.action = Action.EXECUTE;
            }
        }

        public void setDestination(String target) {
            this.targetPath = target;
        }

        public void setFileName(String filename) {
            this.fileName = filename;
        }

    }

    /**
     * JeproLabDownloader
     */
    public class JeproLabDownLoader {
        public void download(String xmlFiles, String targetDirectory, Mode mode){
            //JeproLabXmlDownLoaderParser parser = new JeproLabXmlDownLoaderParser();
            Iterator iterator = downloaderParse(xmlFiles, mode).iterator();
            URL url;
            File dir = new File(targetDirectory);
            if(!dir.exists()){
                dir.mkdir();
            }
            try {
                while (iterator.hasNext()) {
                    url = new URL((String) iterator.next());
                    browseAndGet(url, targetDirectory + File.separator + new File(url.getFile()).getName());
                }
            } catch(IOException ex){
                ex.printStackTrace();
            }
        }

        private void browseAndGet(URL url, String target){
            try {
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                File targetFile = new File(target);
                OutputStream targetOutputStream = new FileOutputStream(targetFile);

                byte[] buffer = new byte[512];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    targetOutputStream.write(buffer, 0, length);
                }
                inputStream.close();
                targetOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private ArrayList<String> downloaderParse(String fileName, Mode mode){
            try {
                XMLReader reader = XMLReaderFactory.createXMLReader();
                JeproLabXmlDownLoaderParserHandler handler = new JeproLabXmlDownLoaderParserHandler();
                reader.setContentHandler(handler);
                reader.setErrorHandler(handler);

                if (mode == Mode.FILE) {
                    reader.parse(new InputSource(new FileReader(new File(fileName))));
                } else {
                    URL url = new URL(fileName);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    reader.parse(new InputSource(inputStream));
                }

                return handler.getFiles();
            }catch (SAXException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class JeproLabXmlDownLoaderParserHandler extends DefaultHandler{
        private String currentElement = "";
        private ArrayList<String> files = new ArrayList<>();

        public JeproLabXmlDownLoaderParserHandler(){
            super();
        }


        public void startElement(String uri, String name, String qName, Attributes attributes){
            currentElement = qName;
        }

        @Override
        public void characters(char ch[], int start, int length){
            String value = null;
            if(!currentElement.equals("")){
                value = String.copyValueOf(ch, start, length).trim();
            }

            if(currentElement.equals("file")){
                files.add(value);
            }
            currentElement = "";
        }

        public ArrayList<String> getFiles(){
            return files;
        }
    }
}