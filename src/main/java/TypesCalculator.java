
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jessica
 */
public class TypesCalculator {

    public static void main(String[] args) throws Exception {
        String hospitalsInput = args[0];
        String residentsInput = args[1];
        BufferedReader hospitalsBufferedReader = getReader(hospitalsInput);
        BufferedReader residentsBufferedReader = getReader(residentsInput);
        List<Pair<Integer, Agent>> hospitals = readToHospitals2(hospitalsBufferedReader);
        List<Pair<Integer, Agent>> unsortedResidents = readToResidents2(residentsBufferedReader);
        System.out.println("residents size "+unsortedResidents.size());
        List<Pair<Integer, Agent>> sortedResidents = sortAgents(unsortedResidents);
        List<ArrayList<Pair<Integer, Agent>>> groupedResidents = groupAgentsIdenticalPrefs(sortedResidents);
        List<ArrayList<Pair<Integer, Agent>>> typedResidents = findTypes(groupedResidents, hospitals);
        System.out.println("typed residents size " + typedResidents.size());
        for (ArrayList<Pair<Integer, Agent>> type : typedResidents) {
            if (type.size() > 1) {
                System.out.println("type");
                for (Pair<Integer, Agent> pair : type) {
                    System.out.println(pair.getKey());
                }
            }
        }

    }

    public static List<ArrayList<Pair<Integer, Agent>>> findTypes(List<ArrayList<Pair<Integer, Agent>>> groupedAgents, List<Pair<Integer, Agent>> hospitals) {
        List<ArrayList<Pair<Integer, Agent>>> groupedAgentsReturned = new ArrayList<ArrayList<Pair<Integer, Agent>>>();
        for (ArrayList<Pair<Integer, Agent>> group : groupedAgents) {
            ArrayList<Pair<Integer, Agent>> groupCopy = (ArrayList<Pair<Integer, Agent>>) group.clone();
            while (!groupCopy.isEmpty()) {
                ArrayList<Pair<Integer, Agent>> type = new ArrayList<Pair<Integer, Agent>>();
                Pair<Integer, Agent> agentOne = groupCopy.get(0);
                type.add(agentOne);
                groupCopy.remove(agentOne);
                for (int i = 0; i < groupCopy.size(); i++) {
                    Pair<Integer, Agent> agentI = groupCopy.get(i);
                    if (areResidentsConsideredIdentical(agentOne.getKey(), agentI.getKey(), hospitals)) {
                        type.add(agentI);
                        groupCopy.remove(agentI);
                    }
                }
                groupedAgentsReturned.add(type);
            }
        }
        return groupedAgentsReturned;
    }

    public static boolean areResidentsConsideredIdentical(int r1, int r2, List<Pair<Integer, Agent>> hospitals) {
        for (Pair<Integer, Agent> p : hospitals) {
            if (!p.getValue().areAgentsRankedIdentically(r1, r2)) {
                return false;
            }
        }
        return true;
    }

    public static List<ArrayList<Pair<Integer, Agent>>> groupAgentsIdenticalPrefs(List<Pair<Integer, Agent>> agents) {
        List<ArrayList<Pair<Integer, Agent>>> groupedAgents = new ArrayList<>();
        ArrayList<Pair<Integer, Agent>> currentList = new ArrayList<>();
        currentList.add(agents.get(0));
        Agent currentAgent = agents.get(0).getValue();
        int pos = 1;
        while (pos < agents.size()) {
            Agent newAgent = agents.get(pos).getValue();
            if (currentAgent.compareTo(newAgent) == 0) {
                currentList.add(agents.get(pos));
                currentAgent = newAgent;
            } else {
                groupedAgents.add(currentList);
                currentList = new ArrayList();
                currentList.add(agents.get(pos));
                currentAgent = newAgent;
            }
            pos++;
            if (pos == agents.size()) {
                if (!currentList.isEmpty()) {
                    groupedAgents.add(currentList);
                }
            }

        }
        return groupedAgents;
    }

    public static List<Pair<Integer, Agent>> sortAgents(List<Pair<Integer, Agent>> unsortedList) {
        Collections.sort(unsortedList, new Comparator<Pair<Integer, Agent>>() {
            public int compare(Pair<Integer, Agent> o1,
                    Pair<Integer, Agent> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return unsortedList;
    }

    public static BufferedReader getReader(String name) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(name)));
        } catch (FileNotFoundException e) {
            System.out.print("File " + name + " cannot be found");
        }
        return in;
    }

    private static List<Pair<Integer, Agent>> readToHospitals1(BufferedReader reader) {
        List<Pair<Integer, Agent>> hospitals = new ArrayList<Pair<Integer, Agent>>();
        try {
            String line = reader.readLine();
            int k = 0;
            while ((line = reader.readLine()) != null) {
                int key = k + 1;
                Hospital h = new Hospital();
                int j = 0;
                j = line.indexOf(':') + 1;
                String capacity = "";
                while (line.charAt(j) != ':') {
                    capacity += line.charAt(j);
                    j++;
                }
                h.setCapacity(Integer.parseInt(capacity));
                ArrayList<int[]> prefList = new ArrayList<>();

                String remainder = line.substring(j + 1);
                while (!remainder.isBlank() && !remainder.isEmpty()) {
                    if (!remainder.contains("(")) {
                        String[] splitArray = remainder.trim().split("\\s+");
                        for (int i = 0; i < splitArray.length; i++) {
                            int[] tie = new int[1];
                            tie[0] = Integer.parseInt(splitArray[i]);
                            prefList.add(tie);
                        }
                        remainder = "";
                    } else {
                        String preBracket = remainder.substring(0, remainder.indexOf('('));
                        String inBracket = remainder.substring(remainder.indexOf('(') + 1, remainder.indexOf(')'));
                        if (remainder.indexOf(')') == remainder.length() - 1) {
                            remainder = "";
                        } else {
                            remainder = remainder.substring(remainder.indexOf(')') + 1);
                        }
                        if (!preBracket.isBlank() && !preBracket.isEmpty()) {
                            String[] splitArray = preBracket.trim().split("\\s+");
                            for (int i = 0; i < splitArray.length; i++) {
                                int[] tie = new int[splitArray.length];
                                tie[0] = Integer.parseInt(splitArray[i]);
                                prefList.add(tie);
                            }
                        }
                        String[] splitArray = inBracket.trim().split("\\s+");
                        int[] tieArray = Arrays.asList(splitArray).stream().mapToInt(Integer::parseInt).toArray();
                        prefList.add(tieArray);
                    }

                }
                int[][] prefArray = new int[prefList.size()][];
                for (int i = 0; i < prefList.size(); i++) {
                    prefArray[i] = prefList.get(i);
                }
                h.setPrefList(prefArray);
                Pair p = new Pair(key, h);
                hospitals.add(p);
                k++;
            }

        } catch (IOException e) {
            System.out.print("I/O Error");
            System.exit(0);
        }

        return hospitals;
    }

    private static List<Pair<Integer, Agent>> readToHospitals2(BufferedReader reader) {
        List<Pair<Integer, Agent>> hospitals = new ArrayList<Pair<Integer, Agent>>();
        try {
            String line = reader.readLine();
            int k = 0;
            while ((line = reader.readLine()) != null) {
                int key = k + 1;
                Hospital h = new Hospital();
                int j = 2;
                while (Character.isDigit(line.charAt(j))) {
                    j++;
                }
                j++;
                String capacity = "";
                while (Character.isDigit(line.charAt(j))) {
                    capacity += line.charAt(j);
                    j++;
                }
                h.setCapacity(Integer.parseInt(capacity));
                ArrayList<int[]> prefList = new ArrayList<>();

                String remainder = line.substring(j + 1);
                while (!remainder.isBlank() && !remainder.isEmpty()) {
                    if (!remainder.contains("(")) {
                        String[] splitArray = remainder.trim().split("\\s+");
                        for (int i = 0; i < splitArray.length; i++) {
                            int[] tie = new int[1];
                            tie[0] = Integer.parseInt(splitArray[i]);
                            prefList.add(tie);
                        }
                        remainder = "";
                    } else {
                        String preBracket = remainder.substring(0, remainder.indexOf('('));
                        String inBracket = remainder.substring(remainder.indexOf('(') + 1, remainder.indexOf(')'));
                        if (remainder.indexOf(')') == remainder.length() - 1) {
                            remainder = "";
                        } else {
                            remainder = remainder.substring(remainder.indexOf(')') + 1);
                        }
                        if (!preBracket.isBlank() && !preBracket.isEmpty()) {
                            String[] splitArray = preBracket.trim().split("\\s+");
                            for (int i = 0; i < splitArray.length; i++) {
                                int[] tie = new int[splitArray.length];
                                tie[0] = Integer.parseInt(splitArray[i]);
                                prefList.add(tie);
                            }
                        }
                        String[] splitArray = inBracket.trim().split("\\s+");
                        int[] tieArray = Arrays.asList(splitArray).stream().mapToInt(Integer::parseInt).toArray();
                        prefList.add(tieArray);
                    }

                }
                int[][] prefArray = new int[prefList.size()][];
                for (int i = 0; i < prefList.size(); i++) {
                    prefArray[i] = prefList.get(i);
                }
                h.setPrefList(prefArray);
                Pair p = new Pair(key, h);
                hospitals.add(p);
                k++;
            }

        } catch (IOException e) {
            System.out.print("I/O Error");
            System.exit(0);
        }

        return hospitals;
    }

    private static List<Pair<Integer, Agent>> readToResidents1(BufferedReader reader) {
        List<Pair<Integer, Agent>> residents = new ArrayList<Pair<Integer, Agent>>();
        try {
            String line = reader.readLine();
            int k = 0;
            while ((line = reader.readLine()) != null) {
                int key = k + 1;
                Resident r = new Resident();
                int j = 0;
                j = line.indexOf(':') + 1;
                String substring = line.substring(j);
                try {
                    String[] splitArray = substring.trim().split("\\s+");
                    int[][] tieArray = new int[splitArray.length][];
                    for (int i = 0; i < splitArray.length; i++) {
                        int[] candidate = new int[1];
                        candidate[0] = Integer.parseInt(splitArray[i]);
                        tieArray[i] = candidate;
                    }

                    r.setPrefList(tieArray);
                } catch (PatternSyntaxException ex) {
                }
                Pair p = new Pair(key, r);
                residents.add(p);
                k++;
            }

        } catch (IOException e) {
            System.out.print("I/O Error");
            System.exit(0);
        }
        return residents;
    }

    private static List<Pair<Integer, Agent>> readToResidents2(BufferedReader reader) {
        List<Pair<Integer, Agent>> residents = new ArrayList<Pair<Integer, Agent>>();
        try {
            String line = reader.readLine();
            int k = 0;
            while ((line = reader.readLine()) != null) {
                int key = k + 1;
                Resident r = new Resident();
                int j = 2;
                while (Character.isDigit(line.charAt(j))) {
                    j++;
                }
                j++;
                String substring = line.substring(j);
                try {
                    String[] splitArray = substring.trim().split("\\s+");
                    int[][] tieArray = new int[splitArray.length][];
                    for (int i = 0; i < splitArray.length; i++) {
                        int[] candidate = new int[1];
                        candidate[0] = Integer.parseInt(splitArray[i]);
                        tieArray[i] = candidate;
                    }

                    r.setPrefList(tieArray);
                } catch (PatternSyntaxException ex) {
                }
                Pair p = new Pair(key, r);
                residents.add(p);
                k++;
            }

        } catch (IOException e) {
            System.out.print("I/O Error");
            System.exit(0);
        }
        return residents;
    }

}
