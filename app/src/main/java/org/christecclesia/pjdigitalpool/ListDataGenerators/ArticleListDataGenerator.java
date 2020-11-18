package org.christecclesia.pjdigitalpool.ListDataGenerators;

import org.christecclesia.pjdigitalpool.Models.ArticleModel;

import java.util.ArrayList;
import java.util.List;

public class ArticleListDataGenerator {


    // DECLARING THE DATA ARRAY LIST
    static List<ArticleModel> allData = new ArrayList<>();

    // SETTING/RESETTING ALL SUGGESTED LINKUPS DATA
    public static void setAllDatasAfresh(List<ArticleModel> newAllData) {
        ArticleListDataGenerator.allData = newAllData;
    }

    // ADDING ONE DATA TO ARRAY LIST
    public static boolean addOneData(ArticleModel model) {
        return allData.add(model);
    }

    // GETTING ALL DATA AS ARRAY LIST
    public static List<ArticleModel> getAllData() {
        return allData;
    }

    // ADDING ONE DATA TO A DESIRED POSITION IN ARRAY LIST
    public static void addOneDataToDesiredPosition(int i, ArticleModel model){
        allData.add(i, model);
    }
}
