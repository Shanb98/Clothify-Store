package edu.icet.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.db.DBConnection;
import edu.icet.entity.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static edu.icet.controller.DashItemController.getLastSupplierId;

public class DashOrdersController implements Initializable {

    @FXML
    private Label lblOrderId;

    @FXML
    private JFXTextField txtEmpName;

    @FXML
    private JFXComboBox<String> selectEmpId;

    @FXML
    private JFXTextField txtCusName;

    @FXML
    private JFXTextField txtCusContact;

    @FXML
    private JFXTextField txtCusEmail;

    @FXML
    private JFXComboBox<String> selectPayment;

    @FXML
    private JFXComboBox<String> selectItemType;

    @FXML
    private JFXTextField txtItemDescription;

    @FXML
    private JFXTextField txtItemQty;

    @FXML
    private JFXTextField TxtItemQtyonHand;

    @FXML
    private JFXTextField txtItemId;

    @FXML
    private JFXTextField txtSellingPrice;

    @FXML
    private JFXTextField txtDiscount;

    @FXML
    private Button btnRemoveOrder;

    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnAddtoCart;

    @FXML
    private TableView<?> tblOrder;

    @FXML
    private TableColumn<?, ?> colItemCode;


    @FXML
    private TableColumn<?, ?> colItemType;

    @FXML
    private TableColumn<?, ?> tblItemType;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colSellingPrice;

    @FXML
    private TableColumn<?, ?> colDiscount;

    @FXML
    private TableColumn<?, ?> colFinalPrice;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TextField txtGrandTotal;

    @FXML
    private TextField txtTotalDiscount;

    @FXML
    private Button btnPlaceOrder;

    @FXML
    private DatePicker txtdate;

    @FXML
    private JFXComboBox<String> selectItemSize;

    @FXML
    private JFXTextField txtFinalPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateId();
        try {
            loadEmployerIds();
            loadItemIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new  RuntimeException(e);
        }
        selectEmpId.setOnAction(event -> {
            loadEmployerName();
        });
        selectItemType.setOnAction(event -> {
            loadItemTypesWithSizes();
            String selectedEmployerId = selectEmpId.getValue();
        });
        setupItemSizeActionEvent(); // Call this method to set up the action event for selectItemSize
        loadCmb();
    }

    private void generateId() {
        try {
            String lastSupplierId = getLastSupplierId();
            if (lastSupplierId != null && lastSupplierId.matches("ORD\\d{4}")) {
                int numericPart = Integer.parseInt(lastSupplierId.substring(3)) + 1;
                String newSupplierId = String.format("ORD%04d", numericPart);
                lblOrderId.setText(newSupplierId);
            } else {
                lblOrderId.setText("ORD0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getEmployerIds() throws SQLException, ClassNotFoundException {
        List<String> ids = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT EMPID FROM employer").executeQuery();
        while (rst.next()) {
            ids.add(rst.getString(1));
        }
        return ids;
    }

    private void loadEmployerIds() throws SQLException, ClassNotFoundException {
        List<String> employerIds = new DashOrdersController().getEmployerIds();
        selectEmpId.getItems().addAll(employerIds);
    }
    private void loadEmployerName() {
        String selectedEmployerId = selectEmpId.getValue();
        if (selectedEmployerId != null) {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                Statement stm = connection.createStatement();
                String SQL = "SELECT EMPName FROM employer WHERE EMPID = '" + selectedEmployerId + "'";
                ResultSet rst = stm.executeQuery(SQL);

                if (rst.next()) {
                    String supplierName = rst.getString("EMPName");
                    txtEmpName.setText(supplierName);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Supplier not found").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public List<String> getItemIds() throws SQLException, ClassNotFoundException {
        List<String> ids = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT DISTINCT type FROM item").executeQuery();
        while (rst.next()) {
            ids.add(rst.getString(1));
        }
        return ids;
    }

    private void loadItemIds() throws SQLException, ClassNotFoundException {
        List<String> itemIds = getItemIds();
        selectItemType.getItems().addAll(itemIds);

    }

    private void loadItemTypesWithSizes() {
        String selectedItemType = selectItemType.getValue();
        if (selectedItemType != null) {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                Statement stm = connection.createStatement();
                String SQL = "SELECT DISTINCT size FROM item WHERE type = '" + selectedItemType + "'";
                ResultSet rst = stm.executeQuery(SQL);

                List<String> itemSizes = new ArrayList<>();
                while (rst.next()) {
                    itemSizes.add(rst.getString("size"));
                }

                selectItemSize.getItems().setAll(itemSizes);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void loadCmb(){
        ObservableList<String> obs = FXCollections.observableArrayList("Cash", "Card","Bank Transfer","Other");
        selectPayment.setItems(obs);
    }
    private void setupItemSizeActionEvent() {
        selectItemSize.setOnAction(event -> {
            String selectedItemType = selectItemType.getValue();
            String selectedSize = selectItemSize.getValue();
            String SQL = "SELECT item_id, description, qty, selling_price FROM item WHERE type = ? AND size = ?";

            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement pstm = connection.prepareStatement(SQL);
                pstm.setString(1, selectedItemType);
                pstm.setString(2, selectedSize);
                ResultSet resultSet = pstm.executeQuery();

                if (resultSet.next()) {
                    String itemID = resultSet.getString("item_id");
                    String itemDescription = resultSet.getString("description");
                    int itemQty = resultSet.getInt("qty");
                    double itemSellingPrice = resultSet.getDouble("selling_price");

                    txtItemId.setText(itemID);
                    txtItemDescription.setText(itemDescription);
                    txtItemQty.setText(Integer.toString(itemQty));
                    txtSellingPrice.setText(Double.toString(itemSellingPrice));
                } else {
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    void btnAddtoCart(ActionEvent event) {
        String orderId = lblOrderId.getText();
        String empId = selectEmpId.getValue();
        String cusName = txtCusName.getText();
        String cusContact = txtCusContact.getText();
        String cusEmail = txtCusEmail.getText();
        String orderDate = txtdate.getValue().toString();
        String payment = selectPayment.getValue();
        String itemId = txtItemId.getText();
        String description = txtItemDescription.getText();
        String itemType = selectItemType.getValue();
        String itemSize = selectItemSize.getValue();
        int itemQty = Integer.parseInt(txtItemQty.getText());
        int qtyOnHand = Integer.parseInt(TxtItemQtyonHand.getText());
        double sellingPrice = Double.parseDouble(txtSellingPrice.getText());
        double discount = Double.parseDouble(txtDiscount.getText());




    }
    private  void calculateTotal(){
        double ttl = 0;
        for (Order tm: cartList){
            ttl+=tm.getGrand_total();
        }
        txtGrandTotal.setText(ttl+"0 /= ");
    }
    ObservableList<Order> cartList = FXCollections.observableArrayList();
    @FXML
    void btnCalculateFinal(ActionEvent event) {
        double discount = Double.parseDouble(txtDiscount.getText());
        double sellingPrice = Double.parseDouble(txtSellingPrice.getText());
        double discountPrice = (discount*sellingPrice)/100;
        double finalPrice = sellingPrice-discountPrice;
        txtFinalPrice.setText(String.valueOf(finalPrice));
    }

    @FXML
    void btnConfirm(ActionEvent event) {

    }

    @FXML
    void btnPlaceOrder(ActionEvent event) {

    }

    @FXML
    void btnRemoveOrder(ActionEvent event) {

    }

    @FXML
    void btnUpdate(ActionEvent event) {

    }




}

