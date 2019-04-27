package Commands;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public abstract class ConcreteCommand extends Command {
	Channel channel;
	String RPC_QUEUE_NAME;

	public void consume(String RPC_QUEUE_NAME) {
		this.RPC_QUEUE_NAME = RPC_QUEUE_NAME;
		ConnectionFactory factory = new ConnectionFactory();
		String host = System.getenv("RABBIT_MQ_SERVICE_HOST");
		factory.setHost(host);
		Connection connection = null;
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

			System.out.println(" [x] Awaiting DB-RPC Responses");

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {

					AMQP.BasicProperties myProps = (AMQP.BasicProperties) parameters.get("properties");

					System.out.println("Incoming corrID " + properties.getReplyTo());
					System.out.println("My corrID " + myProps.getReplyTo());

					if (properties.getCorrelationId().equals(myProps.getCorrelationId())) {
						System.out.println("after if");
						AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
								.correlationId(properties.getCorrelationId()).build();
						System.out.println("Responding to db-corrID: " + properties.getCorrelationId());

						try {
							String message = new String(body, "UTF-8");
							HashMap<String, Object> props = new HashMap<String, Object>();
							props.put("channel", channel);
							props.put("properties", properties);
							props.put("replyProps", replyProps);
							props.put("envelope", envelope);
							props.put("body", message);

							handleApi(props);
							this.getChannel().basicAck(envelope.getDeliveryTag(), false);
						} catch (RuntimeException e) {
							System.out.println(" [.] " + e.toString());
						} finally {
							synchronized (this) {
								this.notify();
							}
						}
					} else {
						this.getChannel().basicNack(envelope.getDeliveryTag(), false, true);
					}
				}
			};
			channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	public abstract void handleApi(HashMap<String, Object> service_parameters);

	public void sendMessage(String service, String requestId, String message) {
		try {
			AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(requestId)
					.replyTo(RPC_QUEUE_NAME).build();
//			System.out.println(service);
			System.out.println("Sending to db :" + message);
			channel.basicPublish("", service + "-request", props, message.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}