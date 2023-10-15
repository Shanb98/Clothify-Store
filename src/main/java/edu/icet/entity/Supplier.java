package edu.icet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    private String supID;
    private String supName;
    private String supContactNumber;
    private String supCompany;
    private String title;
}
