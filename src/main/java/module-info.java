module ni.edu.ni.hibernate {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.ni.hibernate to javafx.fxml;
    exports ni.edu.ni.hibernate;
}