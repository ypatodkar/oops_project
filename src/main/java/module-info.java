module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires org.apache.logging.log4j;
    requires org.json;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}