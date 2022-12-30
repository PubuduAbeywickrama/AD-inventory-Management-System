package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane root;
    public TextField txtUserName;
    public PasswordField txtPassword;



    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        String uName = txtUserName.getText();
        String pw = txtPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from ad_det where userName = ? AND password = ?");
            preparedStatement.setObject(1,uName);
            preparedStatement.setObject(2,pw);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean isExist = resultSet.next();

            if(isExist){
                Parent parent = FXMLLoader.load(this.getClass().getResource("../view/MainForn.fxml"));
                Scene scene = new Scene(parent);

                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.setTitle("Main Form");
                primaryStage.centerOnScreen();
            }
            else{
                new Alert(Alert.AlertType.WARNING,"Invalid User Name or Password").showAndWait();
                txtPassword.clear();
                txtUserName.clear();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



    }
}
