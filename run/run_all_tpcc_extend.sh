java -Xmx8024m -cp `run/classpath.sh` -Dlog4j.configuration=./log4j.properties com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_0.xml --create=true --load=true  --execute=true -s 5 -ss -o result/mysql_w10_t20_0_extend_0
java -Xmx8024m -cp `run/classpath.sh` -Dlog4j.configuration=./log4j.properties com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_2.xml --create=true --load=true  --execute=true -s 5 -ss -o result/mysql_w10_t20_0_extend_2
java -Xmx8024m -cp `run/classpath.sh` -Dlog4j.configuration=./log4j.properties com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_10.xml --create=true --load=true  --execute=true -s 5 -ss -o result/mysql_w10_t20_0_extend_10
java -Xmx8024m -cp `run/classpath.sh` -Dlog4j.configuration=./log4j.properties com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_50.xml --create=true --load=true  --execute=true -s 5 -ss -o result/mysql_w10_t20_0_extend_50
java -Xmx8024m -cp `run/classpath.sh` -Dlog4j.configuration=./log4j.properties com.oltpbenchmark.DBWorkload -b tpcc -c config/tpcc_extend/tpcc_extend_100.xml --create=true --load=true  --execute=true -s 5 -ss -o result/mysql_w10_t20_0_extend_100