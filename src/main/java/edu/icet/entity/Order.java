package edu.icet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String OrderId;
    private String EMPID;
    private String CusName;
    private String CusContact;
    private String CusEmail;
    private String OrderDate;
    private String Payment;
    private String item_id;
    private String description;
    private String type;
    private String size;
    private int qty;
    private int qtyOnHand;
    private double selling_price;
    private double discount;
    private double discount_price;
    private double final_price;
    private double total_discount;
    private double grand_total;
}
