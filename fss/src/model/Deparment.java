package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public enum Deparment {
    IT, HR, Marketing, Buchhaltung, Produktion;

    public static ObservableList<Deparment> valuesAsObservableList() {
        return FXCollections.observableList(Arrays.asList(values()));
    }
}
