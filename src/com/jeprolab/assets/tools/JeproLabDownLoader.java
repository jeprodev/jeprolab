package com.jeprolab.assets.tools;

import com.jeprolab.assets.tools.exception.JeproLabUncaughtExceptionHandler;
import org.apache.log4j.Level;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * Created by jeprodev on 15/03/2016.
 */
public class JeproLabDownLoader {
    public void download(String xmlFile, String targetDirectory, JeproLabUpdater.Mode mode){
        Iterator iterator = parse(xmlFile, mode).iterator();
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
        } catch(IOException ignored){
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
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
        } catch (IOException ignored) {
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
        }
    }

    private ArrayList<String> parse(String fileName, JeproLabUpdater.Mode mode){
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            JeproLabXmlDownLoaderParserHandler handler = new JeproLabXmlDownLoaderParserHandler();
            reader.setContentHandler(handler);
            reader.setErrorHandler(handler);
            System.out.println(fileName);
            if (mode == JeproLabUpdater.Mode.FILE) {
                reader.parse(new InputSource(new FileReader(new File(fileName))));
            } else {
                URL url = new URL(fileName);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                reader.parse(new InputSource(inputStream));
            }

            return handler.getFiles();
        }catch (SAXException ignored) {
            JeproLabUncaughtExceptionHandler.logExceptionMessage(Level.ERROR, ignored);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private class JeproLabXmlDownLoaderParserHandler extends DefaultHandler {
        private String currentElement = "";
        private ArrayList<String> files = new ArrayList<>();

        public JeproLabXmlDownLoaderParserHandler(){
            super();
        }

        @Override
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
