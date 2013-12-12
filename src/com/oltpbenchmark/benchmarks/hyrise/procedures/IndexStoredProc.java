package com.oltpbenchmark.benchmarks.hyrise.procedures;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public class IndexStoredProc extends HyriseProcedure {

    Random r = new Random();
    
    @Override
    public void runQuery(Connection conn, int id) throws SQLException {
        
        CallableStatement cb = conn.prepareCall("/proc/indexscan\nlimit=1&table=users&index=users_idx&value=" + r.nextInt(id) );
        cb.executeQuery();

    }

}
