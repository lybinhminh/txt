package io.unfish;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class txt {
    public File file;
    public Map<String,String>map = new HashMap<>(); // store data <path>: value [line, list]
    public Map<String,Integer>map1 = new HashMap<>(); // store start index of a list <path to list always end by #>: value
    public Map<String,Integer>map2 = new HashMap<>(); // opposite to the upper one, store end index of a list
    public Map<Integer,String>map0 = new HashMap<>(); // store original lines in txt file <line number ?>: line
    public Map<Integer,String>map3 = new HashMap<>(); //  store list of an element of list
    public List<Integer>comments  = new ArrayList<>();
    public boolean update = true;
    public txt(File file){
     this.file = file;
    }
    public void open(){
        try{
            Scanner scanner = new Scanner(file);
            String block[] = new String[20];
            int currentBlock = -1;
            List<String>list =new ArrayList<>();
            boolean LIST = false;
            for(int currentLine = 1;scanner.hasNextLine();++currentLine){
                String line = scanner.nextLine();
                map0.put(currentLine,line);
                boolean inString = false;
                boolean beforeColons = true;
                boolean error = false;
                String key = "";
                String value = "";
                boolean comment = false;
                for(int index = 0; index < line.length(); ++index){
                    char ch = line.toCharArray()[index];
                    if(ch == '#' && !comment && !inString){
                        comment = true;
                        continue;
                    }else if(ch == '#' && comment && !inString){
                        comment = false;
                        continue;
                    }else if(comment){
                        continue;
                    }
                    if(ch == '\"' && !inString){
                        inString = true;
                        continue;
                    }
                    if(ch == '\"' && inString){
                        inString = false;
                        continue;
                    }

                    if(inString && beforeColons){
                        System.out.println("Invalid syntax found in line "+currentLine+" [character sequence written before colons] (method: open(), io.unfish.txt)" +
                                "\nIgnore this line");
                        error = true;
                        break;
                    }
                    if(ch == ':' && !inString){beforeColons = false;continue;}
                    if(beforeColons){
                        key += ch;
                    }
                    if(!beforeColons){
                        value += ch;
                    }
                }
                if(error)continue;
                key = key.trim();
                value = value.trim();
                if(key.isBlank()){
                    comments.add(currentLine);
                    continue;
                }
                String path = "";
                for(int i = 0; i <= currentBlock; ++i){
                    path += block[i] + "#";
                }
                if(key.trim().equals("}")){
                    map2.put(path, currentLine);
                    if(list.size() > 0){
                        map.put(path,list.toString());
                        list.clear();
                    }

                    --currentBlock;
                    continue;
                }
                if(!value.isBlank()){
                    if(value.equals("{")){
                        block[++currentBlock] = key;
                        LIST = true;
                        map1.put(path + key + "#", currentLine);
                        continue;
                    }
                    map.put(path + key, value);
                    map1.put(path + key, currentLine);
                    continue;
                }
                if(LIST && !key.isBlank())list.add(key);
                map3.put(currentLine,key);
            }
            scanner.close();
        }catch(Exception e){
        }
    }
    public void set(String path,String newValue,List<String> newList,boolean createNew){
        if(map.get(path) != null){
                try{

                    if(map2.get(path) != null){
                        if(newList == null){
                            newList = new ArrayList<>();
                            if(newValue.contains(",")){
                                for(String temporary: newValue.split(",")){
                                    newList.add(temporary);
                                }
                            }else{
                                newList.add(newValue);
                            }

                        }
                        List<Integer>temporary = new ArrayList<>();
                        for(Map.Entry<Integer,String>entry: map3.entrySet()){
                            if(entry.getKey() > map1.get(path) && entry.getKey() < map2.get(path)){

                                temporary.add(entry.getKey());
                            }
                        }
                        List<String>ls = new ArrayList<>();
                        boolean enough = true;
                        if(temporary.size() < newList.size()){
                            enough = false;
                        }
                        int i = 0;
                        for(Map.Entry<Integer,String>entry: map0.entrySet()){
                            if(i < temporary.size())
                            if(entry.getKey() == temporary.get(i)){
                                if(i < newList.size())
                                ls.add(newList.get(i));
                                ++i;
                                continue;
                            }
                            if((i == temporary.size()) && !enough){
                                for(; i < newList.size(); ++i){
                                    ls.add(newList.get(i));
                                }
                            }
                            ls.add(entry.getValue());
                        }
                        PrintWriter out = new PrintWriter(file);
                        for(String line: ls){
                            out.println(line);
                            out.flush();
                        }
                        out.close();
                        return;
                    }
                    map0.replace(map1.get(path),map0.get(map1.get(path)).split(":")[0]+": "+newValue);
                    PrintWriter out = new PrintWriter(file);
                    for(Map.Entry<Integer,String>entry : map0.entrySet()){
                        out.println(entry.getValue());
                        out.flush();
                    }
                    out.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                return;
        }
        if(createNew){
            String[] temporary = path.split("#");
            List<String>ls = new ArrayList<>();
            /*
            if(temporary.length == 1){
                for(Map.Entry<Integer,String>entry : map0.entrySet()){
                    ls.add(entry.getValue());
                }

                if(!path.contains("#")){
                    ls.add(path+": "+newValue);
                }
                else{
                    if(newList == null){
                        newList=  new ArrayList<>();
                        if(newValue.contains(",")){
                            for(String temporary1 : newValue.split(",")){
                                newList.add(temporary1);
                            }
                        }else{
                            newList.add(newValue);
                        }
                    }
                    ls.add(path+": {");
                    for(String str: newList){
                        ls.add(str);
                    }
                    ls.add("}");
                }
                try{
                    PrintWriter out = new PrintWriter(file);
                    for(String l: ls){
                        out.println(l);
                        out.flush();
                    }
                    out.close();
                    return;
                }catch(Exception e){

                }
            }

            */
            String PATH = "";
            String lastElement = temporary[temporary.length - 1];

            String temporary2[] = new String[temporary.length-1];
            for(int i = 0; i < temporary.length - 1; ++i){
                temporary2[i] = temporary[i];
            }
            List<String>newBs = new ArrayList<>();
            int mode = 0;
            if(path.toCharArray()[path.length()-1] == '#')mode++;
            for(String temporary1: temporary2){
                if(map1.get(PATH+temporary1+"#") != null ){
                    PATH += temporary1 + "#";
                    continue;
                }
                newBs.add(temporary1);
            }
            int target = 0;
            if(!PATH.isBlank())
            target = map1.get(PATH);
            if(newList == null && newValue == null){
                System.out.println("Invalid value input [both character sequence input and list input are blank!] (in method:set() io.unfish.txt)" +
                        "\nSuspend this method");
                return;
            }
            if(mode > 0 && newList == null){
                newList = new ArrayList<>();
                if(newValue.contains(",")){
                    for(String str: newValue.split(",")){
                        newList.add(str);
                    }
                }else{
                    newList.add(newValue);
                }
            }
            if(mode == 0 && newValue == null){
                for(String str: newList){
                    newValue += str;
                }
            }
            for(Map.Entry<Integer,String>entry : map0.entrySet()){
                ls.add(entry.getValue());
                if(entry.getKey() == target){
                    int i = 0;
                    for(String unB: newBs){
                        ls.add(unB+": {");
                        ++i;
                    }
                    if(mode > 0){
                        ls.add(lastElement+": {");
                        ++i;
                        for(String l: newList){
                            ls.add(l);
                        }
                    }
                    if(mode == 0){ls.add(lastElement+": "+newValue);}
                        for(int i2 = 0; i2 < i; ++i2){
                            ls.add("}");
                        }
                }

            }
            try{
                PrintWriter out = new PrintWriter(file);
                for(String line: ls){
                    out.println(line);
                    out.flush();
                }
                out.close();
            }catch(Exception e){

            }
            return;

        }
    }
    public void update(long period){
        Thread t = new Thread(){
            long prevT = System.currentTimeMillis();
            public void run(){
                while(update){
                    if((System.currentTimeMillis() - prevT)/1000 >= period){
                        prevT = System.currentTimeMillis();
                        open();
                    }
            }
        };

        };
        t.start();
    }
    public String getAnyList(int startIndex,int endIndex,int take, boolean includeComment){
        if(startIndex < 1){
            System.out.println("Invalid startIndex input in method getAnyList(int,int,int,boolean) in io.unfish.txt [startIndex < 1]");
            return null;
        }else if(startIndex > endIndex){
            int temp = startIndex;
            startIndex = endIndex;
            endIndex=  temp;
        }else if(endIndex - startIndex == 0){
            System.out.println("Invalid startIndex and endIndex input in method getAnyList(int,int,int.boolean) in io.unfish.txt[startIndex = endIndex]");
            return null;
        }
        /**
         * put all line from startIndex + 1 to endIndex - 1 into a list then make it turned into a char sequence
         * take = 1 : only take line with blank value ( element of a list); take = 2 : only take <key>: value; take = other numbers : take all
         */
        List<String> result = new ArrayList<>();
        for(int index = startIndex + 1; index > endIndex; ++index){
            if(comments.contains(index) &&  !includeComment)continue;
            else if(comments.contains(index)){
                result.add(map0.get(index));
                continue;
            }
            if(take == 1 && map0.get(index).split(":").length > 1)continue;
            else if(take == 1){
                result.add(map0.get(index));
                continue;
            }
            if(take == 2 && map0.get(index).split(":").length < 2)continue;
            else if(take == 2){
                result.add(map0.get(index));
                continue;
            }
            result.add(map0.get(index));
        }
        return result.toString();
    }
}
