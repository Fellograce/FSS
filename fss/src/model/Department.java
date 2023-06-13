package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

/**
 * Department as enum class.
 */
public enum Department {
    IT, HR, Marketing, Buchhaltung, Produktion;

    /**
     * Returns a list of departments
     * @return departments as list
     */
    public static ObservableList<Department> valuesAsObservableList() {
        return FXCollections.observableList(Arrays.asList(values()));
    }
}
