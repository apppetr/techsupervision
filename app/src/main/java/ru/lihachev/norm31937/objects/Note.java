package ru.lihachev.norm31937.objects;

import android.support.v7.widget.AppCompatSpinner;

public class Note {

    private String name;
    private String area;
    private String count;
    private String depth;
    private String length;
    private String width;
    private String Quality;
    private String sDepth;
    private String sLength;
    private String sArea;
    private String sWidth;
    private String sCount;


    public Note(String name) {
        this.name = name;
        this.area = "";
        this.count = "";
        this.depth = "";
        this.length = "";
        this.width = "";
        this.Quality = "";
        this.sDepth = "";
        this.sLength = "";
        this.sArea = "";
        this.sWidth = "";
        this.sCount = "";
    }

    public Note() {
        this.name = "";
        this.area = "";
        this.count = "";
        this.depth = "";
        this.length = "";
        this.width = "";
        this.Quality = "";
        this.sDepth = "";
        this.sLength = "";
        this.sArea = "";
        this.sWidth = "";
        this.sCount = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getQuality() {
        return Quality;
    }

    public void setQuality(String quality) {
        Quality = quality;
    }

    public String getsDepth() {
        return sDepth;
    }

    public void setsDepth(String sDepth) {
        this.sDepth = sDepth;
    }

    public String getsLength() {
        return sLength;
    }

    public void setsLength(String sLength) {
        this.sLength = sLength;
    }

    public String getsArea() {
        return sArea;
    }

    public void setsArea(String sArea) {
        this.sArea = sArea;
    }

    public String getsWidth() {
        return sWidth;
    }

    public void setsWidth(String sWidth) {
        this.sWidth = sWidth;
    }

    public String getsCount() {
        return sCount;
    }

    public void setsCount(String sCount) {
        this.sCount = sCount;
    }
}