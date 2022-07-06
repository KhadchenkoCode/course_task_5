package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class CorpusBase{

    static final char[] punctuation = "([*^\\|«\\[\\]»/()$.—,!?:;'\\\"-]|\\s)+".toCharArray();

    public HashMap<String, Integer> occurences_map;
    public HashMap<String, Integer> occurences_map_punct;
    ArrayList<HashMap<String, Integer>> word_groups_maps;
    ArrayList<Sentence> sentences;

    int key_sum;
    String[] split_string(String start) {
        //потом удалим знаки препинания

        return start.split("([*^\\|«\\[\\]»/()$.—,!?:;'\\\"-]|\\s)+");
        // return  start.split( " ");
    }
    void count_cases(String arg){// additive

        String[] words_in_line = split_string(arg);

        for(int i = 0; i<words_in_line.length; i++){
            String key_char_str = words_in_line[i];
            //    key_char_str = key_char_str.toLowerCase();
            boolean contains_key_arg  = occurences_map.containsKey(key_char_str);
            if(contains_key_arg){

                int old_value = occurences_map.get(key_char_str);
                occurences_map.put(key_char_str, old_value+1);
                key_sum++;

            }else{
                key_sum++;
                occurences_map.put(key_char_str, 1);
            }
        }
    }

    // arg = sentence from sentences
    void count_cases_v2(String arg, boolean first_run){// additive

        int max_group_length = 5;


        String[] words_in_line = splitter(arg);

        if(first_run){
            Sentence sentence = new Sentence();
            sentence.rawWords = new ArrayList<String>(words_in_line.length);
            sentence.terms = new ArrayList<Boolean>(sentence.rawWords.size());

            for (int i = 0; i <words_in_line.length ; i++) {
                sentence.rawWords.add(i, words_in_line[i]);
            }
            sentences.add(sentence);
            sentence.orderInText = sentences.size();
        }



        for(int i = 0; i<words_in_line.length; i++){

            String word = words_in_line[i];

            boolean where_add = !isPunctSign(word);

            map_add_or_put(word, occurences_map_punct, first_run);
            if(where_add){
                map_add_or_put(word, occurences_map, first_run);
            }


            String[] words_group = new String[max_group_length];
            if(i<words_in_line.length-5){
                for(int j = 0; j<max_group_length; j++){
                    words_group[j] = words_in_line[i+j];
                }
                String group_add = "";
                if(!isPunctSign(words_group[0])){
                    group_add += words_group[0];

                }
                if(!isPunctSign(words_group[1])){
                    group_add +=" "+words_group[1];
                }
                else{
                    group_add+= words_group[1];
                }

                for(int j = 2; j<max_group_length; j++){
                    int index = j-2;
                    if(isPunctSign(words_group[j])){
                        group_add+=words_group[j];

                    }
                    else {
                        group_add+=" "+words_group[j];
                    }
                    HashMap<String, Integer> map_for_groups_tmp = this.word_groups_maps.get(index);
                    if(map_for_groups_tmp == null){
                        int bruh = 0;
                    }
                    map_add_or_put(group_add, map_for_groups_tmp, first_run);
                }

            }


        }

    }




    void map_add_or_put(String key_char_str, HashMap<String, Integer> arg, Boolean first_run){
        if(arg == null){
            return;
        }
        boolean contains_key_arg  = arg.containsKey(key_char_str);
        if(contains_key_arg){
            int old_value = arg.get(key_char_str);
            arg.put(key_char_str, old_value+1);
            key_sum++;

        }else{
            if(first_run){
            key_sum++;
            arg.put(key_char_str, 1);
            }
        }


    }


    CorpusBase(){
        this.word_groups_maps = new ArrayList<HashMap<String, Integer>>();
        this.occurences_map= new HashMap<String, Integer>();
        this.occurences_map_punct= new HashMap<String, Integer>();
        this.sentences = new ArrayList<Sentence>();
        key_sum = 0;
        for(int i = 2; i<5; i++){
            word_groups_maps.add(new HashMap<String, Integer>());
        }
    }
    private static String[] splitter(String str) {
        List<String> arrayList = new ArrayList<>();  // New ArrayList
        Set<Character> allowedChars = "([*^\\|«\\[\\]»/()$.—,!?:;'\\\"-]|\\s)+".chars()
                .mapToObj(c -> (char)c)
                .collect(Collectors.toSet());
        StringBuilder word = new StringBuilder();
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (allowedChars.contains(ch[i])) {
                arrayList.add(word.toString());
                word.setLength(0);
                arrayList.add(word.append(ch[i]).toString());
                word.setLength(0);
            }else if( ch[i]==' '){
                if(i!=0){
                    if(!allowedChars.contains(ch[i-1])){
                        arrayList.add(word.toString());
                        word.setLength(0);
                    }
                }
            } else if( i==ch.length-1){
                word.append(ch[i]);
                arrayList.add(word.toString());
            }else{
                word.append(ch[i]);
            }
        }
        String[] strings = new String[arrayList.size()];
        return arrayList.toArray(strings);
    }
     public static boolean isPunctSign(String str){ // должны передаваться слова из метода splitter, получается по времени O(1)
        boolean ret = false;
        if(str.length()==0){
            return  false;
        }
        for (char c: punctuation) {
            if(str.toCharArray()[0] == c){
                return  true;
            }
        }
        return ret;
    }



}
