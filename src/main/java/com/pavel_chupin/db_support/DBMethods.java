package com.pavel_chupin.db_support;

import java.sql.SQLException;
import java.util.List;

public interface DBMethods {

    void initData(long n) throws SQLException;

    List<Data> selectData() throws SQLException;

    void initDataMass(long n) throws SQLException;
}
