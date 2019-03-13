package com.ryuuta0217.niconicoSearchAPI;

import java.util.Arrays;

public class nicoVideoInfo {
    //video info
    private String videoId;
    private String title;
    private String[] tags;
    private String watchUrl;
    private String thumbnailUrl;
    private String description;
    private String lengthFormatted;

    //counts
    private int viewCount;
    private int mylistCount;
    private int commentCount;

    //Uploader info
    private int uploadUserId;
    private String uploadUserName;
    private String uploadUserIconUrl;

    public nicoVideoInfo(String videoId, String title, String[] tags, String watchUrl, String thumbnailUrl, String description, String lengthFormatted,
                         int viewCount, int mylistCount, int commentCount,
                         int uploadUserId, String uploadUserName, String uploadUserIconUrl) {

        this.videoId = videoId;
        this.title = title;
        this.tags = tags;
        this.watchUrl = watchUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.lengthFormatted = lengthFormatted;

        this.viewCount = viewCount;
        this.mylistCount = mylistCount;
        this.commentCount = commentCount;

        this.uploadUserId = uploadUserId;
        this.uploadUserName = uploadUserName;
        this.uploadUserIconUrl = uploadUserIconUrl;
    }

    public nicoVideoInfo() {}

    public nicoVideoInfo setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }

    public nicoVideoInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public nicoVideoInfo setTags(String[] tags) {
        this.tags = tags;
        return this;
    }

    public nicoVideoInfo setWatchUrl(String watchUrl) {
        this.watchUrl = watchUrl;
        return this;
    }

    public nicoVideoInfo setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public nicoVideoInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public nicoVideoInfo setLengthFormatted(String lengthFormatted) {
        this.lengthFormatted = lengthFormatted;
        return this;
    }

    public nicoVideoInfo setViewCount(int viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    public nicoVideoInfo setMylistCount(int mylistCount) {
        this.mylistCount = mylistCount;
        return this;
    }

    public nicoVideoInfo setCommentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public nicoVideoInfo setUploadUserId(int uploadUserId) {
        this.uploadUserId = uploadUserId;
        return this;
    }

    public nicoVideoInfo setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
        return this;
    }

    public nicoVideoInfo setUploadUserIconUrl(String uploadUserIconUrl) {
        this.uploadUserIconUrl = uploadUserIconUrl;
        return this;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    public String[] getTags() {
        return tags;
    }

    public String getWatchUrl() {
        return watchUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getLengthFormatted() {
        return lengthFormatted;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getMylistCount() {
        return mylistCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getUploadUserId() {
        return uploadUserId;
    }

    public String getUploadUserName() {
        return uploadUserName;
    }

    public String getUploadUserIconUrl() {
        return uploadUserIconUrl;
    }

    public String toString() {
        return "videoId=" + videoId +
                ", title=" + title +
                ", tags=" + Arrays.toString(tags) +
                ", watchUrl=" + watchUrl +
                ", thumbnailUrl=" + thumbnailUrl +
                ", description=" + description +
                ", lengthFormatted=" + lengthFormatted +
                ", viewCount=" + viewCount +
                ", mylistCount=" + mylistCount +
                ", commentCount=" + commentCount +
                ", uploadUserId=" + uploadUserId +
                ", uploadUserName=" + uploadUserName +
                ", uploadUserIconUrl=" + uploadUserIconUrl;
    }
}
