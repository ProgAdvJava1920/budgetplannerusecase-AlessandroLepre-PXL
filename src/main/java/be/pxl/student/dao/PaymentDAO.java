package be.pxl.student.dao;

import be.pxl.student.entity.Payment;

import java.sql.*;

public class PaymentDAO {
    private static final String SELECT_BY_ID = "SELECT * FROM Payment WHERE id = ?";
    private static final String UPDATE = "UPDATE Payment SET date=?, amount=?, currency=?, detail=? WHERE id = ?";
    private static final String INSERT = "INSERT INTO Payment (dates, amount, currency, detail) VALUES (?, ?, ?, ?)";
    private static final String DELETE = "DELETE FROM Payment WHERE id = ?";
    private String url;
    private String user;
    private String password;

    public PaymentDAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }


    public Payment createPayment(Payment payment) {

        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, (Date) payment.getDate());      //Gecast naar "Date" was oorsprongkelijk niet.
            stmt.setFloat(2, payment.getAmount());
            stmt.setString(3, payment.getCurrency());
            stmt.setString(4, payment.getDetail());
            if (stmt.executeUpdate() == 1) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        payment.setId(rs.getInt(1));
                        return payment;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean updatePayment(Payment payment) {
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setDate(1, (Date) payment.getDate());      //Gecast naar "Date" was oorsprongkelijk niet.
            stmt.setFloat(2, payment.getAmount());
            stmt.setString(3, payment.getCurrency());
            stmt.setString(4, payment.getDetail());
            return stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deletePayment(int id) {
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            return stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Payment readPayment(int id) {
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapAccount(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Payment mapAccount(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setAmount(rs.getFloat("amount"));
        payment.setCurrency(rs.getString("currency"));
        payment.setDate(rs.getDate("date"));
        payment.setDetail(rs.getString("detail"));
        return payment;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);

    }
}
