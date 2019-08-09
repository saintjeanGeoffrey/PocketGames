package be.pocketgames.models;

import java.util.Arrays;
import java.util.List;

public enum Editor {


    ACTIVISION ("Activision"),
    CAPCOM ("Capcom"),
    UBISOFT ("Ubisof");


    private String name = "";

    //Constructeur

    Editor(String name){
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
