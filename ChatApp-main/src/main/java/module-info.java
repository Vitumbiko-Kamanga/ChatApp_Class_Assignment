module org.example.chatapp {
    requires javafx.controls;
    requires javafx.fxml;

// Grants JavaFX permission to look inside and instantiate your controller
    opens org.example.app1 to javafx.fxml;

// Grants JavaFX permission to look inside your main application package
    opens org.example.chatapp to javafx.fxml;

// Allows other modules to use the classes in your main package if necessary
    exports org.example.chatapp;

}