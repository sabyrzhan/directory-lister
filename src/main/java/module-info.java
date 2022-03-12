module kz.sabyrzhan.directorylister {
    requires javafx.controls;
    requires javafx.fxml;


    opens kz.sabyrzhan.directorylister to javafx.fxml;
    exports kz.sabyrzhan.directorylister;
}