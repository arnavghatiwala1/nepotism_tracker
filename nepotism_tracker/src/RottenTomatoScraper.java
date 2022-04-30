import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class RottenTomatoScraper {
    private Document currentDoc;
    private HashMap<String, String> actorMap = new HashMap<>();
    private HashMap<String, List<String>> movieMap = new HashMap<>();
    private HashMap<String, List<Integer>> everyMovieRatingMap = new HashMap<>();
    private HashMap<String, Integer> averageMovieRatingMap = new HashMap<>();
    private HashMap<String, List<Integer>> everyBoxOffice = new HashMap<>();
    private HashMap<String, Integer> averageBoxOffice = new HashMap<>();
    private String baseURL;

    public RottenTomatoScraper() {
        WikipediaScraper ws = new WikipediaScraper();
        baseURL = "https://www.rottentomatoes.com";
        for (String actor : ws.getActors().keySet()) {
            String URL = ws.getActors().get(actor);
            URL = URL.toLowerCase();
            URL = URL.replace("/wiki", "/celebrity");
            URL = URL.replace(".", "");
            URL = URL.replace("_(actor)", "");
            URL = URL.replace("_(hindi_actor)", "");
            URL = URL.replace("_(bollywood_actor)", "");
            URL = URL.replace("_(bollywood_actor)", "");
            URL = URL.replace("_(comedian)", "");
            URL = URL.replace("_(actor,_born_1959)", "");
            URL = URL.replace("_(politician)", "");
            URL = URL.replace("%27", "");
            URL = URL.replace("_(actress)", "");
            URL = URL.replace("[^a-zA-Z_-]", "");
            URL = URL.replace("_(actor,_born_1955)", "");
            URL = URL.replace("_(dj)", "");
            URL = URL.replace("_(hindi_actress)", "");
            URL = URL.replace("_(singer)", "");
            URL = URL.replace("_(assamese_actress)", "");
            URL = URL.replace("_(composer)", "");
            URL = URL.replace("%e2%80%93", "");
            URL = URL.replace("/w/indexphp?title=shubhangi_latkar&action=edit&redlink=1",
                    "/celebrity/shubhangi_latkar");
            actorMap.put(actor, URL);
            System.out.println(actor);
            System.out.println(URL);
        }
    }

      void getMovieList() {
        List<String> movie = new ArrayList<>();
        for (String actor : actorMap.keySet()) {
            try {
                String URL = baseURL.concat(actorMap.get(actor));
                System.out.println(URL);
                this.currentDoc = Jsoup.connect(URL).get();
            } catch (IOException e) {
                System.out.println("Link Not Found");
            }
            Elements row =  currentDoc.getElementsByClass("celebrity-filmography__tbody");
            for ( Element curr: row){
                Elements titles = curr.getElementsByTag("tr");
                for ( Element title: titles){
                    if(curr.getElementsByAttribute("href") != null) {
                        movie.add(Objects.requireNonNull(title.getElementsByAttribute("href")).text());
                        System.out.println(title.getElementsByAttribute("href").text());
                    }
                }
            }
            movieMap.put(actor, movie);
        }
    }

    void getMovieRating() {
        List<Integer> movieRating = new ArrayList<>();
        int rating = 0;
        for (String actor : actorMap.keySet()) {
            try{
                String URL = baseURL.concat(actorMap.get(actor));
                System.out.println(URL);
                this.currentDoc = Jsoup.connect(URL).get();
            } catch (IOException e) {
                System.out.println("Link Not Found");
            }
            Elements row = currentDoc.getElementsByClass("celebrity-filmography__tbody");
            for (Element curr : row) {
                Elements titles = curr.getElementsByTag("span");
                for (Element title : titles) {
                    Elements name = title.getElementsByAttribute("data-audiencescore");
                    for (Element el : name) {
                        if (el.text() != null) {
                            String score = el.text();
                            if ( score.equals("No Score Yet")){
                                score = "0";
                            }
                            score = score.replace("%", "");
                            score = score.trim();
                            score = score.trim();
                            System.out.println(score);
                            movieRating.add(Integer.parseInt(score));
                            rating = rating + Integer.parseInt(score);
                        }
                    }
                }
            }
            averageMovieRatingMap.put(actor, rating/movieRating.size());
            everyMovieRatingMap.put(actor, movieRating);
            rating = 0;
        }
    }


    void getBoxOffice() {
        List<Integer> everyBoxOffice = new ArrayList<>();
        int rating = 0;
        for (String actor : actorMap.keySet()) {
            try{
                String URL = baseURL.concat(actorMap.get(actor));
                System.out.println(URL);
                this.currentDoc = Jsoup.connect(URL).get();
            } catch (IOException e) {
                System.out.println("Link Not Found");
            }
            Elements row = currentDoc.getElementsByClass("celebrity-filmography__tbody");
            for (Element curr : row) {
                Elements titles = curr.getElementsByClass("celebrity-filmography__box-office");
                for (Element title : titles) {
                    Elements name = title.getElementsByAttribute("data-qa");
                    for (Element el : name) {
                        if (el.text() != null) {
                            String score = el.text();
                            if ( score.equals("-")){
                                score = "0";
                            }
                            score = score.replace("$", "");
                            if (score.contains("K")) {
                                  score = score.replace("K", "00");
                                  score = score.replace(".", "");
                                 }
                            if (score.contains("M")) {
                                score = score.replace("M", "00000");
                                score = score.replace(".", "");
                            }
                            score = score.trim();
                            score = score.trim();
                            System.out.println(score);
                            rating = rating + Integer.parseInt(score);
                        }
                    }
                }
            }
            rating = 0;
        }
    }

    private void getMovieYearRange() {

    }

    private void getMovie() {


    }


}
