package com.prajwal.textrecognision;

public class searchHistory {
    String username;
    String data;

    searchHistory(){
        username = "";
        data = "";
    }
    searchHistory(String name){
        username = name;
        data = "";
    }

    public void storeData(String s){
        if (!data.isEmpty())
            data = data +", "+s;
        else
            data = s;
    }
    public String getData(){
        if (!data.isEmpty())
            return data;
        return "";
    }
}
