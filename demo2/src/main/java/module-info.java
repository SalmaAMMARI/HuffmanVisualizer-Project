module com.example.demo2 {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    exports huffman.ui;
    exports huffman.algorithm;
    exports huffman.model;

    opens huffman.ui to javafx.graphics;
}