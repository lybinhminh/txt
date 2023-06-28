package io.unfish;

import java.io.File;
import java.util.*;

public class test {
    public static txt t;
    public static void main(String[] args){
        t = new txt(new File("config.txt"));
        /*
        t.open();
        System.out.println("map");
        for(Map.Entry<String,String>entry: t.map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
        System.out.println("map1");
        for(Map.Entry<String,Integer>entry: t.map1.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
        System.out.println("map2");
        for(Map.Entry<String,Integer>entry: t.map2.entrySet()){
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
        System.out.println("map3");
        for(Map.Entry<Integer,String>entry : t.map3.entrySet()){
            System.out.println(entry.getValue() + " " +entry.getKey());
        }
        t.set("server#note2#port","25565",null,true);
         */
        t.open();
        System.out.println(t.map.get("server#"));
    }
}
