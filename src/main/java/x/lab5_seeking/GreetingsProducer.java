package x.lab5_seeking;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import x.utils.MyConfig;

public class GreetingsProducer {
	private static final Logger logger = LogManager.getLogger();
	
 
  public static void main(String[] args) throws Exception {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("client.id", "GreetingsProducer");
    props.put("key.serializer",
        "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer",
        "org.apache.kafka.common.serialization.StringSerializer");

    KafkaProducer<String, String> producer = new KafkaProducer<>(props);

    //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    for (int i = 1; i <= 10; i++) {
      String key = new Integer(i).toString();
      //String value = df.format(new Date())+  "--" + i + ", Hello world";
      String value =  "Hello world";
      ProducerRecord<String, String> record =
          new ProducerRecord<>(MyConfig.TOPIC_GREETINGS, key, value);
      logger.debug("sending : " + record);
      producer.send(record);
    }
    producer.close();

  }

}