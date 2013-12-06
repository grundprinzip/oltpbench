package com.oltpbenchmark.benchmarks.hyrise;

import java.sql.Connection;
import java.sql.SQLException;

import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;

public class HyriseLoader extends Loader {

    public HyriseLoader(BenchmarkModule benchmark, Connection conn) {
        super(benchmark, conn);
    }

    @Override
    public void load() throws SQLException {
    }

}
