package com.github.wdx.spirngstreamactivemqbinder.activemq.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Title: ActiveMQDefaultExtendedBindingProperties <br>
 * Description: ActiveMQDefaultExtendedBindingProperties <br>
 * Date: 2019年08月01日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
@ConfigurationProperties(value = "spring.cloud.stream.activemq.default")
public class ActiveMQDefaultExtendedBindingProperties {

    private ActiveMQConsumerProperties consumer = new ActiveMQConsumerProperties();

    private ActiveMQProducerProperties producer = new ActiveMQProducerProperties();

    public ActiveMQConsumerProperties getConsumer() {
        return consumer;
    }

    public void setConsumer(ActiveMQConsumerProperties consumer) {
        this.consumer = consumer;
    }

    public ActiveMQProducerProperties getProducer() {
        return producer;
    }

    public void setProducer(ActiveMQProducerProperties producer) {
        this.producer = producer;
    }
}
