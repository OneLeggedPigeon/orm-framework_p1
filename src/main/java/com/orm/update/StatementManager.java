package com.orm.update;

import java.sql.Connection;

public abstract class StatementManager {


    public static void createLoan(int loanId, int customerId, int carId, int terms, double amount, int principle) {
        /*
        try (ConnectionSession ses = new ConnectionSession()) {
            Connection conn = ses.getActiveConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement(
                    new PreparedInsert().text;
            );
            ps.setInt(1, loanId);
            ps.setInt(2, customerId);
            ps.setInt(3, carId);
            ps.setInt(4, terms);
            ps.setFloat(5, (float) amount);
            ps.setInt(6, principle);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

         */
    }
}