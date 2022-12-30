package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tm.inStockTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MainFormController {
    public AnchorPane root;
    public TableView<inStockTM> tblinStock;
    public DatePicker dtpDODate;
    public ComboBox cboDOItemCode;
    public TextField txtDOQuantity;
    public DatePicker dtpDRDate;
    public ComboBox cboDRItemCode;
    public TextField txtDRQuantity;
    public DatePicker dtpDEDate;
    public ComboBox cboDEItemCode;
    public TextField txtDEQuantity;
    public TextField txtDOItemCode;
    public TextField txtDRItemCode;
    public TextField txtDEItemCode;

    public void initialize(){
        tblinStock.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        tblinStock.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tblinStock.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        loadtable();
        //loadlist();

        LocalDate tday = LocalDate.now();
        dtpDODate.setValue(tday);
        dtpDEDate.setValue(tday);
        dtpDRDate.setValue(tday);

        tblinStock.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<inStockTM>() {
            @Override
            public void changed(ObservableValue<? extends inStockTM> observable, inStockTM oldValue, inStockTM newValue) {
                txtDOItemCode.setText(newValue.getItemCode());
                txtDRItemCode.setText(newValue.getItemCode());
                txtDEItemCode.setText(newValue.getItemCode());
            }
        });
    }
    public void loadtable(){
        ObservableList<inStockTM> instock = tblinStock.getItems();
        instock.clear();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select item_code,item_name,quantity from items");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String i_code = resultSet.getString(1);
                String i_name = resultSet.getString(2);
                int s_qua = resultSet.getInt(3);
                inStockTM instocktm = new inStockTM(i_code,i_name,s_qua);
                instock.add(instocktm);
            }
            tblinStock.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public void btnItemsOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/ItemForm.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Items");
        primaryStage.centerOnScreen();
    }

    public void btnOutOnAction(ActionEvent actionEvent) {
        Connection connection = DBConnection.getInstance().getConnection();
        String itemCode = txtDOItemCode.getText();
        LocalDate date = dtpDODate.getValue();
        int out_quantity = Integer.valueOf(txtDOQuantity.getText());

        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("select item_name,quantity from items where item_code =?");
            preparedStatement1.setObject(1,itemCode);
            ResultSet resultSet = preparedStatement1.executeQuery();
            String itemName = "";
            int quantity = 0;
            while (resultSet.next()){
                itemName = resultSet.getString(1);
                quantity = resultSet.getInt(2);
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transaction (item_code,item_name,date,d_out) values(?,?,?,?)");
            preparedStatement.setObject(1,itemCode);
            preparedStatement.setObject(2,itemName);
            preparedStatement.setObject(3,date);
            preparedStatement.setObject(4,txtDOQuantity.getText());

            preparedStatement.executeUpdate();


            int new_quantity = quantity-out_quantity;

            PreparedStatement preparedStatement2 = connection.prepareStatement("Update items set quantity = ? where item_code = ?");
            preparedStatement2.setObject(1,new_quantity);
            preparedStatement2.setObject(2,itemCode);
            preparedStatement2.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        loadtable();
        txtDOQuantity.clear();
        txtDOItemCode.clear();
        txtDRQuantity.clear();
        txtDRItemCode.clear();
        txtDEItemCode.clear();
        txtDEQuantity.clear();
        //loadlist();
    }



    public void btnReturnOnAction(ActionEvent actionEvent) {
        String itemCode = txtDRItemCode.getText();
        LocalDate date = dtpDRDate.getValue();
        int return_quantity = Integer.valueOf(txtDRQuantity.getText());
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Update transaction set d_return = ? where item_code =? and date = ?");
            preparedStatement.setObject(1,return_quantity);
            preparedStatement.setObject(2,itemCode);
            preparedStatement.setObject(3,date);

            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement1 = connection.prepareStatement("select quantity from items where item_code = ?");
            preparedStatement1.setObject(1,itemCode);
            ResultSet resultSet = preparedStatement1.executeQuery();

            int quantity = 0;
            while (resultSet.next()){
                quantity = resultSet.getInt(1);
            }
            int new_quantity = quantity+return_quantity;

            PreparedStatement preparedStatement2 = connection.prepareStatement("Update items set quantity = ? where item_code = ?");
            preparedStatement2.setObject(1,new_quantity);
            preparedStatement2.setObject(2,itemCode);
            preparedStatement2.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        loadtable();
        txtDRQuantity.clear();
        txtDRItemCode.clear();
        txtDOQuantity.clear();
        txtDOItemCode.clear();
        txtDEItemCode.clear();
        txtDEQuantity.clear();
    }



    public void btnDamageOnAction(ActionEvent actionEvent) {
        String itemCode = txtDEItemCode.getText();
        LocalDate date = dtpDEDate.getValue();
        int damage_quantity = Integer.valueOf(txtDEQuantity.getText());
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("Update transaction set damage = ? where item_code =? and date = ?");
            preparedStatement.setObject(1,damage_quantity);
            preparedStatement.setObject(2,itemCode);
            preparedStatement.setObject(3,date);

            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement1 = connection.prepareStatement("select quantity from items where item_code = ?");
            preparedStatement1.setObject(1,itemCode);
            ResultSet resultSet = preparedStatement1.executeQuery();

            int quantity = 0;
            while (resultSet.next()){
                quantity = resultSet.getInt(1);
            }
            int new_quantity = quantity-damage_quantity;

            PreparedStatement preparedStatement2 = connection.prepareStatement("Update items set quantity = ? where item_code = ?");
            preparedStatement2.setObject(1,new_quantity);
            preparedStatement2.setObject(2,itemCode);
            preparedStatement2.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        loadtable();
        txtDRQuantity.clear();
        txtDRItemCode.clear();
        txtDOQuantity.clear();
        txtDOItemCode.clear();
        txtDEItemCode.clear();
        txtDEQuantity.clear();
    }

    public void btnSummaryOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/SummaryForm.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Summary");
        primaryStage.centerOnScreen();
    }





    /*public void loadlist(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT item_code from items");

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList doitem = cboDOItemCode.getItems();
            ObservableList dritem = cboDRItemCode.getItems();
            ObservableList deitem = cboDEItemCode.getItems();
            while (resultSet.next()){
                String doi = resultSet.getString(1);
                doitem.add(doi);
                dritem.add(doi);
                deitem.add(doi);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }*/
}
