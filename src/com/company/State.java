package com.company;

import java.util.ArrayList;
import java.util.List;

public class State {
    public State(String _name) {
        name = _name;
    }
    public String name;
    public List<State> nextStates = new ArrayList<>();
}
