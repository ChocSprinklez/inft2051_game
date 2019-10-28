package uon.inft2051.lab04;

import Weapons.SuperWeapon;
import java.util.Random;

public class Player {

    public double dodgeChance = 0.5;
    public double blockChance = 0.5;

    public int numStrengthPotions;
    public int numHealthPotions;
    public int numEvasionPotions;
    public SuperWeapon currentWeapon;

    public int attack(){
        Random RAND = new Random();
        return RAND.nextInt(currentWeapon.maxDamage) + 1;
    }

    /*public int defend(Enemy enemy){
        int incomingAttack = enemy.attack();
        Random RAND = new Random();
        int random = RAND.nextInt(100);

        if (random < (blockChance*100)){
            hitPoints -= incomingAttack;
        }

        if (random < (dodgeChance*100)){
            hitPoints -= incomingAttack;
        }

        return hitPoints;
    }*/

    public void addHealthPotion(){
        if (numHealthPotions < 5){
            numHealthPotions++;
        }
    }

    public void addEvasionPotion(){
        if (numEvasionPotions < 5){
            numEvasionPotions++;
        }
    }

    public void addStrengthPotion(){
        if (numStrengthPotions < 5){
            numStrengthPotions++;
        }
    }
}
