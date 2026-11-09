module cl.duoc.viviendas {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens cl.duoc.viviendas to javafx.fxml;
    opens cl.duoc.viviendas.model to com.google.gson, javafx.base;
    opens cl.duoc.viviendas.controller to javafx.fxml;


    exports cl.duoc.viviendas;
}