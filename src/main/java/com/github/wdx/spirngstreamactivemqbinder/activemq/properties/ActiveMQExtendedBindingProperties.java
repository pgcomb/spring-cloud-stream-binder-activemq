package com.github.wdx.spirngstreamactivemqbinder.activemq.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.ExtendedBindingProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(value = "spring.cloud.stream")
public class ActiveMQExtendedBindingProperties implements ExtendedBindingProperties<ActiveMQConsumerProperties, ActiveMQProducerProperties> {

	private Map<String, ActiveMQBindingProperties> bindings;

	public Map<String, ActiveMQBindingProperties> getBindings() {
		return this.bindings;
	}

	public void setBindings(Map<String, ActiveMQBindingProperties> bindings) {
		this.bindings = bindings;
	}

	@Override
	public synchronized ActiveMQConsumerProperties getExtendedConsumerProperties(String channelName) {
		if (bindings.containsKey(channelName)) {
			if (bindings.get(channelName).getConsumer() != null) {
				return bindings.get(channelName).getConsumer();
			}
			else {
				ActiveMQConsumerProperties properties = new ActiveMQConsumerProperties();
				this.bindings.get(channelName).setConsumer(properties);
				return properties;
			}
		}
		else {
			ActiveMQConsumerProperties properties = new ActiveMQConsumerProperties();
			ActiveMQBindingProperties rbp = new ActiveMQBindingProperties();
			rbp.setConsumer(properties);
			bindings.put(channelName, rbp);
			return properties;
		}
	}

	@Override
	public synchronized ActiveMQProducerProperties getExtendedProducerProperties(String channelName) {
		if (bindings.containsKey(channelName)) {
			if (bindings.get(channelName).getProducer() != null) {
				return bindings.get(channelName).getProducer();
			}
			else {
				ActiveMQProducerProperties properties = new ActiveMQProducerProperties();
				this.bindings.get(channelName).setProducer(properties);
				return properties;
			}
		}
		else {
			ActiveMQProducerProperties properties = new ActiveMQProducerProperties();
			ActiveMQBindingProperties rbp = new ActiveMQBindingProperties();
			rbp.setProducer(properties);
			bindings.put(channelName, rbp);
			return properties;
		}
	}
}
