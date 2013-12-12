package com.oltpbenchmark.benchmarks.hyrise.procedures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class IndexScan extends HyriseProcedure {
    
    Random r = new Random();
    
    @Override
    public void runQuery(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = this.getPreparedStatement(conn, query);
        stmt.setInt(0, r.nextInt(id));
        stmt.setMaxRows(1);
        stmt.executeUpdate();                
    }

}
