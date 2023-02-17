module com.example.juego_fx_m03 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.juego_fx_m03 to javafx.fxml;
    exports com.example.juego_fx_m03;
}