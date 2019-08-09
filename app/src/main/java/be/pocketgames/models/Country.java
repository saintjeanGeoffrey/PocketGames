package be.pocketgames.models;

import java.util.Arrays;
import java.util.List;

public enum Country {
    //Objets directement construits

    BELGIUM ("Belgium"),

    FRANCE ("France"),

    GERMANY ("Germany"),

    SPAIN ("Spain");


    private String name = "";

    //Constructeur

    Country(String name){
        this.name = name;
    }


    public String toString(){
        return name;
    }

    public static List<String> getEnumNames(Class<? extends Enum<?>> e) {
        return Arrays.asList(Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", "));
    }

    public String getName() {
        return name;
    }
}
