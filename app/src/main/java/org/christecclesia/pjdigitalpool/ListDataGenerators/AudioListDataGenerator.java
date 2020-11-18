package org.christecclesia.pjdigitalpool.ListDataGenerators;

import org.christecclesia.pjdigitalpool.Models.AudioModel;

import java.util.ArrayList;
import java.util.List;

public class AudioListDataGenerator {

    // DECLARING THE DATA ARRAY LIST
    static List<AudioModel> allData = new ArrayList<>();

    // SETTING/RESETTING ALL SUGGESTED LINKUPS DATA
    public static void setAllDatasAfresh(List<AudioModel> newAllData) {
        AudioListDataGenerator.allData = newAllData;
    }

    // ADDING ONE DATA TO ARRAY LIST
    public static boolean addOneData(AudioModel model) {
        return allData.add(model);
    }

    // GETTING ALL DATA AS ARRAY LIST
    public static List<AudioModel> getAllData() {
        return allData;
    }

    // ADDING ONE DATA TO A DESIRED POSITION IN ARRAY LIST
    public static void addOneDataToDesiredPosition(int i, AudioModel model){
        allData.add(i, model);
    }
}
