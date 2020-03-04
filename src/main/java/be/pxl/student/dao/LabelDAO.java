package be.pxl.student.dao;

import be.pxl.student.entity.Label;

import java.sql.*;

public class LabelDAO {
    private static final String SELECT_BY_ID = "SELECT * FROM Label WHERE id = ?";
    private static final String UPDATE = "UPDATE Label SET name=?, description=? WHERE id = ?";
    private static final String INSERT = "INSERT INTO Label (name, description) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM Label WHERE id = ?";
    private String url;
    private String user;
    private String password;

    public LabelDAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }


    public Label createLabel(Label label) {

        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, label.getName());
            stmt.setString(2, label.getDescription());
            if (stmt.executeUpdate() == 1) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        label.setId(rs.getInt(1));
                        return label;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean updateLabel(Label label) {
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setString(1, label.getName());
            stmt.setString(2, label.getDescription());
            stmt.setInt(3, label.getId());
            return stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteLabel(int id) {
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            return stmt.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Label readLabel(int id) {
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapLabel(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Label mapLabel(ResultSet rs) throws SQLException {
        Label label = new Label();
        label.setName(rs.getString("name"));
        label.setDescription(rs.getString("description"));
        label.setId(rs.getInt("id"));
        return label;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);

    }
}
