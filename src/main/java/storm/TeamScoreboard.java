package storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davek
 * Date: 10/28/12
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TeamScoreboard implements IRichBolt {
    private OutputCollector collector;
    static Map<String, Integer> scoreboard = Maps.newHashMap();

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String team = tuple.getString(3);
        DateTime time = (DateTime) tuple.getValue(1);

        if (!scoreboard.containsKey(team)) {
            scoreboard.put(team, 0);
        }

        scoreboard.put(team, scoreboard.get(team)+1);

        System.out.println(time.toString() + " : " + team + " score: " + scoreboard.get(team) + " thanks to: " + tuple.getString(2));

        collector.emit(new Values(
                team,
                scoreboard.get(team)));

        collector.ack(tuple);
    }

    @Override
    public void cleanup() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("team", "score"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
