package storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davek
 * Date: 10/28/12
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TeamFilterBolt implements IRichBolt {
    private OutputCollector collector;

    private static enum Team {
        //BRONCOS("broncos", "denver", "manning", "peyton"),
        //SAINTS("saints","new orleans", "brees"),
        FORTYNINERS("sanfrancisco", "san francisco", "san fran", "49ers", "niners", "gore", "davis", "crabtree", "manningham", "moss" ),
        CARDINALS("cardinals", "cards", "arizona", "ari", "azcardinals", "kolb", "fitzgerald", "skelton", "williams", "roberts");


        private final List<String> keywords;
        private final String name;

        private Team(String name, String... keywords) {
            this.name = name;
            this.keywords = ImmutableList.<String>builder().add(name).add(keywords).build();
        }

        public String getName() {
            return name;
        }

        public List<String> getKeywords() {
            return keywords;
        }
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        String text = tuple.getString(2);

        for (Team team : Team.values()) {
            for (String keyword : team.getKeywords()) {
                if (text.contains(keyword)) {
                    collector.emit(new Values(
                            tuple.getString(0),
                            tuple.getValue(1),
                            tuple.getString(2),
                            team.getName()));

                    break;
                }
            }
        }

        collector.ack(tuple);
    }

    @Override
    public void cleanup() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("id", "created_at", "text", "team"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
