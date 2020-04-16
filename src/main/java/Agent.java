/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jessica
 */
public interface Agent {
    
    
    public int[][] getPrefList();
    
    public void setPrefList(int[][] list);

    public int getPosOfResident(int residentNo);

    public int compareTo(Agent a2);
    
    public boolean areAgentsRankedIdentically(int r1, int r2);
}
