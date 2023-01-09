module com.mwgroup.shannonfanocode {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mwgroup.shannonfanocode to javafx.fxml;
    exports com.mwgroup.shannonfanocode;
}