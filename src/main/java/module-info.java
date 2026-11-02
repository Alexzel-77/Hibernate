module ni.edu.ni.hibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens ni.edu.ni.hibernate to javafx.fxml, org.hibernate.orm.core;
    exports ni.edu.ni.hibernate;
}
