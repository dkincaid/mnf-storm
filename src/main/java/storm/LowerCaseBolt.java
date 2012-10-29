package storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davek
 * Date: 10/28/12
 * Time: 8:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class LowerCaseBolt implements IRichBolt {
    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        collector.emit(tuple, new Values(
                tuple.getString(0),
                tuple.getValue(1),
                tuple.getString(2).toLowerCase()
                ));

        collector.ack(tuple);
    }

    @Override
    public void cleanup() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("id", "created_at", "text"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
