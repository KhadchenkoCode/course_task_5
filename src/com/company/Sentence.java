package com.company;

import java.util.ArrayList;

public class Sentence{

    boolean containsTerms;
    boolean checked_previously;
    int orderInText;
    ArrayList<String> rawWords;
    ArrayList<Boolean> terms;
    String getString(){
        String ret = "";
        for (int i = 0; i < rawWords.size(); i++) {
            String str = rawWords.get(i);
            if (CorpusBase.isPunctSign(str)){
                ret+= str;
            }else {
                if(terms.get(i)){
                    str = "->"+str+"<-";
                }                ret+= " "+str;
            }
        }
        return  ret;
    }
    //takes table sorted by w3 as argument, that stores words from text
    boolean checkTerms(ArrayList<TableRow> sorted_table){

        if(containsTerms){
            return  true;
        } else if(!checked_previously){//выделил немножко памяти на 1 boolean чтобы не проверять каждый раз с нуля
            resizeIfNeeded(terms, rawWords.size());

            for(int i = 0; i<100&&i<sorted_table.size(); i++){
                String term_in_question = sorted_table.get(i).key;
                for (String rawWord: rawWords) {
                    if(rawWord==term_in_question){
                        containsTerms = true;
                        checked_previously = true;

                        int term_index = findWord(rawWord);
                        if(term_index>-1){
                        terms.add(term_index, true);
                        }
                        //return  true;
                    }
                }
            }
        }
            checked_previously=true;
        //containsTerms = false;
        return checked_previously;


    }
    Sentence(){
        containsTerms = false;
        checked_previously=false;

    }

     void resizeIfNeeded(ArrayList<Boolean> arr, int newsize){
        int original_size = arr.size();
        for(int i = 0; i<newsize- original_size; i++){
            arr.add(false);
        }

    }
    int findWord(String word){

        for(int i = 0; i<rawWords.size(); i++){
            if (rawWords.get(i) == word){
                return  i;
            }
        }
        return -1;
    }

}
