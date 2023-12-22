package models;

import java.sql.SQLException;

public interface PlaceOrderModel {
    int checkValidQty(String x) throws SQLException, ClassNotFoundException;
}
