package com.oltpbenchmark.util;

import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

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
