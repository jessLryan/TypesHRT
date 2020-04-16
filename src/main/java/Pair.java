/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jessica
 */
public class Pair<Key,Value> {
    private Key key;
    private Value value;
    public Pair(Key key, Value value){
        this.key = key;
        this.value = value;
    }
    public Key getKey(){ 
        return key; 
    }
    public Value getValue(){ 
        return value; 
    }
    public void setKey(Key key){
        this.key = key; 
    }
    public void setValue(Value value){ 
        this.value = value; 
    }
}