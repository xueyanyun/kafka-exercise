package kafka.exercise;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class ConsumerTest implements Runnable {
	private KafkaStream<byte[], byte[]> m_stream;
	private int m_threadNumber;
	
	public ConsumerTest(KafkaStream<byte[], byte[]> a_stream, int a_threadNumber) {
		m_threadNumber = a_threadNumber;
		m_stream = a_stream;		
	}


	@Override
	public void run() {
		ConsumerIterator<byte[],byte[]> iterator = m_stream.iterator();
		while (iterator.hasNext()) {
			System.out.println("Thread " + m_threadNumber + ": " + new String(iterator.next().message()));			
		}
		System.out.println("Shutting down Thread: " + m_threadNumber);	
	}

}
