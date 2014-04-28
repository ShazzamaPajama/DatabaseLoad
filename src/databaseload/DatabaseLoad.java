/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package databaseload;

import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Shazzama.Pajama
 */
public class DatabaseLoad {
    
    public static void main(String[] args){
        DBManager test = new DBManager();
        Random Randint = new Random();
        
        for (int i=0; i<200; i++){
            System.out.println("Adding character " + (i+1) + " of 200");
            //Basic Info
            String Name;
            String Race;
            String Type;
            String Class;
            String Align;
            Integer Level;
            Integer HP;
            Integer AC;
            Integer ATK;
            String Desc;
            String Extra;
            int decider;
            
            //Abilities
            Integer STR;
            Integer CON;
            Integer DEX;
            Integer INT;
            Integer WIS;
            Integer CHA;
            
            //Abilities
            Integer Perception;
            Integer Acrobatics;
            
            
            //SetBasic Info
            Name = "TestCharacter " +i;
            
            //set Race
            decider = Randint.nextInt(3);
            if(decider == 0){
                Race = "Human";
            }else if (decider == 1){
                Race = "Orc";
            }else{
                Race = "Dwarf";
            }
            
            //set Type
            decider = Randint.nextInt(3);
            if (decider == 0){
                Type = "Player";
            }else if (decider == 1){
                Type = "NPC";
            }else{
                Type = "Monster";
            }
            
            //set Class
            if (Type.equals("Monster")){
                Class = "Monster";
            }else{
                decider = Randint.nextInt(3);
                if (decider == 0){
                    Class = "Paladin";
                }else if (decider == 1){
                    Class = "Rogue";
                }else{
                    Class = "Wizard";
                }
            }
            
            //set Align
            decider = Randint.nextInt(2);
            if (decider == 0){
                Align = "Neutral Good";
            }else{
                Align = "Neutral Evil";
            }
            
            //set Level
            Level = Randint.nextInt(10) +1;
            
            //set HP
            HP = (Randint.nextInt(Level) +1) * Level;
            
            //set AC
            AC = Randint.nextInt(6) + 14;
            
            ATK = Randint.nextInt(Level * 2);
            
            Desc = "Description " + i;
            Extra = "Extra Abilities " + i;
            
            //Set Abilities
            STR = Randint.nextInt(9) +10;
            CON = Randint.nextInt(9) +10;
            DEX = Randint.nextInt(9) +10;
            INT = Randint.nextInt(9) +10;
            WIS = Randint.nextInt(9) +10;
            CHA = Randint.nextInt(9) +10;
            
            //Set Skills
            Perception = Randint.nextInt(20) + 1;
            Acrobatics = Randint.nextInt(20) + 1;
            
            try {
                test.addBasicInfo(Name, Race, Type, Class, Align, Level, HP, AC, ATK, Desc, Extra);
                test.addAbilitySet(Name, Race, Type, STR, CON, DEX, INT, WIS, CHA);
                test.addSkillSet(Name, Race, Type);
                test.updateSkill(Name, Race, Type, "Acrobatics", Acrobatics);
                test.updateSkill(Name, Race, Type, "Perception", Perception);
            } catch (SQLException ex) {
                System.out.println("error SQL: " + ex.getSQLState());
                System.out.println("error Message: " + ex.getMessage());
            }
        }
        
        System.out.println("Complete");
    }  
       
}
    
    

