package models.impl;

import db.DBConnection;
import dto.OrderDetailsDto;
import models.OrderDetailsModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailsModelImpl implements OrderDetailsModel {
    @Override
    public boolean saveOrderDetails(List<OrderDetailsDto> list) throws SQLException, ClassNotFoundException {

        boolean isDetailsSaved = true;
        for (OrderDetailsDto dto:list) {
            String sql = "INSERT INTO orderdetail VALUES(?,?,?,?)";
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setString(1,dto.getOrderId());
            pstm.setString(2,dto.getItemCode());
            pstm.setInt(3,dto.getQty());
            pstm.setDouble(4,dto.getUnitPrice());

            if(!(pstm.executeUpdate()>0)){
                isDetailsSaved = false;
            }
        }
        return isDetailsSaved;
    }

    @Override
    public boolean updateQty(List<OrderDetailsDto> list) throws SQLException, ClassNotFoundException {
        String sql2 = "SELECT qtyOnHand FROM item WHERE code = ?";
        String sql = "UPDATE item SET qtyOnHand = ? WHERE code = ?";

        for (OrderDetailsDto dto : list) {
            try (PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql2);
                 PreparedStatement pstmt = DBConnection.getInstance().getConnection().prepareStatement(sql)) {

                pstm.setString(1, dto.getItemCode());
                ResultSet resultSet = pstm.executeQuery();

                if (resultSet.next()) {
                    int currentQtyOnHand = resultSet.getInt(1);
                    pstmt.setInt(1, currentQtyOnHand - dto.getQty());
                    pstmt.setString(2, dto.getItemCode());
                    pstmt.executeUpdate();
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return true; // Return true if the update was successful
    }


}
