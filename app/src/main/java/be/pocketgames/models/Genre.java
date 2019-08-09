package be.pocketgames.models;

import java.util.Arrays;
import java.util.List;

public enum Genre {
    //Objets directement construits

    ACTION("Action"),
    AVENTURE("Aventure"),
    RPG("Rpg"),
    STRATEGIE("Stratégie"),
    REFLEXION("Réflexion"),
    SHOOTER("Shooter"),
    SPORT("Sport"),
    AUTRE("Autre"),
    PLATE_FORME("Plate-forme"),
    COURSE("Course"),
    SIMULATION("Simulation"),
    GESTION("Gestion"),
    PUUZLE_GAME("Puzzle-game"),
    FPS("Fps"),
    COMBAT("Combat"),
    SHOOT_EM_UP("Shoot'em up"),
    MMO("Mmo"),
    ADRESSE("Adresse"),
    BEAT_EM_ALL("Beat'em all"),
    POINT_N_CLICK("Point'n click"),
    RYTHME("Rythme"),
    PARTY_GAME("Party-game"),
    TIR("Tir"),
    TACTIQUE("Tactique"),
    SURIVAL_HORROR("Survival-horror");

    private String name = "";

    //Constructeur
    Genre(String name){
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
