java -Dlog4j.configuration=log4j.properties -Xmx4G -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b hyrise -c config/simple_config_hyrise.xml --load true  --execute true -s 1 -ss -o hyrise

