package storm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import com.idexx.twitter.Tweet;
import com.ning.http.client.*;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: davek
 * Date: 10/27/12
 * Time: 11:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RecordedTwitterSpout implements IRichSpout {
    private SpoutOutputCollector collector = null;
    private LinkedBlockingQueue<Tweet> queue = null;
    private final String eventServerUrl = "http://localhost:8080/replay";

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("id", "created_at", "text"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
        queue = new LinkedBlockingQueue<Tweet>(1000);

        final ObjectMapper mapper = new ObjectMapper();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(new AsyncHttpClientConfig.Builder()
          .setConnectionTimeoutInMs(10 * 60 * 1000)
          .setRequestTimeoutInMs(10 * 60 * 1000)
          .build()
        );

        try {
            asyncHttpClient.prepareGet("http://localhost:8080/replay").execute(new AsyncHandler<Response>() {

                @Override
                public void onThrowable(Throwable throwable) {

                }

                @Override
                public STATE onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                    try {
                        if (bodyPart != null) {
                            queue.offer(mapper.readValue(new String(bodyPart.getBodyPartBytes()), Tweet.class));
                        }
                    } catch (JsonParseException jpe) {
                        jpe.printStackTrace();
                    }
                    return STATE.CONTINUE;
                }

                @Override
                public STATE onStatusReceived(HttpResponseStatus httpResponseStatus) throws Exception {
                    return STATE.CONTINUE;
                }

                @Override
                public STATE onHeadersReceived(HttpResponseHeaders httpResponseHeaders) throws Exception {
                    return STATE.CONTINUE;
                }

                @Override
                public Response onCompleted() throws Exception {
                    return null;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void activate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deactivate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nextTuple() {
        Tweet nextTweet = queue.poll();
        if (nextTweet == null) {
            Utils.sleep(50);
        } else {
           collector.emit(new Values(
                   nextTweet.getId(),
                   nextTweet.getCreatedAt(),
                   nextTweet.getText()));
        }
    }

    @Override
    public void ack(Object o) {

    }

    @Override
    public void fail(Object o) {

    }
}
