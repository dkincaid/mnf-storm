package storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davek
 * Date: 10/27/12
 * Time: 11:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MnfTopology {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("twitter", new RecordedTwitterSpout());
        builder.setBolt("lowercase", new LowerCaseBolt(), 5)
                .shuffleGrouping("twitter");
        builder.setBolt("teamfilter", new TeamFilterBolt(), 5)
                .shuffleGrouping("lowercase");
        builder.setBolt("scoreboard", new TeamScoreboard(), 2)
                .fieldsGrouping("teamfilter", new Fields("team"));

        Map conf = new HashMap();
        conf.put(Config.TOPOLOGY_DEBUG, false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("MnfTopology", conf, builder.createTopology());

        Handler handler = new AbstractHandler() {

            @Override
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
                response.addHeader("Access-Control-Allow-Origin", "*");
                PrintWriter out = response.getWriter();
                out.print("{ \"Teams\" : [");

                int first = 0;
                for (Map.Entry<String, Integer> score : TeamScoreboard.scoreboard.entrySet()) {
                    out.print(String.format("%s {\"name\" : \"%s\", \"count\" : %d}",
                            first++ > 0 ? "," : "",
                            score.getKey(),
                            score.getValue()));
                }
                out.print("]}");
                out.close();
            }
        };

        Server server = new Server(8082);
        server.setHandler(handler);

        try {
            System.out.println("Starting the UI server!!!!!!!!!!!!!!!!!!!!!");
            server.start();
            System.out.println("Server started!!!!!!!!!!!!!!!!!!!!!!!!!!1");
        } catch (Exception e) {
            e.printStackTrace();

        }

        Utils.sleep(10 * 60 * 1000);
        cluster.killTopology("MnfTopology");
        cluster.shutdown();

    }
}
