package com.example.ecommerce.common;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.lifecycle.Startables;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    // Singleton containers: static so they start once for all test classes extending this base class
    static final PostgreSQLContainer<?> POSTGRES_CONTAINER;
    static final GenericContainer<?> REDIS_CONTAINER;
    static final RabbitMQContainer RABBITMQ_CONTAINER;
    static final GenericContainer<?> MINIO_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
                .withDatabaseName("test_db")
                .withUsername("test_user")
                .withPassword("test_pass");

        REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
                .withExposedPorts(6379);

        RABBITMQ_CONTAINER = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-alpine"));

        MINIO_CONTAINER = new GenericContainer<>(DockerImageName.parse("minio/minio:RELEASE.2024-05-28T17-19-04Z"))
                .withExposedPorts(9000)
                .withEnv("MINIO_ROOT_USER", "minioadmin")
                .withEnv("MINIO_ROOT_PASSWORD", "minioadmin")
                .withCommand("server /data");

        // Start containers in parallel
        Startables.deepStart(List.of(
                POSTGRES_CONTAINER, 
                REDIS_CONTAINER, 
                RABBITMQ_CONTAINER, 
                MINIO_CONTAINER
        )).join();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);

        // Redis
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", REDIS_CONTAINER::getFirstMappedPort);

        // RabbitMQ
        registry.add("spring.rabbitmq.host", RABBITMQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", RABBITMQ_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBITMQ_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBITMQ_CONTAINER::getAdminPassword);

        // MinIO
        registry.add("spring.cloud.storage.endpoint", MINIO_CONTAINER::getHost);
        registry.add("spring.cloud.storage.port", MINIO_CONTAINER::getFirstMappedPort);
    }
}