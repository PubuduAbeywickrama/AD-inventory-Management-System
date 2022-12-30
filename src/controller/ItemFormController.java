package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tm.itemsTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemFormController {

    public AnchorPane root;
    public TableView<itemsTM> tblItems;
    public TextField txtItemCode;
    public TextField txtItemName;
    public TextField txtDealerPrice;
    public TextField txtSellingPrice;
    public TextField txtQuantity;
    public TextField txtItemCode1;
    public TextField txtAddQuantity;

    public void initialize(){
        tblItems.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        tblItems.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("dealerPrice"));
        tblItems.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        tblItems.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadtable();

        tblItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<itemsTM>() {
            @Override
            public void changed(ObservableValue<? extends itemsTM> observable, itemsTM oldValue, itemsTM newValue) {
                txtItemCode.setText(newValue.getItemCode());
                txtItemName.setText(newValue.getItemName());
                txtDealerPrice.setText(newValue.getDealerPrice().toString());
                txtSellingPrice.setText(newValue.getSellingPrice().toString());
                txtQuantity.setText(String.valueOf(newValue.getQuantity()));
                txtItemCode1.setText(newValue.getItemCode());
            }
        });
    }

    public void loadtable(){
        ObservableList<itemsTM> item = tblItems.getItems();
        item.clear();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from items");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String i_code = resultSet.getString(1);
                String i_name = resultSet.getString(2);
                Double dp = resultSet.getDouble(3);
                Double sp = resultSet.getDouble(4);
                int q = resultSet.getInt(5);

                itemsTM itemstm = new itemsTM(i_code,i_name,dp,sp,q);
                item.add(itemstm);
            }
            tblItems.refresh();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnAddtoStockOnAction(ActionEvent actionEvent) {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            int addQua = Integer.valueOf(txtAddQuantity.getText());
            int stockQua = Integer.valueOf(txtQuantity.getText());
            int sum = addQua+stockQua;
            PreparedStatement preparedStatement = connection.prepareStatement("update items set quantity = ? where item_code = ?");
            preparedStatement.setObject(1,sum);
            preparedStatement.setObject(2,txtItemCode1.getText());

            preparedStatement.executeUpdate();

            txtItemCode1.clear();
            txtAddQuantity.clear();

            loadtable();

            txtItemCode.clear();
            txtItemName.clear();
            txtDealerPrice.clear();
            txtSellingPrice.clear();
            txtQuantity.clear();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM items WHERE item_code = ?");
            preparedStatement.setObject(1,txtItemCode.getText());
            preparedStatement.executeUpdate();

            loadtable();

            txtItemCode.clear();
            txtItemName.clear();
            txtDealerPrice.clear();
            txtSellingPrice.clear();
            txtQuantity.clear();
            txtItemCode1.clear();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
            Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE items SET item_name = ?,dealer_price = ?,selling_price = ?,quantity = ? WHERE item_code = ?");
            preparedStatement.setObject(1,txtItemName.getText());
            preparedStatement.setObject(2,txtDealerPrice.getText());
            preparedStatement.setObject(3,txtSellingPrice.getText());
            preparedStatement.setObject(4,txtQuantity.getText());
            preparedStatement.setObject(5,txtItemCode.getText());

            preparedStatement.executeUpdate();

            loadtable();

            txtItemCode.clear();
            txtItemName.clear();
            txtDealerPrice.clear();
            txtSellingPrice.clear();
            txtQuantity.clear();
            txtItemCode1.clear();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnInsertOnAction(ActionEvent actionEvent) {
            Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO items VALUES (?,?,?,?,?)");
            preparedStatement.setObject(1,txtItemCode.getText());
            preparedStatement.setObject(2,txtItemName.getText());
            preparedStatement.setObject(3,txtDealerPrice.getText());
            preparedStatement.setObject(4,txtSellingPrice.getText());
            preparedStatement.setObject(5,txtQuantity.getText());

            preparedStatement.executeUpdate();
            loadtable();

            txtItemCode.clear();
            txtItemName.clear();
            txtDealerPrice.clear();
            txtSellingPrice.clear();
            txtQuantity.clear();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtItemCode.clear();
        txtItemName.clear();
        txtDealerPrice.clear();
        txtSellingPrice.clear();
        txtQuantity.clear();
        txtItemCode1.clear();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/MainForn.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) this.root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Form");
        primaryStage.centerOnScreen();
    }
}
