package edu.icet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String item_id;
    private String description;
    private int qty;
    private double buying_price;
    private double selling_price;
    private String type;
    private String size;
    private double profit;
    private String supplier_id;
}
