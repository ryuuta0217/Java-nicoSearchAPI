package com.ryuuta0217.niconicoSearchAPI;

import java.util.List;

public class Sample {
    public static void main(String[] args) {
        nicoSearchAPI ns = new nicoSearchAPI(true, 5);

        long start = System.currentTimeMillis();
        List<nicoVideoSearchResult> results_0 = ns.searchVideo("初音ミク", 5);
        long end = System.currentTimeMillis();

        long start_2 = System.currentTimeMillis();
        List<nicoVideoSearchResult> results_1 = ns.searchVideo("千本桜", 5);
        long end_2 = System.currentTimeMillis();

        System.out.println("First: " + (end-start));
        results_0.forEach(result -> System.out.println(result.getTitle() + ": " + result.getWatchUrl()));

        System.out.println("Second: " + (end_2-start_2));
        results_1.forEach(result -> System.out.println(result.getTitle() + ": " + result.getWatchUrl()));
    }
}
