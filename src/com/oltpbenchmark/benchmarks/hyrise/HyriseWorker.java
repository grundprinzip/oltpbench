package com.oltpbenchmark.benchmarks.hyrise;

import java.sql.SQLException;

import com.oltpbenchmark.api.BenchmarkModule;
import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.Procedure.UserAbortException;
import com.oltpbenchmark.api.TransactionType;
import com.oltpbenchmark.api.Worker;
import com.oltpbenchmark.benchmarks.hyrise.procedures.NoOpProc;
import com.oltpbenchmark.types.TransactionStatus;

public class HyriseWorker extends Worker {

    public HyriseWorker(BenchmarkModule benchmarkModule, int id) {
        super(benchmarkModule, id);
    }

    @Override
    protected TransactionStatus executeWork(TransactionType txnType) throws UserAbortException, SQLException {

        Class<? extends Procedure> procClass = txnType.getProcedureClass();

        if (procClass.equals(NoOpProc.class)) {
            NoOpProc proc = this.getProcedure(NoOpProc.class);
            assert (proc != null);
            proc.run(getConnection(), getId());
        } else {
            throw new UserAbortException("TX Type not Supported");
        }

        return TransactionStatus.SUCCESS;
    }

}
