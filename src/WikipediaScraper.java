import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class WikipediaScraper {

    private Document currentDoc;
    private final HashMap<String, String> actorMap;
    private final HashMap<String, String> urlMap;
    private final TreeMap<String, HashSet<String>> actorToAdj;

    public WikipediaScraper() {
        actorMap = new HashMap<>();
        urlMap = new HashMap<>();
        actorToAdj = new TreeMap<>();
        getMaleActors();
        getFemaleActors();
        actorMap.remove("");
        urlMap.remove("");
        actorToAdj.remove("");
        graphCreator();
        System.out.println(actorToAdj);
    }

    private void getMaleActors() {
        //Connecting to Wikipedia page with List of Hindi Film Actors
        try {
            this.currentDoc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_Hindi_film_actors").get();
        } catch (IOException e) {
            System.out.println("Error");
        }
        Elements aTags = currentDoc.select("div.div-col a");
        for (Element a : aTags) {
            actorMap.put(a.text(), a.attr("href"));
            urlMap.put(a.attr("href"), a.text());
        }
    }

    private void getFemaleActors() {
        try {
            this.currentDoc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_Hindi_film_actresses").get();
        } catch (IOException e) {
            System.out.println("Error");
        }
        Elements tableRows = currentDoc.select("table[class=wikitable sortable] tr");
        tableRows.addAll(currentDoc.select("table.sortable tr"));
        for (Element row : tableRows) {
            Elements entries = row.select("td");
            if (!entries.isEmpty()) {
                Element td;
                if (checkNumeric(entries.get(0).text())) {
                    td = entries.get(1);
                } else {
                    td = entries.get(0);
                }
                actorMap.put(td.text(), td.select("a").attr("href"));
                urlMap.put(td.select("a").attr("href"), td.text());
            }
        }
    }

    private boolean checkNumeric(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void graphCreator() {
        for (String name : actorMap.keySet()) {
            actorToAdj.put(name, new HashSet<>());
            String url = actorMap.get(name);
            try {
                this.currentDoc = Jsoup.connect("https://en.wikipedia.org" + url).get();
            } catch (IOException e) {
                System.out.println("Error");
            }
            Elements rows = currentDoc.select("table[class=infobox biography vcard] tr");
            if (rows.isEmpty()) {
                rows = currentDoc.select("table[class=infobox vcard]");
            }
            for (Element row : rows) {
                if (row.text().contains("Spouse(s)") || row.text().contains("Parent(s)") ||
                        row.text().contains("Children") || row.text().contains("Relatives")) {
                    Elements aTags = row.select("a");
                    for (Element aTag : aTags) {
                        String relURL = aTag.attr("href");
                        if (urlMap.containsKey(relURL)) {
                            actorToAdj.get(name).add(urlMap.get(relURL));
                        }
                    }
                }
            }
        }

    }

    public TreeMap<String, HashSet<String>> getActorToAdj() {
        return actorToAdj;
    }
}
