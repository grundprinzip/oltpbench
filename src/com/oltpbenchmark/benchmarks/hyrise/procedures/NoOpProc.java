package com.oltpbenchmark.benchmarks.hyrise.procedures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;

public class NoOpProc extends HyriseProcedure {

    @Override
    public void runQuery(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = this.getPreparedStatement(conn, query);
        stmt.executeUpdate();
        
    }
    
}
