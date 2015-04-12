package kafka.exercise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class ConsumerGroupExample {
	private ConsumerConnector consumer;
	private String topic;
	private ExecutorService executor;
	
	
	public ConsumerGroupExample(String a_zookeeper, String a_groupId, String a_topic) {
		this.consumer = Consumer.createJavaConsumerConnector(createConsumerConfig(a_zookeeper, a_groupId));
		this.topic = a_topic;
	}
	
	
	private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
 
        return new ConsumerConfig(props);
    }
	
	
	public void run(int a_numThread) {
		
		Map<String, Integer> topicCount = new HashMap<String, Integer>();
		topicCount.put(topic, a_numThread);
		
		
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCount );
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
		
		executor = Executors.newFixedThreadPool(a_numThread);
		
		int threadNumber = 0;
		for( KafkaStream<byte[], byte[]>  stream: streams) {
			executor.submit(new ConsumerTest(stream, threadNumber));
			threadNumber++;			
		}
	}
	
	public void shutdown() {
		if (consumer != null) {
			consumer.shutdown();
	
		}
		if (executor != null) {
			executor.shutdown();			
		}
		
		try {
			if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
				 System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
			}
			
		} catch (Exception e) {
			System.out.println("Interrupted during shutdown, exiting uncleanly");
		}
	}
	
	public static void main(String[] args) {
		 String zooKeeper = args[0];
	        String groupId = args[1];
	        String topic = args[2];
	        int threads = Integer.parseInt(args[3]);
	 
	        ConsumerGroupExample example = new ConsumerGroupExample(zooKeeper, groupId, topic);
	        example.run(threads);
	 
	        try {
	            Thread.sleep(10000);
	        } catch (InterruptedException ie) {
	 
	        }
	        example.shutdown();
	}
	
	
}
 
	
			