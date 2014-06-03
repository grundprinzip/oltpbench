package com.oltpbenchmark.util;

import com.oltpbenchmark.benchmarks.tpcc.TPCCUtil;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class captures optional schema modifcation information for each benchmark.
 * <p/>
 * In detail, the goal is to provide a system, that allows to modify the underlying schema without changing the queries.
 * This can include actions as adding additional columns or changing the distribution of a single column.
 * <p/>
 * Created by grund on 03.06.14.
 */
public class SchemaConfiguration {

    // Number of columns to extend the schema
    public int getExtendSchemaBy() {
        return extendSchemaBy;
    }

    int extendSchemaBy = 0;


    /**
     * Build the column names from the extended Schema Variable
     * @return list of columns to append
     */
    public List<String> extendedColumnNames() {
        ArrayList<String> cols = new ArrayList<>();
        for(int i=0; i < extendSchemaBy; ++i) {
            cols.add("extended_col_"+i);
        }
        return cols;
    }


    public void extendPreparedStatement(PreparedStatement ps, int offset) throws SQLException {
        for (int i = 0; i < extendSchemaBy; i++) {
            ps.setString(offset + i, TPCCUtil.randomNStr(24));
        }
    }

    public String extendByString() {
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < extendSchemaBy; i++) {
            bld.append(TPCCUtil.randomNStr(24));
            if (i < extendSchemaBy - 1) {
                bld.append(";");
            }
        }
        return bld.toString();
    }

    /**
     * Private constructor only use builder
     */
    private SchemaConfiguration() {

    }


    public static SchemaConfiguration build(SubnodeConfiguration config) {
        SchemaConfiguration result = new SchemaConfiguration();

        // Check for all kinds of configuration values that can appear
        result.extendSchemaBy = config.getInt("extendBy", 0);

        return result;
    }

}
