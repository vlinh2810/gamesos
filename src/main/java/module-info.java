module com.example.gamesos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;
    requires java.sql;
    requires com.rabbitmq.client;

    opens com.example.gamesos to javafx.fxml;
    exports com.example.gamesos;
}