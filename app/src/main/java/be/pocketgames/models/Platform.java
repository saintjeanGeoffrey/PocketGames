package be.pocketgames.models;

import java.util.Arrays;
import java.util.List;

public enum Platform {
    //Objets directement construits

    PLAYSTATION1("playstation 1", "ps1"),
    PLAYSTATION2("playstation 2", "ps2"),
    PLAYSTATION3("playstation 3", "ps3"),
    PLAYSTATION4("playstation 4", "ps4");


    private String name = "";
    private String abrev = "";

    //Constructeur
    Platform(String name, String abrev){
        this.name = name;
        this.abrev = abrev;
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

    public String getAbrev() {
        return abrev;
    }
}
