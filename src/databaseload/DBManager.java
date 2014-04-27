/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package databaseload;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.SQLiteConfig;

/**
 *Class used to edit databases used by RPGConnector
 * @author paul.koroski
 */
public class DBManager {
    private ArrayList<Character> Characters;
    private Connection Database;
    private Statement stmt;
    
    /**
     *Creates and connects to a file named DND.db if it does not already exist.
     * Creates empty character, item, skillset, and abilityset tables if they do not exist.
     */
    public DBManager(){
        Characters = new ArrayList<>();
        String SQLString;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            Database = DriverManager.getConnection("jdbc:sqlite:DND.db", config.toProperties());
            stmt = Database.createStatement();
            
            //Character basic data table creation
            SQLString = "CREATE TABLE IF NOT EXISTS Characters "
                    + "(Name TEXT NOT NULL,"
                    + " Race TEXT NOT NULL,"
                    + " Type TEXT NOT NULL,"
                    + " Class TEXT NOT NULL,"
                    + " Alignment TEXT NOT NULL,"
                    + " Level INTEGER NOT NULL,"
                    + " HitPoints INTEGER NOT NULL,"
                    + " ArmorClass INTEGER NOT NULL,"
                    + " AtkBonus INTEGER NOT NULL,"
                    + " Description TEXT NOT NULL,"
                    + " PRIMARY KEY (Name, Race, Type))";
            this.stmt.executeUpdate(SQLString);
            
            
            SQLString = "CREATE TABLE IF NOT EXISTS SkillSets "
                    + "(Name TEXT NOT NULL,"
                    + " Race TEXT NOT NULL,"
                    + " Type TEXT NOT NULL,"
                    + " Acrobatics INTEGER,"
                    + " Arcana INTEGER,"
                    + " Athletics INTEGER,"
                    + " Bluff INTEGER,"
                    + " Diplomacy INTEGER,"
                    + " Dungeoneering INTEGER,"
                    + " Endurance INTEGER,"
                    + " Heal INTEGER,"
                    + " History INTEGER,"
                    + " Insight INTEGER,"
                    + " Intimidate INTEGER,"
                    + " Nature INTEGER,"
                    + " Perception INTEGER,"
                    + " Religion INTEGER,"
                    + " Stealth INTEGER,"
                    + " Streetwise INTEGER,"
                    + " Thievery INTEGER,"
                    + " PRIMARY KEY (Name, Race, Type),"
                    + " FOREIGN KEY (Name, Race, Type) REFERENCES Characters(Name, Race, Type) ON DELETE CASCADE ON UPDATE CASCADE)";
            this.stmt.executeUpdate(SQLString);
            
            SQLString = "CREATE TABLE IF NOT EXISTS AbilitySets "
                    + "(Name TEXT NOT NULL,"
                    + " Race TEXT NOT NULL,"
                    + " Type TEXT NOT NULL,"
                    + " Strength INTEGER NOT NULL,"
                    + " Constitution INTEGER NOT NULL,"
                    + " Dexterity INTEGER NOT NULL,"
                    + " Intelligence INTEGER NOT NULL,"
                    + " Wisdom INTEGER NOT NULL,"
                    + " Charisma INTEGER NOT NULL,"
                    + " PRIMARY KEY (Name, Race, Type)"
                    + " FOREIGN KEY (Name, Race, Type) REFERENCES Characters(Name, Race, Type) ON DELETE CASCADE ON UPDATE CASCADE)";
            this.stmt.executeUpdate(SQLString);
           
            SQLString = "CREATE TABLE IF NOT EXISTS Items "
                    + "(Name TEXT NOT NULL, "
                    + "Type TEXT NOT NULL, "
                    + "Price TEXT NOT NULL, "
                    + "Magic NUMERIC NOT NULL, "
                    + "Description TEXT NOT NULL,"
                    + "PRIMARY KEY (Name, Type))";
            this.stmt.executeUpdate(SQLString);
            
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Query Methods
    
    /**
     *Executes a query for a character with a specific stat in the Character's table
     * 
     * @param Stat Name of the stat the user is looking for
     * @param Val Value of the stat the user is looking for
     * @return set of characters that have corresponding value for the given stat
     * @throws SQLException syntax error / wrong column name
     */  
    public ResultSet QueryBasicStat(String Stat, String Val) throws SQLException{
        ResultSet result;
        String SQL;
        
        //Check to see if Stat being Queried is a Integer
        if(Stat.equals("Level") || Stat.equals("Hitpoints") || Stat.equals("ArmorClass") || Stat.equals("AtkBonus")){
            try{
                Integer value = Integer.parseInt(Val);
                
                SQL = "SELECT * FROM Characters "
                + "WHERE " + Stat + "= " + value;
                result = this.stmt.executeQuery(SQL);
                return result;
            }catch(NumberFormatException e){
                return null;
            }
        
            // Stat being queries is a String
        }else{
            SQL = "SELECT * FROM Characters "
                + "WHERE " + Stat + "='" + Val + "'";
            
            result = this.stmt.executeQuery(SQL);
            return result;
        }
    }
    
    /**
     *Execute Query for a character with a specific ability score
     * @param ability name of an ability
     * @param value value of the ability the user is searching for
     * @return set of character with the given ability score
     * 
     * @throws SQLException if the query fails, syntax error / wrong column name
     */
    public ResultSet AbilityQuery(String ability, Integer value) throws SQLException{
        String SQL;
        ResultSet result;
        
        SQL = "SELECT * FROM AbilitySets "
                + "WHERE " + ability + "= " + value;
        
        result = this.stmt.executeQuery(SQL);
        return result;
        
    }
    
    /**
     *Executes a query for a character with a specific Skill score
     * @param skill name of a skill
     * @param value value of the skill the user is looking for
     * @return set of characters with the given skill score
     * @throws SQLException if query fails, syntax error / wrong column name
     */
    public ResultSet SkillQuery(String skill, Integer value) throws SQLException{
        String SQL;
        ResultSet result;
        
        SQL = "SELECT * FROM SkillSets "
                + "WHERE " + skill + "= " + value;
        
        result = this.stmt.executeQuery(SQL);
        
        return result;
    }
    
    /**
     *Executes a query for a specific character
     * 
     * @param name name of the character
     * @param race race of the character
     * @param type type of the character
     * @return the character with the given name, race and type
     * @throws SQLException if query fails, syntax error
     */
    public ResultSet CharacterQuery(String name, String race, String type) throws SQLException{
        String SQL;
        ResultSet result;
        String Name = "'"+name+"'";
        String Race = "'"+race+"'";
        String Type = "'"+type+"'";
        
        SQL = "SELECT * FROM Characters"
                + " WHERE Name = " + Type +","
                + " Race = " + Race + ","
                + " Type = " + Type;
        
        result = this.stmt.executeQuery(SQL);
        return result;
    }
    
    public void addBasicInfo(
            String Name, 
            String Race, 
            String Type, 
            String Class, 
            String Alignment,
            Integer Level,
            Integer HP, 
            Integer AC, 
            Integer ATK, 
            String Desc) throws SQLException{
        
        //Prepare strings for sql statement
        String name = "'" + Name + "'";
        String race = "'" + Race + "'";
        String type = "'" + Type + "'";
        String classname = "'" + Class + "'";
        String align = "'" + Alignment + "'";
        String desc = "'" + Desc + "'";
        
        
        
        
        String SQL = "INSERT OR REPLACE INTO Characters "
                + "VALUES ("
                +  name + ", "
                +  race + ", "
                +  type + ", "
                +  classname +", "
                +  align + ", "
                +  Level + ", "
                +  HP + ", "
                +  AC + ", "
                +  ATK + ", "
                +  desc + ")";
        
        stmt.executeUpdate(SQL);
    }
    
    public void addAbilitySet(String Name, String Race, String Type, Integer STR, Integer CON, Integer DEX, Integer INT, Integer WIS, Integer CHA) throws SQLException{
        String name = "'" + Name + "'";
        String race = "'" + Race + "'";
        String type = "'" + Type + "'";
        
        String SQL = "INSERT OR REPLACE INTO AbilitySets VALUES ("
                + name + ", "
                + race + ", "
                + type + ", "
                + STR + ", "
                + CON + ", "
                + DEX + ", "
                + INT + ", "
                + WIS + ", "
                + CHA + ")";
        
        stmt.execute(SQL);
    }
    
    public void addSkillSet(String Name, String Race, String Type) throws SQLException{
        String name = "'" + Name + "'";
        String race = "'" + Race + "'";
        String type = "'" + Type + "'";
        
        String SQL = "INSERT OR REPLACE INTO SkillSets (Name, Race, Type) VALUES ("
                + name + ", "
                + race + ", "
                + type + ")";
        
        stmt.executeUpdate(SQL);
    }
    
    public void updateSkill(String Name, String Race, String Type, String Skill, Integer Value) throws SQLException{
        String name = "'" + Name + "'";
        String race = "'" + Race + "'";
        String type = "'" + Type + "'";
        
        String SQL = "UPDATE SkillSets "
                + "SET " + Skill + " = " + Value
                + " WHERE Name = " + name
                + " AND Race = " + race
                + " AND Type = " + type;
        
        stmt.executeUpdate(SQL);
    }
    
    
    public void LoadDatabase(){
        Random RandInt = new Random();
        
        for(int i=1; i<201; i++ ){
            
        }
    }
    
}
