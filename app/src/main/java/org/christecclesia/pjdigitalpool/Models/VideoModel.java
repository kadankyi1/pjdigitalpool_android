package org.christecclesia.pjdigitalpool.Models;

public class VideoModel {
    private static final long serialVersionUID = 1L;
    private long rowId;
    private int video_id;
    private String video_name;
    private String video_description;
    private String video_image;
    private String video_mp4;
    private String created_at;


    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public String getVideo_image() {
        return video_image;
    }

    public void setVideo_image(String video_image) {
        this.video_image = video_image;
    }

    public String getVideo_mp4() {
        return video_mp4;
    }

    public void setVideo_mp4(String video_mp4) {
        this.video_mp4 = video_mp4;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
