package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            File file = new File("example1.txt");
            Scanner fileReader = new Scanner(file);
            int alphabetLength = 0, stateCount = 0, initialState = 0, finalStateCount = 0, currentState = initialState;
            String finalStatesLine;
            ArrayList<String>  branches = new ArrayList<>();
            List<int[]> finalStates = new ArrayList<>();
            List<State> allStates = new ArrayList<>();

            if (fileReader.hasNextLine()) {
                alphabetLength = Integer.parseInt(fileReader.nextLine());
            }
            if (fileReader.hasNextLine()) {
                stateCount = Integer.parseInt(fileReader.nextLine());
                for (int i = 0; i < stateCount; i++) {
                    State newState = new State(Integer.toString(i));
                    allStates.add(i, newState);
                }
            }
            if (fileReader.hasNextLine()) {
                initialState = Integer.parseInt(fileReader.nextLine());
            }
            if (fileReader.hasNextLine()) {
                finalStatesLine = fileReader.nextLine();
                String states = finalStatesLine.substring(finalStatesLine.indexOf(" ") + 1);
                finalStateCount = Integer.parseInt(finalStatesLine.substring(0, finalStatesLine.indexOf(" ")));
                int[] finalStatesArray = Arrays.stream(states.split(" ")).mapToInt(Integer::parseInt).toArray();
                finalStates = Arrays.asList(finalStatesArray);
            }

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                branches.add(line);
            }

            String[] branchesArr = new String[branches.size()];
            branchesArr = branches.toArray(branchesArr);

            if(alphabetLength <= 0 || stateCount <= 0) {
                throw new NumberFormatException("Must be greater than 1");
            }

            if(initialState < 0 || initialState >= stateCount) {
                throw new NumberFormatException("State that should be initial doesn`t exists");
            }

            // String w1="ab", w2="cba";
            Scanner in = new Scanner(System.in);

            System.out.println("w1:");
            String w1 = in.next();
            System.out.println("w2:");
            String w2 = in.next();

            String[] fullBranchArr = new String[branchesArr.length*3];

            fullBranchArr = String.join("  ", branchesArr).split("  ");

            for (Character ch: w1.toCharArray()) {
                for (int i = 0; i < fullBranchArr.length; i++) {
                    if (i % 3 != 0) {
                        continue;
                    }
                    if (currentState == Integer.parseInt(fullBranchArr[i]) && fullBranchArr[i+1].equals(ch.toString())) {
                        currentState = Integer.parseInt(fullBranchArr[i+2]);
                        break;
                    }
                }
            }


            for (int i = 0; i < fullBranchArr.length; i++) {
                if (i % 3 != 0) {
                    continue;
                }
                allStates.get(Integer.parseInt(fullBranchArr[i])).nextStates.add(allStates.get(Integer.parseInt(fullBranchArr[i+2])));
            }

            for (int finalState: finalStates.get(0)) {
                int currentFinalState = finalState;
                boolean isWrong = false;
                for (Character ch: new StringBuilder(w2).reverse().toString().toCharArray()) {
                    for (int i = 0; i < fullBranchArr.length; i++) {
                        if (i % 3 != 0) {
                            continue;
                        }
                        if (currentFinalState == Integer.parseInt(fullBranchArr[i+2]) && fullBranchArr[i+1].equals(ch.toString())) {
                            currentFinalState = Integer.parseInt(fullBranchArr[i]);
                            break;
                        }
                        if (i == fullBranchArr.length - 3) {
                            isWrong = true;
                        }
                    }
                }
                // System.out.println(currentFinalState);
                if (!isWrong) {
                    System.out.println(isPathExists(allStates.get(currentState), new ArrayList<>(), Integer.toString(currentFinalState)));
                }
            }

        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input in input file!");
            System.out.println(e);
        }
        return;
    }

    public static boolean isPathExists (State state, List<State> visitedStates, String finalState) {
        List<State> newVisitedStates = new ArrayList<>(visitedStates);
        newVisitedStates.add(state);
        if (state.name.equals(finalState)) {
            return true;
        }

        for(State nextState: state.nextStates) {
            if (newVisitedStates.contains(nextState)) {
                continue;
            }
            State currentState = nextState;
            if (currentState.name.equals(finalState)) {
                return true;
            }
            boolean res = isPathExists(currentState, newVisitedStates, finalState);
            if (res == true) {
                return true;
            }
        }
        return false;
    }
}


