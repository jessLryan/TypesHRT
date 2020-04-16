/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jessica
 */
public class Hospital implements Agent {

    int[][] preferenceList;
    int capacity;

    public int[][] getPrefList() {
        return preferenceList;
    }

    public void setPrefList(int[][] list) {
        preferenceList = list;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int cap) {
        capacity = cap;
    }
    
    public int getPosOfResident(int residentNo) {
        for (int i=0;i<preferenceList.length;i++) {
            for (int j=0;j<preferenceList[i].length;j++) {
                if (preferenceList[i][j]==residentNo) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public boolean areAgentsRankedIdentically(int r1, int r2) {
        return (getPosOfResident(r1)==getPosOfResident(r2));
    }

    @Override
    public int compareTo(Agent h2) {
        int[][] h2preferenceList = h2.getPrefList();
        int prefListLength = preferenceList.length;
        int h2prefListLength = h2preferenceList.length;
        for (int i = 0;; i++) {
            if (i == prefListLength && h2prefListLength > i + 1) {
                return -1;
            }
            if (i == h2prefListLength && prefListLength > i + 1) {
                return 1;
            }
            if (i == h2prefListLength && i == prefListLength) {
                return 0;
            }
            for (int p = 0;; p++) {
                if (p == preferenceList[i].length && h2preferenceList[i].length > p + 1) {
                    return -1;
                }
                if (p == h2preferenceList[i].length && preferenceList[i].length > p + 1) {
                    return 1;
                }
                if (p == h2preferenceList[i].length && p == preferenceList[i].length) {
                    return 0;
                }
                if (preferenceList[i][p] > h2preferenceList[i][p]) {
                    return 1;
                }
                if (h2preferenceList[i][p] > preferenceList[i][p]) {
                    return -1;
                }
            }
            
        }

    }

}
