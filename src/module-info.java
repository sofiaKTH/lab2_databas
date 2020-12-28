module labb2_databas {
    requires javafx.controls;
    requires mongo.java.driver;
    opens controller;
    opens model;
    opens view;
}