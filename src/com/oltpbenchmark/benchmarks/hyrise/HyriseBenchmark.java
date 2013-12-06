package com.oltpbenchmark.benchmarks.hyrise;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.oltpbenchmark.WorkloadConfiguration;
import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Loader;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.hyrise.procedures.NoOpProc;

public class HyriseBenchmark extends BenchmarkModule {

    public HyriseBenchmark(WorkloadConfiguration workConf) {
        super("hyrise", workConf, true);
    }
    
    
    @Override
    protected List<Worker> makeWorkersImpl(boolean verbose) throws IOException {
        ArrayList<Worker> workers = new ArrayList<Worker>();
        for (int i = 0; i < workConf.getTerminals(); ++i) {
            workers.add(new HyriseWorker(this, i));
        }
        return workers;
    }

    @Override
    protected Loader makeLoaderImpl(Connection conn) throws SQLException {
        return new HyriseLoader(this, conn);
    }

    @Override
    protected Package getProcedurePackageImpl() {
        // TODO Auto-generated method stub
        return NoOpProc.class.getPackage();
    }

}
