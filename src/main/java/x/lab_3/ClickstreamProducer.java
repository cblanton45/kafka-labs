package x.lab_3;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import x.utils.ClickStreamGenerator;

public class ClickstreamProducer implements Runnable {

  private final String topic;
  private final int maxMessages;
  private final int frequency;
  private final Properties props;
  private boolean keepRunning;

  private final KafkaProducer<Integer, String> producer;

  // topic, how many messages to send, and how often (in milliseconds)
  public ClickstreamProducer(String topic, int maxMessages, int frequency) {
    this.topic = topic;
    this.maxMessages = maxMessages;
    this.frequency = frequency;
    this.keepRunning = true;

    this.props = new Properties();
    // TODO-1 : set the broker to 'localhost:9092'   
    this.props.put("bootstrap.servers", "???");
    this.props.put("client.id", "ClickstreamProducer");
    this.props.put("key.serializer",
        "org.apache.kafka.common.serialization.IntegerSerializer");
    this.props.put("value.serializer",
        "org.apache.kafka.common.serialization.StringSerializer");
    this.producer = new KafkaProducer<>(props);
  }

  @Override
  public void run() {

    int numMessages = 0;
    long t1, t2;
    long start = System.nanoTime();
    while (this.keepRunning && (numMessages < this.maxMessages)) {
      numMessages++;
      String clickstream = ClickStreamGenerator.getClickstreamAsCsv();
      //String clickstream = ClickStreamGenerator.getClickstreamAsJSON();
      
      /* TODO-2 : let's construct a record
       *   ProducerRecord takes three parameters
       *   - first param : topic = this.topic
       *   - second param : key = numMessages  (our counter)
       *   - third param : value = clickstream
       */
      ProducerRecord<Integer, String> record =
          new ProducerRecord<>( ???,   ???,   ???);
      t1 = System.nanoTime();
      producer.send(record);
      t2 = System.nanoTime();

      System.out.println("sent : [" + record + "]  in " + (t2 - t1) / 10e6
          + " milli secs");
      // TimeUnit.NANOSECONDS.toMillis(t2 - t1) + " ms");

      try {
        if (this.frequency > 0)
          Thread.sleep(this.frequency);
      } catch (InterruptedException e) {
      }
    }
    long end = System.nanoTime();
    
    // TODO-3 : end the Kafka producer.  Call method 'close'
    // producer.???();

    // print summary
    System.out.println("\n== " + toString() + " done.  " + numMessages + " messages sent in "
        + (end - start) / 10e6 + " milli secs.  Throughput : "
        + numMessages * 10e9 / (end - start) + " msgs / sec");

  }

  public void stop() {
    this.keepRunning = false;
  }

  @Override
  public String toString() {
    return "ClickstreamProducer (topic=" + this.topic + ", maxMessages="
        + this.maxMessages + ", freq=" + this.frequency + " ms)";
  }

  // test driver
  public static void main(String[] args) throws Exception {

    /* TODO-4 :  let's kick off the producer
     * ClickstreamProducer() takes three parameters
     * - first param : name of topic = "clickstream"
     * - second param : how many messages to send = 10 (start with 10 and increase later)
     * - third param : frequency, how often to send = 1000  (in milliseconds,  0 for no wait between sends)
     */
    ClickstreamProducer producer =
        new ClickstreamProducer(???,  ??? , ???);

    System.out.println("Producer starting.... : " + producer);
    Thread t1 = new Thread(producer);
    t1.start();
    t1.join(); // wait for thread to complete
    System.out.println("Producer done.");

  }

}
