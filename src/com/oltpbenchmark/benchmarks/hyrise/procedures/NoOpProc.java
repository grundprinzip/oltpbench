package com.oltpbenchmark.benchmarks.hyrise.procedures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;

public class NoOpProc extends Procedure {

    public final static SQLStmt NOP = new SQLStmt("{\"priority\":10,\"performance\": false,\"operators\":{\"0\":{\"type\": \"NoOp\"}},\"edges\": [[\"0\", \"0\"]]}");
    
    public void run(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = this.getPreparedStatement(conn, NOP);
        stmt.executeUpdate();
    }
    
}
