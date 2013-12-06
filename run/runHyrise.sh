java -Dlog4j.configuration=log4j.properties -Xmx4G -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b hyrise -c config/simple_config_hyrise.xml  --execute true

