package be.pocketgames.models;

import java.util.Arrays;
import java.util.List;

public enum Game_component_state {


    NO("No"),
    NEW("New"),
    VERY_GOOD("Very good"),
    GOOD("Good"),
    BAD("Bad"),
    VERY_BAD("Very bad"),
    BROKEN("Broken");



    private String name ;

    //Constructeur
    Game_component_state(String name){
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
