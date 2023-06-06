package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public enum Department {
    IT, HR, Marketing, Buchhaltung, Produktion;

    public static ObservableList<Department> valuesAsObservableList() {
        return FXCollections.observableList(Arrays.asList(values()));
    }
}
