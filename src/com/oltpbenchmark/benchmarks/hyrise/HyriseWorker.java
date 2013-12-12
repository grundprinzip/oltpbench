package com.oltpbenchmark.benchmarks.hyrise;

import java.sql.SQLException;

import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.hyrise.procedures.HyriseProcedure;
import com.oltpbenchmark.benchmarks.hyrise.procedures.NoOpProc;
import com.oltpbenchmark.types.TransactionStatus;

public class HyriseWorker extends Worker {
    
    public static final int USERS = 1000;
    

    public HyriseWorker(BenchmarkModule benchmarkModule, int id) {
        super(benchmarkModule, id);
    }

    @Override
    protected TransactionStatus executeWork(TransactionType txnType) throws UserAbortException, SQLException {

        HyriseProcedure proc = (HyriseProcedure) this.getProcedure(txnType.getProcedureClass());
        if (proc != null) {
            proc.run(getConnection(), USERS);
        } else {
            throw new UserAbortException("TX Type not Supported");
        }

        return TransactionStatus.SUCCESS;
    }

}
