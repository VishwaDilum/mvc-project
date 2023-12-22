package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import dto.OrderDetailsDto;
import dto.tm.CustomerTm;
import dto.tm.ItemTm;
import dto.tm.OrdersTm;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Predicate;

public class OrderTablesController {

    public TableView<OrdersTm>tblOrder;
    public TableView<OrderDetailsDto>tblOrderDetails;
    public TableColumn colItem;
    public JFXTextField txtSearch;
    @FXML
    private TableColumn colCode;

    @FXML
    private TableColumn colCustId;

    @FXML
    private TableColumn colDate;


    @FXML
    private TableColumn colOrders;
    private String Exid;

    @FXML
   private TableColumn colQty;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private AnchorPane pane;

    @FXML
    ObservableList<OrdersTm>tmList = FXCollections.observableArrayList();
    public void initialize() {
        colOrders.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCustId.setCellValueFactory(new PropertyValueFactory<>("customerid"));

        colItem.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        loadOrders();
        searchFilter();
        loadOrderDetails();
        tblOrder.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            setData(newValue);
        });
    }

    private void setData(OrdersTm newValue) {
        loadOrderDetails();
        Exid = newValue.getId();
    }

    private void loadOrderDetails() {
        ObservableList<OrderDetailsDto> tmList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM orderDetail WHERE orderid = '" + Exid + "'";
        try {
            //Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "2005");
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet result = stm.executeQuery(sql);

            while (result.next()){
                OrderDetailsDto c = new OrderDetailsDto(
                        result.getString(1),
                        result.getString(2),
                        Integer.parseInt(String.valueOf(result.getInt(3))),
                        Double.parseDouble(String.valueOf(result.getDouble(4)))
                );
                tmList.add(c);
            }
            tblOrderDetails.setItems(tmList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void searchFilter() {
        FilteredList<OrdersTm> filterData= new FilteredList<>(tmList,e->true);
        txtSearch.setOnKeyReleased(e->{


            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData.setPredicate((Predicate<? super OrdersTm >) cust->{
                    if(newValue==null){
                        return true;
                    }
                    String toLowerCaseFilter = newValue.toLowerCase();
                    if(cust.getId().contains(newValue)){
                        return true;
                    }else  if(cust.getId().toLowerCase().contains(toLowerCaseFilter)){
                        return true;
                    }else  if (cust.getDate().toString().toLowerCase().contains(toLowerCaseFilter)){
                        return true;
                    }else if(cust.getCustomerid().toLowerCase().contains(toLowerCaseFilter)){
                        return true;
                    }

                    return false;
                });
            });

            final SortedList<OrdersTm> customers = new SortedList<>(filterData);
            customers.comparatorProperty().bind(tblOrder.comparatorProperty());
            tblOrder.setItems(customers);
            //ok let's check it
        });
    }


    private void loadOrders() {
        //ObservableList<OrdersTm> tmList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM orders";
        try {
            //Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "2005");
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet result = stm.executeQuery(sql);

            while (result.next()){
                OrdersTm c = new OrdersTm(
                        result.getString(1),
                        result.getDate(2),
                        result.getString(3)
                );
                tmList.add(c);
            }
            tblOrder.setItems(tmList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void RefreshButtonOnAction(ActionEvent actionEvent) {
        tblOrderDetails.refresh();
        tblOrder.refresh();
        //loadOrders();

    }

    public void BackButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/Dashboard.fxml"))));
    }
}
