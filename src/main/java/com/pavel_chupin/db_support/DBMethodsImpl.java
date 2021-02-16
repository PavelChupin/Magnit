package com.pavel_chupin.db_support;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBMethodsImpl implements DBMethods {
    Connection connection;

    public DBMethodsImpl(String url, String user, String pass) throws SQLException, ClassNotFoundException {
        this.connection = ConnectDB.getConnection(url, user, pass);
    }

    @Override
    public void initDataMass(long n) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("insert test (field) values(?)")) {
            int rowcount = st.executeUpdate("delete from test");

            for (int i = 0; i < n; i++) {
                st.setLong(1, i);
                st.addBatch();
            }
            st.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }

    @Override
    public void initData(long n) throws SQLException {
        try (Statement st = connection.createStatement()) {
            int rowcount = st.executeUpdate("delete from test");
            for (int i = 0; i < n; i++) {
                st.addBatch(String.format("insert test (field) values(%d)", i + 1));
            }
            st.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            //throw e;
        }
    }

    @Override
    public List<Data> selectData() throws SQLException {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("select field from test")) {
            //ResultSet rs = st.executeQuery("select field from test");
            List<Data> d = new ArrayList<>();
            while (rs.next()) {
                d.add(new Data(rs.getLong("field")));
            }
            return d;
        }
    }
}
