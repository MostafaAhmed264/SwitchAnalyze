package SwitchAnalyzer.Database;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

import java.util.ArrayList;

public class CassandraConnector {
    private Cluster cluster;
    private Session session;
    /**
     * Input : arraylist of ips that are used as cassandra nodes , number of port to connect to , metadata
     * Output : void
     * Description :
     * 1. This will function will try to connect on any cassandra node
     * 2. If a node is down it will try to connect to another node from the list we passed
     * 3.1. If not connected to any of the arraylist then the connection will fail completely
     * 3.2.1. If it connected to a node then it will connect to the port we passed
     *        if the port is null then it will connect to the default which is 9042
     * 3.2.2. It will set the metadata so we can use it later to check on the keyspaces , tables and udts
     */
    public void connect(ArrayList<String>IPS,Integer port,Metadata metadata) {
        /*add nodes to connect*/
        Cluster.Builder b ;
        for(int i=0;i< IPS.size();++i){
            try{
                b=Cluster.builder().addContactPoint(IPS.get(i));
                // we want to check what will happen if more than an IP is correct
                if (port != null) {
                    b.withPort(port);
                }
                cluster = b.build();
                session = cluster.connect();
                DBConnect.setMetadata(cluster.getMetadata());
            }
            catch (Exception ex) {
                System.out.println("can't connect to node with this IP " + IPS.get(i));
            }
        }

    }
    public Session getSession() {
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }
}
