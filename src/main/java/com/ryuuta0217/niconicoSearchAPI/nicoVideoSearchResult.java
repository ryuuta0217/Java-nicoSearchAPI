package com.ryuuta0217.niconicoSearchAPI;

import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class nicoVideoSearchResult {
    private String contentId;
    private String title;
    private String description;
    private String watchUrl;
    private String[] tags;
    private String[] categoryTags;
    private int viewCount;
    private int mylistCount;
    private int commentCount;
    private String startTime;
    private String thumbnailUrl;
    private nicoVideoInfo videoInfo;

    public nicoVideoSearchResult(String contentId, String title, String description, String[] tags, String[] categoryTags, int viewCount, int mylistCount, int commentCount, String startTime, String thumbnailUrl, boolean getVideoInfo) {
        this.contentId = contentId;
        this.title = title;
        this.description = description;
        this.watchUrl = "https://www.nicovideo.jp/watch/" + this.contentId;
        this.tags = tags;
        this.categoryTags = categoryTags;
        this.viewCount = viewCount;
        this.mylistCount = mylistCount;
        this.commentCount = commentCount;
        this.startTime = startTime;
        this.thumbnailUrl = thumbnailUrl;
        if(getVideoInfo) videoInfo = getInfo();
    }

    public nicoVideoSearchResult(String contentId, String title, String description, String[] tags, String[] categoryTags, int viewCount, int mylistCount, int commentCount, String startTime, String thumbnailUrl) {
        this.contentId = contentId;
        this.title = title;
        this.description = description;
        this.watchUrl = "https://www.nicovideo.jp/watch/" + this.contentId;
        this.tags = tags;
        this.categoryTags = categoryTags;
        this.viewCount = viewCount;
        this.mylistCount = mylistCount;
        this.commentCount = commentCount;
        this.startTime = startTime;
        this.thumbnailUrl = thumbnailUrl;
    }

    public nicoVideoSearchResult(boolean getVideoInfo) {
        if(getVideoInfo) videoInfo = getInfo();
    }

    public nicoVideoSearchResult() {

    }

    //set
    public nicoVideoSearchResult setContentId(String contentId) {
        this.contentId = contentId;
        this.watchUrl = "https://www.nicovideo.jp/watch/" + this.contentId;
        return this;
    }

    public nicoVideoSearchResult setTitle(String title) {
        this.title = title;
        return this;
    }

    public nicoVideoSearchResult setDescription(String description) {
        this.description = description;
        return this;
    }

    public nicoVideoSearchResult setWatchUrl(String watchUrl) {
        this.watchUrl = watchUrl;
        return this;
    }

    public nicoVideoSearchResult setTags(String[] tags) {
        this.tags = tags;
        return this;
    }

    public nicoVideoSearchResult setCategoryTags(String[] categoryTags) {
        this.categoryTags = categoryTags;
        return this;
    }

    public nicoVideoSearchResult setViewCount(int viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    public nicoVideoSearchResult setMylistCount(int mylistCount) {
        this.mylistCount = mylistCount;
        return this;
    }

    public nicoVideoSearchResult setCommentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public nicoVideoSearchResult setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public nicoVideoSearchResult setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public nicoVideoSearchResult setInfo(nicoVideoInfo videoInfo) {
        this.videoInfo = videoInfo;
        return this;
    }

    //get
    public String getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getWatchUrl() {
        return watchUrl;
    }

    public String[] getTags() {
        return tags;
    }

    public String[] getCategoryTags() {
        return categoryTags;
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

    public String getStartTime() {
        return startTime;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public nicoVideoInfo getInfo() {
        if(videoInfo == null) {
            HTTPUtil requester = new HTTPUtil("GET", "https://ext.nicovideo.jp/api/getthumbinfo/"+contentId);
            JSONObject object = XML.toJSONObject(requester.request());
            object = object.getJSONObject("nicovideo_thumb_response").getJSONObject("thumb");

            nicoVideoInfo nvi = new nicoVideoInfo();
            nvi.setCommentCount(object.getInt("comment_num"));
            nvi.setMylistCount(object.getInt("mylist_counter"));
            nvi.setLengthFormatted(object.getString("length"));
            nvi.setDescription(object.getString("description"));
            nvi.setTitle(object.getString("title"));
            nvi.setThumbnailUrl(object.getString("thumbnail_url"));

            List<String> tags = new ArrayList<>();
            object.getJSONObject("tags").getJSONArray("tag").forEach(obj -> {
                if(obj instanceof String) tags.add(obj.toString());
                else if (obj instanceof JSONObject) tags.add(((JSONObject) obj).getString("content"));
                else {
                    System.out.println("Err: " + obj.toString());
                    tags.add(obj.toString());
                }
            });

            nvi.setTags(tags.toArray(new String[]{}));
            nvi.setUploadUserIconUrl(object.getString("user_icon_url"));
            nvi.setWatchUrl(object.getString("watch_url"));
            nvi.setUploadUserId(object.getInt("user_id"));
            nvi.setUploadUserName(object.getString("user_nickname"));
            nvi.setViewCount(object.getInt("view_counter"));
            nvi.setVideoId(object.getString("video_id"));
            return nvi;
        } else return videoInfo;
    }

    public String toString() {
        return "contentId="+contentId+", title="+title+", description="+description+", watchUrl="+watchUrl+", tags="+Arrays.toString(tags)+", categoryTags="+Arrays.toString(categoryTags)+
                ", viewCount="+viewCount+", mylistCount="+mylistCount+", commentCount="+commentCount+", startTime="+startTime+", thumbnailUrl="+thumbnailUrl+", videoInfo=\""+getInfo().toString()+"\"";
    }
}
