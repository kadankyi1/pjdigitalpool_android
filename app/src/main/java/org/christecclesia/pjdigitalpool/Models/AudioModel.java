package org.christecclesia.pjdigitalpool.Models;

public class AudioModel {
    private static final long serialVersionUID = 1L;
    private long rowId;
    private int audio_id;
    private String audio_name;
    private String audio_description;
    private String audio_image;
    private String audio_mp3;
    private String created_at;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public int getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

    public String getAudio_name() {
        return audio_name;
    }

    public void setAudio_name(String audio_name) {
        this.audio_name = audio_name;
    }

    public String getAudio_description() {
        return audio_description;
    }

    public void setAudio_description(String audio_description) {
        this.audio_description = audio_description;
    }

    public String getAudio_image() {
        return audio_image;
    }

    public void setAudio_image(String audio_image) {
        this.audio_image = audio_image;
    }

    public String getAudio_mp3() {
        return audio_mp3;
    }

    public void setAudio_mp3(String audio_mp3) {
        this.audio_mp3 = audio_mp3;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
