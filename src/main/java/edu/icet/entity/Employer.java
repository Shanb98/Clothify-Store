package edu.icet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employer {
    private String EMPID;
    private String EMPName;
    private String EMPNic;
    private String EMPAddress;
    private String EMPDob;
    private String EMPContact;
    private String EMPBankAcNo;
    private String EMPBranch;
    private String EMPTitle;
}
