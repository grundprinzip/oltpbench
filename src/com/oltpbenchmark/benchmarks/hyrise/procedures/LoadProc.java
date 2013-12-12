package com.oltpbenchmark.benchmarks.hyrise.procedures;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Random;

import net.sourceforge.jtds.jdbc.DateTime;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.util.FileUtil;
import com.oltpbenchmark.util.TimeUtil;

public class LoadProc extends Procedure {

    public static final SQLStmt TABLE = new SQLStmt("{ " + "\"operators\" : " + "{ \"load_mani\" : " + "{  \"type\" : \"JsonTable\",  "
            + " \"names\" : [\"mid\", \"creatorid\", \"rid\", \"modifierid\", \"timestamp\", \"type\", \"content\"],  "
            + "\"types\" : [\"INTEGER\", \"INTEGER\", \"INTEGER\", \"INTEGER\", \"INTEGER\", \"STRING\", \"STRING\"], " + "\"serials\": [\"mid\"], \"groups\" : [1,1,1,1,1,1,1], \"useStore\": true "
            + "}," + "  \"set_mani\" : " + "{ \"type\" : \"SetTable\", \"name\": \"manipulations\" }}, " + "\"edges\" : [[\"load_mani\", \"set_mani\"]]}");

    public static final SQLStmt INSERT = new SQLStmt(
            "{\"operators\":{\"get\":{\"type\":\"GetTable\",\"name\":\"manipulations\"},\"insert\":{\"type\":\"InsertScan\",\"data\":[%s]}},\"edges\":[[\"get\",\"insert\"]]}");
    
    // Create the Indices
    public static SQLStmt INDEX = null;
    
    // Merge the table
    public static SQLStmt MERGE = null;
    
    public static SQLStmt UNLOAD = null;
    
    static {
        INDEX = new SQLStmt(FileUtil.readFile("src/com/oltpbenchmark/benchmarks/hyrise/create_index.json"));
        MERGE = new SQLStmt(FileUtil.readFile("src/com/oltpbenchmark/benchmarks/hyrise/merge_table.json"));
        UNLOAD= new SQLStmt(FileUtil.readFile("src/com/oltpbenchmark/benchmarks/hyrise/unload.json"));
    }

    Random r = new Random();

    String generateRow(int creator, int resource) {
        StringBuilder buf = new StringBuilder();
        buf.append(creator);
        buf.append(", ");
        buf.append(resource);
        buf.append(", ");
        buf.append(0);
        buf.append(", ");

        buf.append(TimeUtil.getCurrentTime().getTime());
        buf.append(", ");
        buf.append("\"");
        buf.append("Commment");
        buf.append("\"");
        buf.append(", ");
        buf.append("\"");
        buf.append("This is really a comment" + r.nextInt(1000));
        buf.append("\"");        
        return buf.toString();
    }

    public void run(Connection conn, int id, double scaleFactor) throws SQLException {
        conn.setAutoCommit(true);
        PreparedStatement stmt = this.getPreparedStatement(conn, UNLOAD);
        stmt.executeUpdate();
        
        stmt = this.getPreparedStatement(conn, TABLE);
        stmt.executeUpdate();

        int maxCre = 1000;
        int maxRes = 1000;

        // Insert some rows
        int base = (int) (10000 * scaleFactor);
        

        int stepping = 1000;
        int batches = (base / stepping) + 1;
        for (int batch = 0; batch < batches; ++batch) {
            int lower = batch * stepping;
            int upper = (lower + stepping) > base ? base : lower + stepping;
            
            // Perform inserts batchwise
            stmt = this.getPreparedStatement(conn, INSERT);
            StringBuilder buf = new StringBuilder();
            for (int i = lower; i < upper; ++i) {
                buf.append("[");
                buf.append(generateRow(r.nextInt(maxCre), r.nextInt(maxRes)));
                buf.append("]");
                if ((i + 1) < upper) {
                    buf.append(",\n");
                }
            }
            
            stmt.setString(0, buf.toString());
            stmt.setMaxRows(1);
            stmt.execute();
        }
        
        
        stmt = this.getPreparedStatement(conn, MERGE);
        stmt.setMaxRows(1);
        stmt.execute();
        
        stmt = this.getPreparedStatement(conn, INDEX);
        stmt.setMaxRows(1);
        stmt.execute();
        
        
        
    }

}
