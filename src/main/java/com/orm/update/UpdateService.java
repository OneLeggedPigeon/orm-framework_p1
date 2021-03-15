package com.orm.update;


import java.sql.ResultSet;
import java.sql.SQLException;

public  class UpdateService {

    /*
    private static UpdateService instance;

    // Load EVERYTHING from the database
    private UpdateService(){
        updateAll();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static UpdateService getInstance() {
        if (instance == null) {
            instance = new UpdateService();
        }
        return instance;
    }

    // TODO: set everything local to null first, then make this public
    // TODO: perhaps also separate out each of the updates?
    private void updateAll(){
        System.out.println("Loading Users");
        ResultSet logRS = SQLQueryService.query("select * from login");
        ResultSet empRS = SQLQueryService.query("select login.user_id from employee, login where employee.user_id = login.user_id");
        ResultSet cusRS = SQLQueryService.query("select login.user_id from customer, login where customer.user_id = login.user_id;");
        // I could make sure no one is a customer and employee, but that's out of scope for MVP

        System.out.println("Loading Cars");
        ResultSet carRS = SQLQueryService.query("select * from car");

        System.out.println("Loading Offers");
        ResultSet offRS = SQLQueryService.query("select * from offer");

        System.out.println("Loading Loans");
        ResultSet loanRS = SQLQueryService.query("select * from loan");

        try {
            assert logRS != null;
            assert empRS != null;
            assert cusRS != null;
            boolean empValid = empRS.next();
            boolean cusValid = cusRS.next();
            // Users
            while(logRS.next()){
                int id = logRS.getInt("user_id");
                if(empValid && id == empRS.getInt("user_id")){
                    UserService.loadEmployee(id,logRS.getString("username"),logRS.getString("password"));
                    empValid = empRS.next();
                } else if (cusValid && id == cusRS.getInt("user_id")){
                    UserService.loadCustomer(id,logRS.getString("username"),logRS.getString("password"));
                    cusValid = cusRS.next();
                } else {
                    UserService.loadUser(id,logRS.getString("username"),logRS.getString("password"));
                }
            }
            // Cars
            assert carRS != null;
            while (carRS.next()){
                CarService.loadCar(
                        carRS.getInt("car_id"),
                        carRS.getInt("user_id"),
                        carRS.getBoolean("in_lot"),
                        carRS.getString("model")
                );
            }
            // Offers
            assert offRS != null;
            while (offRS.next()){
                OfferService.loadOffer(
                        offRS.getInt("offer_id"),
                        offRS.getInt("user_id"),
                        offRS.getInt("car_id"),
                        offRS.getInt("amount")
                );
            }
            // Loans
            assert loanRS != null;
            while (loanRS.next()){
                LoanService.loadLoan(
                        loanRS.getInt("loan_id"),
                        loanRS.getInt("user_id"),
                        loanRS.getInt("car_id"),
                        loanRS.getInt("remaining_terms"),
                        loanRS.getFloat("monthly_due"),
                        loanRS.getInt("principle")
                );
            }
        } catch (SQLException e) {
            System.out.println("Loading Error");
            e.printStackTrace();
            System.exit(0);
        }
    }

     */
}
