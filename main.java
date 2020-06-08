import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

public class main {
    public static void main(String[] args) throws IOException, DocumentException {
        try (ZipFile ods = new ZipFile(args[0]);
             FileOutputStream fos = new FileOutputStream("content.xml")) {

            byte[] b = ods.getInputStream(ods.getEntry("content.xml")).readAllBytes(); //extracts .xml file
            File file = new File("content.xml");
            file.createNewFile();
            fos.write(b);
        }

        File file = new File("content.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.elements();

        findTable(list);
    }

    private static void findTable(List<Element> list) { //recursively finds element named table and extracts info from cells.
        if (list.stream().anyMatch(element -> element.getName().equals("table"))) {
            List<Element> elemendid = list.stream().flatMap(element -> element.elements().stream()).collect(Collectors.toList());
            for (int i = 0; i < elemendid.size(); i++) {
                List<Element> a = elemendid.get(i).elements();
                for (Element element : a) {
                    System.out.print(element.getStringValue() + "\t");
                }
                System.out.println();
            }
        } else {
            for (Element element : list) findTable(element.elements());
        }
    }
}


