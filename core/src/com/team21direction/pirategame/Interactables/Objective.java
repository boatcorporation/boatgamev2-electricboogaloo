package com.team21direction.pirategame.Interactables;

import com.team21direction.pirategame.actors.College;
import com.team21direction.pirategame.actors.Ship;
import com.team21direction.pirategame.screens.MainScreen;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Objective {
    private String display;
    public enum Type {ObtainXP, ObtainGold, HaveShips, UsePowerup, Destroy}
    private Type type;
    private int goalNum = -1;
    private String toDestroy = "";
    private boolean active;
    private int startGold;
    private int startXP;
    private int startShips;
    private boolean finalObjective;

    public Objective(College[] colleges, boolean finalObjective) {
        this.active = true;
        this.finalObjective = finalObjective;
        this.type = (this.finalObjective) ? Type.Destroy : Type.values()[new Random().nextInt(Type.values().length - 1)];

        switch(this.getType()) {
            case ObtainXP:
                do {
                    goalNum = new Random().nextInt(11);
                } while(goalNum == 0);
                display = "Obtain " + goalNum + " more XP";
                break;
            case ObtainGold:
                do {
                    goalNum = new Random().nextInt(31);
                } while(goalNum < 10);
                display = "Obtain " + goalNum + " more gold";
                break;
            case HaveShips:
                do {
                    goalNum = new Random().nextInt(6);
                } while(goalNum == 0);
                display = "Have " + goalNum + " more ships allied with you";
                break;
            case UsePowerup:
                toDestroy = Powerup.Type.values()[new Random().nextInt(Powerup.Type.values().length)].toString();
                display = "Use a " + toDestroy + " powerup";
                break;
            case Destroy:
                toDestroy = colleges[new Random().nextInt(colleges.length)].getCollegeName();
                display = "Destroy " + toDestroy + " College (Invulnerable)";
                break;
        }

    }

    public void startTracking(Ship player, ArrayList<Ship> ships, MainScreen screen) {
        startGold = player.getGold();
        startXP = screen.experience;
        for (Ship ship : ships) {
            if (Objects.equals(player.getParentCollegeName(), ship.getParentCollegeName())) {
                startShips++;
            }
        }
    }

    public Type getType() { return this.type; }

    public void setType(String type) {
        switch(type) {
            case "ObtainXP":
                this.type = Type.ObtainXP;
                break;
            case "ObtainGold":
                this.type = Type.ObtainGold;
                break;
            case "HaveShips":
                this.type = Type.HaveShips;
                break;
            case "UsePowerup":
                this.type = Type.UsePowerup;
                break;
            case "Destroy":
                this.type = Type.Destroy;
                break;
        }
    }


    public String getToDestroy() { return toDestroy; }

    public void setToDestroy(String toDestroy) { this.toDestroy = toDestroy; }

    public String getDisplay() { return this.display; }

    public void setDisplay(String display) { this.display = display; }

    public int getGoalNum() { return goalNum; }

    public void setGoalNum(int goalNum) { this.goalNum = goalNum; }

    public int getStartGold() { return startGold; }

    public void setStartGold(int startGold) { this.startGold = startGold; }

    public int getStartShips() { return startShips; }

    public void setStartShips(int startShips) {this.startShips = startShips;}

    public int getStartXP() { return startXP; }

    public void setStartXP(int startXP) { this.startXP = startXP; }

    public boolean isFinalObjective() { return finalObjective; }

    public void setFinalObjective(boolean finalObjective) { this.finalObjective = finalObjective; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public void checkActive(Ship player, ArrayList<Ship> ships, College[] colleges, MainScreen screen) {
        switch(this.getType()) {
            case ObtainXP:
                active = goalNum >= (screen.experience - startXP);
                break;
            case ObtainGold:
                active = goalNum >= (player.getGold() - startGold);
                break;
            case HaveShips:
                int count = 0;
                for (Ship ship : ships) {
                    if (Objects.equals(player.getParentCollegeName(), ship.getParentCollegeName())) {
                        count++;
                    }
                }
                active = goalNum >= (count - startShips);
                break;
            case UsePowerup:
                active = !(Objects.equals(player.getCurrentPow(), toDestroy));
                break;
            case Destroy:
                for (College college : colleges) {
                    if (Objects.equals(toDestroy, college.getCollegeName()) && !(college.isActive())) {
                        active = false;
                        break;
                    }
                }
                break;
        }
    }
}
