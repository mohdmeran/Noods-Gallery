/* doesn't work with source level 1.8:
module com.imranfx.hellofx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.imranfx.hellofx to javafx.fxml;
    exports com.imranfx.hellofx;
}
*/
