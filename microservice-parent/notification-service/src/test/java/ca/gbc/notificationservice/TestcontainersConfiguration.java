package ca.gbc.notificationservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:7.5.0");

	@Bean
	public KafkaContainer kafkaContainer() {
		KafkaContainer kafkaContainer = new KafkaContainer(KAFKA_IMAGE)
				.withEnv("KAFKA_BROKER_ID", "1")
				.withEnv("KAFKA_ZOOKEEPER_CONNECT", "zookeeper:2181");
		kafkaContainer.start();
		return kafkaContainer;
	}

	@DynamicPropertySource
	static void registerKafkaProperties(DynamicPropertyRegistry registry) {
		KafkaContainer kafkaContainer = new KafkaContainer(KAFKA_IMAGE);
		kafkaContainer.start();
		registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
	}

}
