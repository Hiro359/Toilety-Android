package com.example.kazuhiroshigenobu.googlemaptraining;

import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReadXmlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_xml);

        Log.i("readXML loaded", "333");

        readXmlData();
    }

    private void readXmlData(){
        try {
            InputStream is = getAssets().open("new_file");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("Placemark");
//            NodeList nList = doc.getElementsByTagName("employee");

            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;


                    String name = getValue("name",element2);

                    String coordinate = getValue("coordinates", element2);

                    String latitude = coordinate.split(",")[0];
                    String longitude = coordinate.split(",")[1];


                    Double latDouble = Double.parseDouble(latitude);
                    Double lonDouble = Double.parseDouble(longitude);


                    Log.i("name 222",name);
                    Log.i("lat 222",String.valueOf(latDouble));
                    Log.i("lon 222",String.valueOf(lonDouble));








                    List<String> elephantList = Arrays.asList(coordinate.split(","));

                    System.out.print(elephantList + "444");







//                    tv1.setText(tv1.getText()+"\nName : " + getValue("name", element2)+"\n");
//                    tv1.setText(tv1.getText()+"Surname : " + getValue("surname", element2)+"\n");
//                    tv1.setText(tv1.getText()+"-----------------------");
                }
            }

        } catch (Exception e) {e.printStackTrace();}

    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

//    private static String getNodeValue(String tag, Element element) {
//        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
//        Node node = nodeList.item(0);
//        return node.getNodeValue();
//    }
//
//    //I have made this June 10.




    }

