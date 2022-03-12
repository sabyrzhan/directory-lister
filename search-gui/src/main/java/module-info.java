module kz.sabyrzhan.searchgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens kz.sabyrzhan.searchgui to javafx.fxml;
    exports kz.sabyrzhan.searchgui;
}