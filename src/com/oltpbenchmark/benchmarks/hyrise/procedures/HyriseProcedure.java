package com.oltpbenchmark.benchmarks.hyrise.procedures;

import java.sql.Connection;
import java.sql.SQLException;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.util.FileUtil;

public abstract class HyriseProcedure extends Procedure {

    public static SQLStmt query = null;
    
    SQLStmt prepare() {
         String name = "src/com/oltpbenchmark/benchmarks/hyrise/"+ getClass().getSimpleName() +".json";
         if (FileUtil.exists(name))
             return query = new SQLStmt(FileUtil.readFile(name));
         else 
             return null;
    }
        
    public final void run(Connection conn, int id) throws SQLException {
        prepare();
        runQuery(conn, id);
    }
    
    public abstract void runQuery(Connection conn, int id) throws SQLException;
    
    
    
    
}
