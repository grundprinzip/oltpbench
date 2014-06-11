java -Xmx8024m -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_0.xml --create=true --load=true  --execute=true
java -Xmx8024m -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_2.xml --create=true --load=true  --execute=true
java -Xmx8024m -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_10.xml --create=true --load=true  --execute=true
java -Xmx8024m -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_50.xml --create=true --load=true  --execute=true
java -Xmx8024m -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_100.xml --create=true --load=true  --execute=true