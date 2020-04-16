

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jessica
 */
public class Resident implements Agent {

    int[][] preferenceList;

    public int[][] getPrefList() {
        return preferenceList;
    }

    public void setPrefList(int[][] list) {
        preferenceList = list;
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
            if (preferenceList[i][0] > h2preferenceList[i][0]) {
                return 1;
            }
            if (h2preferenceList[i][0] > preferenceList[i][0]) {
                return -1;
            }

        }

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

}
