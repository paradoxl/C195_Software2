module com.michael.c195_software2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.michael.c195_software2 to javafx.fxml;
    exports com.michael.c195_software2;
}