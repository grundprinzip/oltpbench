package com.oltpbenchmark.benchmarks.hyrise.procedures;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.sourceforge.jtds.jdbc.DateTime;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.distributions.ZipfianGenerator;
import com.oltpbenchmark.util.FileUtil;
import com.oltpbenchmark.util.TextGenerator;
import com.oltpbenchmark.util.TimeUtil;

public class LoadProc extends Procedure {

    public static final int AVG_CONNECTIONS = 60;

    public static SQLStmt TABLE = null;

    public static final SQLStmt INSERT = new SQLStmt(
            "{\"operators\":{\"get\":{\"type\":\"GetTable\",\"name\":\"%s\"},\"insert\":{\"type\":\"InsertScan\",\"data\":[%s]}},\"edges\":[[\"get\",\"insert\"]]}");

    // Create the Indices
    public static SQLStmt INDEX = null;

    // Merge the table
    public static SQLStmt MERGE = null;

    public static SQLStmt UNLOAD = null;

    static {
        TABLE = new SQLStmt(FileUtil.readFile("src/com/oltpbenchmark/benchmarks/hyrise/schema.json"));
        INDEX = new SQLStmt(FileUtil.readFile("src/com/oltpbenchmark/benchmarks/hyrise/create_index.json"));
        MERGE = new SQLStmt(FileUtil.readFile("src/com/oltpbenchmark/benchmarks/hyrise/merge_table.json"));
        UNLOAD = new SQLStmt(FileUtil.readFile("src/com/oltpbenchmark/benchmarks/hyrise/unload.json"));
    }

    Random r = new Random();

    String generateRow(int userid) {
        StringBuilder buf = new StringBuilder();
        buf.append(userid);
        buf.append(", ");

        // Username
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "pw",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "fname",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "lname",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "gender",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "dob",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "jdate",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "ldate",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "address",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "email",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "tel",
        buf.append("\"" + TextGenerator.randomStrSafe(r, 20) + "\"");
        buf.append(", ");

        // "tpic",
        buf.append("\"\"");
        buf.append(", ");

        // "pic"
        buf.append("\"\"");

        return buf.toString();
    }

    public void run(Connection conn, int id, double scaleFactor) throws SQLException {
        conn.setAutoCommit(true);
        PreparedStatement stmt = this.getPreparedStatement(conn, UNLOAD);
        stmt.executeUpdate();

        stmt = this.getPreparedStatement(conn, TABLE);
        stmt.executeUpdate();

        // Insert some rows
        int base = (int) (100000 * scaleFactor);

        // Insert Users
        int stepping = 30000;
        int batches = (base / stepping) + 1;
        for (int batch = 0; batch < batches; ++batch) {
            int lower = batch * stepping;
            int upper = (lower + stepping) > base ? base : lower + stepping;

            // Perform inserts batchwise
            stmt = this.getPreparedStatement(conn, INSERT);
            StringBuilder buf = new StringBuilder();
            for (int i = lower; i < upper; ++i) {
                buf.append("[");
                buf.append(generateRow(i));
                buf.append("]");
                if ((i + 1) < upper) {
                    buf.append(",\n");
                }
            }

            stmt.setString(0, "users");
            stmt.setString(1, buf.toString());
            stmt.setMaxRows(1);
            stmt.execute();
        }

        // Insert Friendships
        // Given base number of users generate base * AVG_CONNECTIONS
        // connections
        ZipfianGenerator zipf = new ZipfianGenerator(AVG_CONNECTIONS);

        HashMap<Long, HashSet<Long>> connections = new HashMap<Long, HashSet<Long>>();

        for (long u = 0; u < base; ++u) {
            connections.put(u, new HashSet<Long>());
        }

        StringBuilder buffer = new StringBuilder();

        // For each user build connections
        for (long u = 0; u < base; ++u) {

            // Get correct number of friends for this user
            long nextConnCount = zipf.nextLong();
            for (long i = 0; i < nextConnCount; ++i) {

                // Find an arbitrary connections
                long other = r.nextInt(base);
                // Ignore this connection if they are already connected
                while (other == u || connections.get(u).contains(other) || connections.get(other).contains(u)) {
                    other = r.nextInt(base);
                }

                connections.get(u).add(other);
                connections.get(other).add(u);

                // Found and insert
                buffer.append(String.format(",[%s,%s,%s]", u, other, 1));
                buffer.append(String.format(",[%s,%s,%s]", other, u, 1));
            }

            // More than 500kb
            if (buffer.length() > (500 * 1024)) {
                stmt = this.getPreparedStatement(conn, INSERT);
                stmt.setString(0, "friendships");
                stmt.setString(1, buffer.substring(1));
                stmt.setMaxRows(1);
                stmt.execute();
                buffer = new StringBuilder();
            }

        }

        // Rest of the family
        if (buffer.length() > 0) {
            stmt = this.getPreparedStatement(conn, INSERT);
            stmt.setString(0, "friendships");
            stmt.setString(1, buffer.substring(1));
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
