package be.pocketgames.utils;

import android.util.Log;
import be.pocketgames.models.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataGameGenerator {

    private static String mImageLink;
    private static String mTitle;
    private static Platform mPlatform;
    private static String mEditor;
    private static String mEAN;
    private static Genre mGenre;
    private static List<GameDatabase> gamesObjects = new ArrayList<>();

    private static Document doc;
    private static Element body;
    private static Element bodyDeatilsGamePage;

    public static List<GameDatabase> generateGameDatas(Platform platform, String url) {
        initValues();
        int currentPage = 1;
        int cpt = 2;
        doc = getDocument(url);
        body = getBody(doc);

        // récuperer le nombre de pages de jeux
        Elements pagination =  doc.getElementsByClass("page__3yoZnY");
        int lastPage = Integer.parseInt(pagination.get(pagination.size() - 1 ).text());

        while( (currentPage <= lastPage) && (cpt <= 2) ) {

            Elements gamesInfos = getGameBloc("container__3Ow3zD");
            System.out.println("page n: " + currentPage);

            for (Element gameInfos : gamesInfos) {

                try {

                    bodyDeatilsGamePage = getDocument("http://www.jeuxvideo.com" + gameInfos.getElementsByClass("gameTitleLink__196nPy")
                            .get(0)
                            .attr("href"))
                            .body();
                    mPlatform = platform;
                    mTitle = getGameTitle();
                    mImageLink = getImageUrl();
                    addGenres();
                    mEditor = getEditors();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("problem vers mImage, mGenre ou mEditor");
                }

                addGame(mImageLink, mTitle, mPlatform, mEditor, mGenre, mEAN);

                System.out.println("nb d'objets : " + gamesObjects.size());

                resetValuesGame();
            }

            doc = getDocument("http://www.jeuxvideo.com/tous-les-jeux/machine-177539/?p=" + currentPage);
            body = getBody(doc);

            currentPage++;
            cpt++;

        }

        endProcess(gamesObjects);
        return gamesObjects;

    }// end generator
    //**************************************************************************************************************
    private static void resetValuesGame() {
        mImageLink = "";
        mPlatform = null;
        mTitle = "";
        mEditor= "";
        mEAN= "";
        mGenre = null;
    }
    //**************************************************************************************************************
    private static void addGame(String mImageLink, String mTitle, Platform mPlatform, String mEditor, Genre mGenre, String mEAN) {
        gamesObjects.add(new GameDatabase(
                mImageLink,
                mTitle,
                mPlatform,
                Editor.ACTIVISION,
                mGenre,
                new ArrayList<String>(),
                ""
        ));
    }
    //**************************************************************************************************************
    private static String getEditors() {
        return bodyDeatilsGamePage
                .getElementsByClass("gameCharacteristicsDetailed__td")
                .get(1)
                .text();
    }
    //**************************************************************************************************************
    private static void addGenres() {
        for( Element e_genre : bodyDeatilsGamePage
                .getElementsByClass("gameCharacteristicsDetailed__td")
                .get(5)
                .getElementsByClass("gameCharacteristicsDetailed__characValue")
        ) {
            Genre g = Genre.valueOf(e_genre.text()
                    .trim()
                    .replace("'", "_")
                    .replace(" ", "_")
                    .toUpperCase());
            mGenre = g;
        } //end foreach
    }
    //**************************************************************************************************************
    private static String getImageUrl() {
        return bodyDeatilsGamePage
                .getElementsByClass("gameCharacteristicsMain__coverImage")
                .get(0)
                .attr("src");
    }
    //**************************************************************************************************************
    private static String getGameTitle() {
        return bodyDeatilsGamePage
                .getElementsByClass("gameHeaderBanner__title")
                .get(0)
                .text();
    }
    //**************************************************************************************************************
    private static Elements getGameBloc(String classGameBloc) {
        return body.getElementsByClass(classGameBloc);
    }
    //**************************************************************************************************************
    private static Element getBody(Document doc) {
        return doc.body();
    }
    //**************************************************************************************************************
    private static void initValues() {
        String mImageLink = "";
        String mTitle = "";
        Platform mPlatform = null;
        String mEditor = "";
        String mEAN = "";
        List<Genre> mGenre = new ArrayList<>();
        List<GameDatabase> gamesObjects = new ArrayList<>();

        Document doc = null;
        Element body = null;
        bodyDeatilsGamePage = null;
    }
    //**************************************************************************************************************
    private static void endProcess(List<GameDatabase> gamesObjects) {
        //affichage des objets récupérés
        for (Game g : gamesObjects
        ) {
            Log.d("GAMES_DATA", g.toString());
        }
    }
    //**************************************************************************************************************
    private static Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            return null;
        }
    }
    //**************************************************************************************************************
}// end class
