package order.system.cqrs.commandservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

	public static final String PRODUCT_EVENTS_TOPIC = "product-events";
	public static final String ORDER_EVENTS_TOPIC = "order-events";

	public static final String PRODUCT_PRICE_EVENTS_TOPIC = "product-price-changed-events";
	public static final String PRODUCT_DEACTIVATED_EVENTS_TOPIC = "product-deactivated-events";
	public static final String ORDER_CONFIRMED_EVENTS_TOPIC = "order-confirmed-events";
	public static final String ORDER_CANCELLED_EVENTS_TOPIC = "order-cancelled-events";

	@Bean
	public NewTopic productEventsTopic() {
		return TopicBuilder.name(PRODUCT_EVENTS_TOPIC)
				.partitions(3)
				.replicas(3)
				.compact()
				.build();
	}

	@Bean
	public NewTopic orderEventsTopic() {
		return TopicBuilder.name(ORDER_EVENTS_TOPIC)
				.partitions(3)
				.replicas(3)
				.build();
	}

	@Bean
	public NewTopic productPriceEventsTopic() {
		return TopicBuilder.name(PRODUCT_PRICE_EVENTS_TOPIC)
				.partitions(3)
				.replicas(3)
				.build();
	}

	@Bean
	public NewTopic productDeactivatedEventsTopic() {
		return TopicBuilder.name(PRODUCT_DEACTIVATED_EVENTS_TOPIC)
				.partitions(3)
				.replicas(3)
				.build();
	}

	@Bean
	public NewTopic orderConfirmedEventsTopic() {
		return TopicBuilder.name(ORDER_CONFIRMED_EVENTS_TOPIC)
				.partitions(3)
				.replicas(3)
				.build();
	}

	@Bean
	public NewTopic orderCancelledEventsTopic() {
		return TopicBuilder.name(ORDER_CANCELLED_EVENTS_TOPIC)
				.partitions(3)
				.replicas(3)
				.build();
	}
}
