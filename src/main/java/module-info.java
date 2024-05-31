module com.mycompany.projectbiblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens com.mycompany.projectbiblioteca to javafx.fxml;
    exports com.mycompany.projectbiblioteca;
}
