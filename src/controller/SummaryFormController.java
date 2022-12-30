package controller;

import db.DBConnection;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tm.SummaryTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SummaryFormController {
    public ComboBox cboItemCode;
    public DatePicker dtpDate;
    public TableView<SummaryTM> tblSummary;
    public AnchorPane root;
    public void initialize(){
        tblSummary.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("item_code"));
        tblSummary.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("item_name"));
        tblSummary.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("date"));
        tblSummary.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("d_out"));
        tblSummary.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("d_return"));
        tblSummary.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("damage"));
        tblSummary.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("init"));

        loadtable("select * from transaction");

        LocalDate tday = LocalDate.now();
        dtpDate.setValue(tday);

        loadlist();
    }


    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/MainForn.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main");
        primaryStage.centerOnScreen();
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
    }

    public void loadtable(String query){
        Connection connection = DBConnection.getInstance().getConnection();
        ObservableList<SummaryTM> summary = tblSummary.getItems();
        summary.clear();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String i_code = resultSet.getString(1);
                String i_name = resultSet.getString(2);
                String date = resultSet.getString(3);
                int d_o = resultSet.getInt(4);
                int d_r = resultSet.getInt(5);
                int d_d = resultSet.getInt(6);

                SummaryTM summarytm = new SummaryTM(i_code,i_name,date,d_o,d_r,d_d);
                summary.add(summarytm);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnTdaySummaryOnAction(ActionEvent actionEvent) {
        LocalDate d = LocalDate.now();
        loadtable("select * from transaction where date = '"+d+"'");
    }

    public void loadlist(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT item_code from items");

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList doitem = cboItemCode.getItems();

            while (resultSet.next()){
                String doi = resultSet.getString(1);
                doitem.add(doi);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnFilterByItemOnAction(ActionEvent actionEvent) {
        String item = cboItemCode.getValue().toString();

        loadtable("select * from transaction where item_code = '"+item+"' ");
    }

    public void btnFilterByDateOnAction(ActionEvent actionEvent) {
        LocalDate d = dtpDate.getValue();
        loadtable("select * from transaction where date = '"+d+"' ");

        dtpDate.setValue(LocalDate.now());
    }
}
