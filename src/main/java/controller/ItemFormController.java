package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.CustomerDto;
import dto.ItemDto;
import dto.tm.CustomerTm;
import dto.tm.ItemTm;
import dto.tm.OrderTm;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.ItemModel;
import models.impl.ItemModelImpl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ItemFormController {

    public JFXTreeTableView<ItemTm> tableItem;
    @FXML
    private BorderPane borderPane;

    @FXML
    private TreeTableColumn colCode;

    @FXML
    private TreeTableColumn colDes;

    @FXML
    private TreeTableColumn colOption;

    @FXML
    private TreeTableColumn colQty;

    @FXML
    private TreeTableColumn colUnitPrice;

    @FXML
    private JFXTextField txtCode;

    @FXML
    private JFXTextField txtDes;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTextField txtUnitPrice;

    private ItemModel itemModel = new ItemModelImpl();
    ObservableList<ItemTm>tmList = FXCollections.observableArrayList();

    public void initialize() throws SQLException, ClassNotFoundException {
        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDes.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colUnitPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));
        loadItemTable();
        searchItem();
        tableItem.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData(newValue.getValue());

        });

    }

    private void setData(CustomerTm newValue) {
        if (newValue != null) {
            txtCode.setEditable(false);
            txtDes.setText(newValue.getId());
            txtUnitPrice.setText(newValue.getName());
            txtQty.setText(newValue.getAddress());
        }
    }

    private void searchItem() {
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                tableItem.setPredicate(new Predicate<TreeItem<ItemTm>>() {
                    @Override
                    public boolean test(TreeItem<ItemTm> treeItem) {
                        return treeItem.getValue().getCode().contains(newValue) ||
                                treeItem.getValue().getDesc().contains(newValue);
                    }
                });
            }
        });
    }

    private void setData(ItemTm newValue) {
        if (newValue != null) {
            txtCode.setEditable(false);
            txtCode.setText(newValue.getCode());
            txtDes.setText(newValue.getDesc());
            txtUnitPrice.setText(String.valueOf(newValue.getUnitPrice()));
            txtQty.setText(String.valueOf(newValue.getQty()));
        }
    }

    private void loadItemTable() throws SQLException, ClassNotFoundException {
        //ObservableList<ItemTm>tmList = FXCollections.observableArrayList();
        try {
            List<ItemDto> dtoList = itemModel.allItems();

            for (ItemDto dto:dtoList) {
                JFXButton btn = new JFXButton("Delete");

                ItemTm c = new ItemTm(
                        dto.getCode(),
                        dto.getDesc(),
                        dto.getUnitPrice(),
                        dto.getQty(),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    try {
                        deleteItem(c.getCode());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                tmList.add(c);
            }
            TreeItem<ItemTm> treeObject = new RecursiveTreeItem<ItemTm>(tmList, RecursiveTreeObject::getChildren);
            tableItem.setRoot(treeObject);
            tableItem.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteItem(String code) throws SQLException, ClassNotFoundException {
        boolean v = itemModel.deleteItem(code);
            if (v){
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted!").show();
                loadItemTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
            }
    }

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Dashboard.fxml"))));

    }

    public void updateButtonOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException,NullPointerException {
        boolean isUpdated= false;
        try {
            isUpdated = itemModel.updateItem(new ItemDto(
                    txtCode.getText(),
                    txtDes.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtQty.getText())
                    ));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (isUpdated){
            new Alert(Alert.AlertType.INFORMATION,"Item Updated!").show();
            loadItemTable();
            clearFields();
        }else{
            new Alert(Alert.AlertType.ERROR,"Something went wronge !").show();
        }

        }

    private void clearFields() {
        tableItem.refresh();
        txtCode.clear();
        txtDes.clear();
        txtUnitPrice.clear();
        txtQty.clear();
    }


    public void savebuttonOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        boolean isSaved = itemModel.saveItem(new ItemDto(
           txtCode.getText(),
           txtDes.getText(),
           Double.parseDouble(txtUnitPrice.getText()),
           Integer.parseInt(txtQty.getText())
        ));
        if (isSaved){
            new Alert(Alert.AlertType.INFORMATION,"Customer Saved!").show();
            loadItemTable();
            clearFields();
        }
    }
}
