package x.lab9_metrics;

import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.codahale.metrics.Counter;

import x.utils.MyConfig;
import x.utils.MyMetricsRegistry;

public class TrafficReporter2 {
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) throws Exception {
		Properties config = new Properties();
		// "bootstrap.servers" = "localhost:9092"
	   config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, MyConfig.DEFAULT_BOOTSTRAP_SERVERS);
		config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		config.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
		boolean readFromBeginning = true;
		if (readFromBeginning) {
			// the following will make sure I am reading from beginning all the
			// time
			config.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
			config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		} else {
			config.put(ConsumerConfig.GROUP_ID_CONFIG, "traffic-reporter2");
		}

		KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(config);
		consumer.subscribe(Arrays.asList("domainCount")); // subscribe

		boolean keepRunning = true;
		while (keepRunning) {
			ConsumerRecords<String, Long> records = consumer.poll(1000);
			if (records.count() > 0) {
				logger.debug("Got " + records.count() + " messages");
				for (ConsumerRecord<String, Long> record : records) {
					logger.debug("Received message : " + record);

					String domain = record.key();
					// since dots have special meaning in metrics, convert dots in domain names into underscore
					String domain2 = domain.replace(".", "_"); 
					long traffic = record.value();

					logger.debug(domain2 + " = " + traffic);

					//# TODO-1 report metrics
					//#    - use 'MyMetricsRegistry.metrics.counter()  API to get a counter
					//#    - for the name of the counter use  : traffic + domain2
					//#    - call 'inc' method on counter  (counter.inc())
					
					// Counter counter = MyMetricsRegistry.metrics.???("????");
					// counter.???
					
					
					//# TODO-2 : There is a subtle bug in the above counting. :-) 
					//# can you spot it?
					//# Hint : look at the debug print to see how you are getting the data
					
					//# TODO-3 : Bonus lab
					//# Look at Metrics API here : http://metrics.dropwizard.io/3.1.0/getting-started/
					//# can we use another Metric?
					

				}
				Utils.sleep(500);
			}
		}
		consumer.close();
	}

	
}
