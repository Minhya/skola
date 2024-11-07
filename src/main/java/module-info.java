module org.example.ttt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.ttt to javafx.fxml;
    exports org.example.ttt;
}