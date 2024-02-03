package com.example.pointhelper.ui.theme;

import java.io.Serializable;
import java.util.Map;

public class MyMap implements Serializable {
    private Map<Integer,String> mmap;

    public Map<Integer, String> getMmap() {
        return mmap;
    }

    public void setMmap(Map<Integer, String> mmap) {
        this.mmap = mmap;
    }
}