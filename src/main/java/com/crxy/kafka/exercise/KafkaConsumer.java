package com.crxy.kafka.exercise;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafkaConsumer {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("zookeeper.connect", "192.168.1.170:2181");
		props.put("group.id", "aaaa");
		ConsumerConfig consumerConfig = new ConsumerConfig(props);
		//创建消费者连接对象
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig );
		
		//创建消息流
		Map<String, Integer> configMap = new HashMap<String, Integer>();
		configMap.put("test", 2);
		Map<String, List<KafkaStream<byte[], byte[]>>> ms = consumer.createMessageStreams(configMap );
		
		ExecutorService executor = Executors.newFixedThreadPool(2);
		List<KafkaStream<byte[], byte[]>> topicMessages = ms.get("test");
		
		for (final KafkaStream<byte[], byte[]> kafkaStream : topicMessages) {
			executor.submit(new Runnable() {
				
				@Override
				public void run() {
					ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();
					while(iterator.hasNext()){
						String message = new String(iterator.next().message());
						System.out.println(Thread.currentThread()+":"+message);
					}
				}
			});
		}
	}
}
