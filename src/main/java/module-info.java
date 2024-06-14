module com.example.projektksiegarnia {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    opens com.example.projektksiegarnia to javafx.fxml;
    exports com.example.projektksiegarnia;
    exports constants;
    opens constants to javafx.fxml;
    exports com.example.projektksiegarnia.viewmodels;
    opens com.example.projektksiegarnia.viewmodels to javafx.fxml;
    opens com.example.projektksiegarnia.views to org.hibernate.orm.core;
}