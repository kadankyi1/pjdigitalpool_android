package org.christecclesia.pjdigitalpool.ListDataGenerators;

import org.christecclesia.pjdigitalpool.Models.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class VideoListDataGenerator {
    // DECLARING THE DATA ARRAY LIST
    static List<VideoModel> allData = new ArrayList<>();

    // SETTING/RESETTING ALL SUGGESTED LINKUPS DATA
    public static void setAllDatasAfresh(List<VideoModel> newAllData) {
        VideoListDataGenerator.allData = newAllData;
    }

    // ADDING ONE DATA TO ARRAY LIST
    public static boolean addOneData(VideoModel model) {
        return allData.add(model);
    }

    // GETTING ALL DATA AS ARRAY LIST
    public static List<VideoModel> getAllData() {
        return allData;
    }

    // ADDING ONE DATA TO A DESIRED POSITION IN ARRAY LIST
    public static void addOneDataToDesiredPosition(int i, VideoModel model){
        allData.add(i, model);
    }
}
