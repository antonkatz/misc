import java.io.File;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by anton on 18/02/14.
 */
public class ethnicityParser
{
    public static void main(String []args)
    {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
// ===================================================================================

        String listFileName = "ListEthnicGroups.html";
        String outputName = "ListEthnicGroups.csv";
        String outputNameXml = "ListEthnicGroups.xml";
        File listFile = new File(listFileName);

        try {
            Document doc = Jsoup.parse(listFile, "UTF-8");
            Elements rows = doc.select("tr");
            PrintWriter output = new PrintWriter(outputName);
            PrintWriter outputXml = new PrintWriter(outputNameXml);
            outputXml.println("<add>");
            for (Element row : rows){
                HashSet<String> entry = new HashSet<String>();
                String ethnicity = row.select("a").get(0).text();
                String number = "1";
                try {
                    Matcher matcher = Pattern.compile("^([0-9,]+)[ 0-9,-]+").matcher(row.select("td").get(3).text());
                    if (matcher.find()){
                        number = matcher.group(1).replaceAll(",", "");
                    }
                } catch (Exception ex) {
                    //Do nothing
                }
                entry.add(ethnicity);
                entry.add(number);

                Integer intNumber = Integer.parseInt(number);
                outputXml.println("<doc boost=\"" + Math.log10(intNumber) + "\"><field name=\"id\">ETHNO:" +
                    URLEncoder.encode(ethnicity.toLowerCase(), "UTF-8").replaceAll("[^a-z]", "") +
                    "</field><field name=\"name\">"+ethnicity+"</field></doc>");
                output.println(ethnicity + "\t" + number);
            }
            outputXml.println("</add>");
            output.close();
            outputXml.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
//<doc boost="log10(population)"><field name="id">ETHNO: + URLEncode(name.toLowerCase()).replaceAll("[^a-z]", '')panamanian</field><field name="name">Panamanian</field></doc><doc boost="log10(population)"><field name="id">ETHNO: + URLEncode(name.toLowerCase()).replaceAll("[^a-z]", '')panamanian</field><field name="name">Panamanian</field></doc>

