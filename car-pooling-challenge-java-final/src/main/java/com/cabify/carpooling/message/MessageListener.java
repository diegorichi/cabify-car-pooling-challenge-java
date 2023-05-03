package com.cabify.carpooling.message;

import com.cabify.carpooling.model.Car;
import com.cabify.carpooling.model.Journey;
import com.cabify.carpooling.service.AssignmentService;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * A message listener that listens for messages on two queues - "journey" and "car".
 * It assigns journeys and reassigns cars based on the received messages.
 */
@Component
@NoArgsConstructor
public class MessageListener {

    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${queue.journey.name}")
    private String journeyQueueName;

    @Value("${queue.journey.durable}")
    private boolean journeyQueueDurable;

    @Value("${queue.car.name}")
    private String carQueueName;

    @Value("${queue.car.durable}")
    private boolean carQueueDurable;

    /**
     * Defines a bean for the {@link Journey}  queue.
     *
     * @return The journey queue.
     */
    @Bean
    public Queue journeyQueue() {
        return new Queue(journeyQueueName, journeyQueueDurable);
    }

    /**
     * Defines a bean for the {@link Car} queue.
     *
     * @return The car queue.
     */
    @Bean
    public Queue carQueue() {
        return new Queue(carQueueName, carQueueDurable);
    }

    /**
     * Listens for messages on the {@link Journey} queue and assigns the journey.
     *
     * @param journey The journey to assign.
     */
    @RabbitListener(queues = "${queue.journey.name}")
    public void onJourneyMessage(Journey journey) {
        assignmentService.assign(journey);
    }

    /**
     * Listens for messages on the {@link Car} queue and reassigns the car.
     *
     * @param car The car to reassign.
     */
    @RabbitListener(queues = "${queue.car.name}")
    public void onCarMessage(Car car) {
        assignmentService.reassign(car);
    }

    /**
     * Publishes the journey to the journey queue.
     *
     * @param journey The journey to publish.
     */
    public void publish(Journey journey) {
        rabbitTemplate.convertAndSend(journeyQueueName, journey);
    }

    /**
     * Publishes the car to the car queue.
     *
     * @param car The car to publish.
     */
    public void publish(Car car) {
        rabbitTemplate.convertAndSend(carQueueName, car);
    }

}

