package edu.icet.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.db.DBConnection;
import edu.icet.entity.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class DashItemController implements Initializable {

    @FXML
    private JFXTextField txtItemCode;

    @FXML
    private JFXTextField txtItemDescription;

    @FXML
    private JFXTextField txtProfit;

    @FXML
    private TableView<Item> tblItem;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colItemDescription;

    @FXML
    private TableColumn<?, ?> colItemQty;

    @FXML
    private TableColumn<?, ?> colSellingPrice;

    @FXML
    private TableColumn<?, ?> colBuyingPrice;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableColumn<?, ?> colSize;

    @FXML
    private TableColumn<?, ?> colProfit;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtBuyingPrice;

    @FXML
    private JFXComboBox<String> SelectType;

    @FXML
    private JFXComboBox<String> selectSize;

    @FXML
    private TextField txtSearchItem;

    @FXML
    private JFXTextField selectSupplier;
    @FXML
    private JFXTextField txtSellingPrice;

    @FXML
    private JFXComboBox<String> SelectSupId;
    private Map<String, ObservableList<String>> sizeMap = new HashMap<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadSupplierIds(); // Corrected method name
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        generateId();
        loadCmb();
        initializeSizeMap();
        SelectSupId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierName();
            }
        });

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("item_id"));
        colItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory<>("selling_price"));
        colBuyingPrice.setCellValueFactory(new PropertyValueFactory<>("buying_price"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colProfit.setCellValueFactory(new PropertyValueFactory<>("profit"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplier_id"));

        loadItemTable();
    }
    public void loadCmb() {
        ObservableList<String> clothingTypes = FXCollections.observableArrayList(
                "T-shirts",
                "Jeans/Shorts",
                "Dresses",
                "Suits",
                "Sweaters",
                "Jackets",
                "Skirts",
                "Shoes",
                "Accessories",
                "Other"
        );

        SelectType.setItems(clothingTypes);

        // Add an event listener to handle changes in the selected clothing type
        SelectType.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSelectSize(newValue);
        });
    }
    public void loadItemTable() {
        ObservableList<Item> itemList = FXCollections.observableArrayList();
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM item");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Item item = new Item(
                        resultSet.getString("item_id"),
                        resultSet.getString("description"),
                        resultSet.getInt("qty"),
                        resultSet.getDouble("buying_price"),
                        resultSet.getDouble("selling_price"),
                        resultSet.getString("type"),
                        resultSet.getString("size"),
                        resultSet.getDouble("profit"),
                        resultSet.getString("supplier_id")
                );
                itemList.add(item);
            }
            tblItem.setItems(itemList);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Initialize the sizeMap with sizes for each clothing type
    private void initializeSizeMap() {
        // Add sizes for T-shirts
        ObservableList<String> tShirtSizes = FXCollections.observableArrayList(
                "Small", "Medium", "Large", "X-Large"
        );
        sizeMap.put("T-shirts", tShirtSizes);

        // Add sizes for Jeans
        ObservableList<String> jeansSizes = FXCollections.observableArrayList(
                "28", "30", "32", "34", "36"
        );
        sizeMap.put("Jeans/Shorts", jeansSizes);

        // Add sizes for Dresses
        ObservableList<String> dressSizes = FXCollections.observableArrayList(
                "Small", "Medium", "Large", "X-Large"
        );
        sizeMap.put("Dresses", dressSizes);

        // Add sizes for Suits
        ObservableList<String> suitSizes = FXCollections.observableArrayList(
                "36R", "38R", "40R", "42R", "44R"
        );
        sizeMap.put("Suits", suitSizes);

        // Add sizes for Sweaters
        ObservableList<String> sweaterSizes = FXCollections.observableArrayList(
                "Small", "Medium", "Large", "X-Large"
        );
        sizeMap.put("Sweaters", sweaterSizes);

        // Add sizes for Jackets
        ObservableList<String> jacketSizes = FXCollections.observableArrayList(
                "Small", "Medium", "Large", "X-Large"
        );
        sizeMap.put("Jackets", jacketSizes);

        // Add sizes for Skirts
        ObservableList<String> skirtSizes = FXCollections.observableArrayList(
                "Small", "Medium", "Large", "X-Large"
        );
        sizeMap.put("Skirts", skirtSizes);

        // Add sizes for Shorts


        // Add sizes for Shoes
        ObservableList<String> shoesSizes = FXCollections.observableArrayList(
                "6", "7", "8", "9", "10"
        );
        sizeMap.put("Shoes", shoesSizes);

        // Add sizes for Accessories
        ObservableList<String> accessorySizes = FXCollections.observableArrayList(
                "One Size"
        );
        sizeMap.put("Accessories", accessorySizes);

        // Add sizes for Other
        ObservableList<String> otherSizes = FXCollections.observableArrayList(
                "Custom", "N/A"
        );
        sizeMap.put("Other", otherSizes);
    }

    // Update the selectSize ComboBox based on the selected clothing type
    private void updateSelectSize(String clothingType) {
        ObservableList<String> sizes = sizeMap.get(clothingType);
        if (sizes != null) {
            selectSize.setItems(sizes);
        } else {
            selectSize.getItems().clear(); // Clear the items if no sizes are available
        }
    }
    public List<String> getSupplierIds() throws SQLException, ClassNotFoundException {
        List<String> ids = new ArrayList<>();
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM supplier").executeQuery();
        while (rst.next()) {
            ids.add(rst.getString(1));
        }
        return ids;
    }

    private void loadSupplierIds() throws SQLException, ClassNotFoundException {
        List<String> supplierIds = new DashItemController().getSupplierIds();
        SelectSupId.getItems().addAll(supplierIds);
    }

    private void loadSupplierName() {
        String selectedSupplierId = SelectSupId.getValue();
        if (selectedSupplierId != null) {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                Statement stm = connection.createStatement();
                String SQL = "SELECT supplier_name FROM supplier WHERE id = '" + selectedSupplierId + "'";
                ResultSet rst = stm.executeQuery(SQL);

                if (rst.next()) {
                    String supplierName = rst.getString("supplier_name");
                    selectSupplier.setText(supplierName);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Supplier not found").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void btnAddStock(ActionEvent event) {
        // Get the data from the input fields
        String itemCode = txtItemCode.getText();
        String itemDescription = txtItemDescription.getText();
        int itemQty = Integer.parseInt(txtQty.getText());
        double buyingPrice = Double.parseDouble(txtBuyingPrice.getText());
        double sellingPrice = Double.parseDouble(txtSellingPrice.getText());
        String itemType = SelectType.getValue();
        String itemSize = selectSize.getValue();
        double profit = Double.parseDouble(txtProfit.getText());
        String supplierId = SelectSupId.getValue();

        // Create an Item object with the data
        Item item = new Item(itemCode, itemDescription, itemQty, buyingPrice, sellingPrice, itemType, itemSize, profit, supplierId);

        // Save the item to the database
        if (saveItemToDatabase(item)) {
            new Alert(Alert.AlertType.INFORMATION,"Supplier Saved..!").show();
            // Clear the input fields
            clearInputFields();
            // Refresh the table view or loadTable() to update the displayed data
            loadItemTable();
        } else {
            new Alert(Alert.AlertType.ERROR, "Failed to save item").show();
        }
    }

    private boolean saveItemToDatabase(Item item) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO item (item_id, description, qty, buying_price, selling_price, type, size, profit, supplier_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            preparedStatement.setString(1, item.getItem_id());
            preparedStatement.setString(2, item.getDescription());
            preparedStatement.setInt(3, item.getQty());
            preparedStatement.setDouble(4, item.getBuying_price());
            preparedStatement.setDouble(5, item.getSelling_price());
            preparedStatement.setString(6, item.getType());
            preparedStatement.setString(7, item.getSize());
            preparedStatement.setDouble(8, item.getProfit());
            preparedStatement.setString(9, item.getSupplier_id());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearInputFields() {
        txtItemCode.clear();
        txtItemDescription.clear();
        txtQty.clear();
        txtBuyingPrice.clear();
        txtSellingPrice.clear();
        SelectType.setValue(null);
        selectSize.setValue(null);
        txtProfit.clear();
        SelectSupId.setValue(null);
        selectSupplier.clear();
        generateId();
    }


    @FXML
    void btnClear(ActionEvent event) {
        clearInputFields();
    }

    @FXML
    void btnItemSave(ActionEvent event) {

    }

    @FXML
    void btnItemSearch(ActionEvent event) {
        String code = txtSearchItem.getText();

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            String SQL = "SELECT * FROM item WHERE item_id = '" + code + "'";
            ResultSet rst = stm.executeQuery(SQL);

            if (rst.next()) {
                Item item = new Item(
                        rst.getString("item_id"),
                        rst.getString("description"),
                        rst.getInt("qty"),
                        rst.getDouble("buying_price"),
                        rst.getDouble("selling_price"),
                        rst.getString("type"),
                        rst.getString("size"),
                        rst.getDouble("profit"),
                        rst.getString("supplier_id")
                );
                txtItemCode.setText(item.getItem_id());
                txtItemDescription.setText(item.getDescription());
                txtQty.setText(String.valueOf(item.getQty()));
                txtBuyingPrice.setText(String.valueOf(item.getBuying_price()));
                txtSellingPrice.setText(String.valueOf(item.getSelling_price()));
                SelectType.setValue(item.getType());
                selectSize.setValue(item.getSize());
                txtProfit.setText(String.valueOf(item.getProfit()));
                selectSupplier.setText(item.getSupplier_id());
            } else {
                new Alert(Alert.AlertType.ERROR, "Item not found").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPrint(ActionEvent event) {

    }
    public static String getLastSupplierId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT item_id FROM item ORDER BY item_id DESC LIMIT 1");
        return rst.next() ? rst.getString("item_id") : null;
    }

    private void generateId() {
        try {
            String lastSupplierId = getLastSupplierId();
            if (lastSupplierId != null && lastSupplierId.matches("ITM\\d{4}")) {
                int numericPart = Integer.parseInt(lastSupplierId.substring(3)) + 1;
                String newSupplierId = String.format("ITM%04d", numericPart);
                txtItemCode.setText(newSupplierId);
            } else {
                txtItemCode.setText("ITM0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
