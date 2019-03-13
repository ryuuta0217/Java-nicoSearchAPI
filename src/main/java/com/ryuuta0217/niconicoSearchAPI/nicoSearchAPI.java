package com.ryuuta0217.niconicoSearchAPI;

import org.json.JSONObject;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.*;

public class nicoSearchAPI {
    private boolean cacheEnabled;
    private int maxCacheSize;
    private LinkedHashMap<String, nicoVideoSearchResult> videoResultCache;

    public nicoSearchAPI(boolean cacheEnabled, int maxCacheSize) {
        this.cacheEnabled = cacheEnabled;
        this.maxCacheSize = maxCacheSize;
    }

    public LinkedList<nicoVideoSearchResult> searchVideo(String query, int resultLimit) {
        return searchVideo(query, resultLimit, true);
    }


    public LinkedList<nicoVideoSearchResult> searchVideo(String query, int resultLimit, boolean autogetVideoInfo) {
        if(resultLimit <= 0) resultLimit = 10;
        if(cacheEnabled && videoResultCache == null) videoResultCache = new LinkedHashMap<>();

        HTTPUtil hu = new HTTPUtil();
        hu.setTargetAddress("http://api.search.nicovideo.jp/api/v2/video/contents/search");
        hu.setMethod("GET");
        Map<String, String> queryMap = new HashMap<>();
        try {
            queryMap.put("q", URLEncoder.encode(query, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {}
        queryMap.put("_sort", "-viewCounter");
        queryMap.put("targets", "title");
        queryMap.put("fields", "contentId,title,description,tags,categoryTags,viewCounter,mylistCounter,commentCounter,startTime,lastCommentTime,lengthSeconds,thumbnailUrl");
        queryMap.put("_limit", String.valueOf(resultLimit));
        hu.setQueryMap(queryMap);

        LinkedList<nicoVideoSearchResult> results = new LinkedList<>();
        JSONObject object = new JSONObject(hu.request());

        System.out.println(object.toString(4));

        object.getJSONArray("data").forEach(resultObject -> {
            JSONObject result = (JSONObject) resultObject;

            if(videoResultCache.containsKey(result.getString("contentId"))) {
                System.out.println("[DEBUG] Using cache for VideoID " + result.getString("contentId"));
                results.add(videoResultCache.get(result.getString("contentId")));
            } else {
                nicoVideoSearchResult rs = new nicoVideoSearchResult(
                        result.getString("contentId"),
                        result.getString("title"),
                        result.getString("description"),
                        result.getString("tags").split(" "),
                        result.getString("categoryTags").split(" "),
                        result.getInt("viewCounter"),
                        result.getInt("mylistCounter"),
                        result.getInt("commentCounter"),
                        result.getString("startTime"),
                        result.getString("thumbnailUrl"),
                        autogetVideoInfo);
                results.add(rs);
                videoResultCache.put(rs.getContentId(), rs);
            }

            if(maxCacheSize >= 1 && videoResultCache.size() > maxCacheSize) {
                videoResultCache.remove(videoResultCache.keySet().toArray()[0]);
            }
        });
        return results;
    }
}

class HTTPUtil {
    private String method;
    private String targetAddress;
    private String requestData;
    private Map<String, String> query;
    private Map<String, String> headers;

    private HttpURLConnection connection;
    private URL url;

    public HTTPUtil(@Nonnull String method, @Nonnull String targetAddress, Map<String, String> query, Map<String, String> headers) {
        this.method = method;
        this.targetAddress = targetAddress;
        this.query = query;
        this.headers = headers;
    }

    public HTTPUtil(@Nonnull String method, @Nonnull String targetAddress) {
        this.method = method;
        this.targetAddress = targetAddress;
    }

    public HTTPUtil() {}

    public HTTPUtil setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HTTPUtil setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
        return this;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public HTTPUtil setQueryMap(Map<String, String> query) {
        this.query = query;
        return this;
    }

    public HTTPUtil addQuery(String key, Object value) {
        if(query == null) this.query = new HashMap<>();
        query.put(key, value.toString());
        return this;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public HTTPUtil setHeaderMap(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HTTPUtil addHeader(String key, Object value) {
        if(headers == null) this.headers = new HashMap<>();
        headers.put(key, value.toString());
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HTTPUtil setRequestData(String data) {
        this.requestData = data;
        return this;
    }

    public String getRequestData() {
        return requestData;
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    public URL getUrl() {
        return url;
    }

    public String request() {
        if(method == null  || method.isEmpty()) throw new NullPointerException("メソッドが設定されていません。");
        if(targetAddress == null || targetAddress.isEmpty()) throw new NullPointerException("URLが設定されていません。");

        String params = "";

        try {
            url = new URL(targetAddress);
            if (query != null) {
                StringBuilder sb = new StringBuilder();
                query.forEach((key, val) -> {
                    if (sb.length() == 0) sb.append(key).append("=").append(val);
                    else sb.append("&").append(key).append("=").append(val);
                });
                params = sb.toString().replaceFirst("&$", "");
            }

            if (query != null && method.equalsIgnoreCase("GET")) {
                url = new URL(targetAddress + (params.isEmpty() ? "" : "?" + params));
            }

            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(method);
            if (headers != null) headers.forEach(connection::addRequestProperty);

            if ((query != null && requestData == null) && method.equalsIgnoreCase("POST")) {
                PrintWriter pw = new PrintWriter(connection.getOutputStream());
                pw.print(params);
                pw.close();
            }

            if((query == null && requestData != null) && method.equalsIgnoreCase("POST")) {
                PrintWriter pw = new PrintWriter(connection.getOutputStream());
                pw.print(requestData);
                pw.close();
            }

            connection.connect();

            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                InputStream response = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(response, (connection.getContentEncoding() == null ? "UTF-8" : connection.getContentEncoding()));
                BufferedReader br = new BufferedReader(isr);

                StringBuilder sb = new StringBuilder();
                br.lines().forEach(sb::append);
                isr.close();
                br.close();
                response.close();
                connection.disconnect();
                return sb.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                if (connection.getInputStream() != null) {
                    InputStream response = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(response, (connection.getContentEncoding() == null ? "UTF-8" : connection.getContentEncoding()));
                    BufferedReader br = new BufferedReader(isr);

                    br.lines().forEach(sb::append);
                    isr.close();
                    br.close();
                    response.close();
                }

                System.out.println("エラー: " + responseCode + "\n" + sb.toString());
            }
        } catch (MalformedURLException e) {
            throw new NullPointerException("URLが不正です: " + e.getLocalizedMessage());
        } catch (ProtocolException e) {
            throw new NullPointerException("メソッド名が不正です: " + e.getLocalizedMessage());
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            try {
                if (connection.getInputStream() != null) {
                    InputStream response = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(response, (connection.getContentEncoding() == null ? "UTF-8" : connection.getContentEncoding()));
                    BufferedReader br = new BufferedReader(isr);

                    br.lines().forEach(sb::append);
                    isr.close();
                    br.close();
                    response.close();
                }
            } catch(Exception ignored) {}

            System.out.println("エラーが発生しました: " + e.getLocalizedMessage() + "\n" + sb.toString());
        } finally {
            if (connection != null) connection.disconnect();
        }

        return null;
    }
}

