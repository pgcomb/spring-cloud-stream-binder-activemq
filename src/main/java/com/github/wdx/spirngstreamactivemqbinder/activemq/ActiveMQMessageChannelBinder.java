package com.github.wdx.spirngstreamactivemqbinder.activemq;


import com.github.wdx.spirngstreamactivemqbinder.activemq.properties.ActiveMQBinderConfigurationProperties;
import com.github.wdx.spirngstreamactivemqbinder.activemq.properties.ActiveMQConsumerProperties;
import com.github.wdx.spirngstreamactivemqbinder.activemq.properties.ActiveMQExtendedBindingProperties;
import com.github.wdx.spirngstreamactivemqbinder.activemq.properties.ActiveMQProducerProperties;
import com.github.wdx.spirngstreamactivemqbinder.activemq.provisioning.ActiveMQQueueProvisioner;
import com.github.wdx.spirngstreamactivemqbinder.activemq.support.ActiveMQMessageProducer;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.cloud.stream.binder.*;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

public class ActiveMQMessageChannelBinder 
	extends AbstractMessageChannelBinder<ExtendedConsumerProperties<ActiveMQConsumerProperties>,
			ExtendedProducerProperties<ActiveMQProducerProperties>, ActiveMQQueueProvisioner>
	implements ExtendedPropertiesBinder<MessageChannel, ActiveMQConsumerProperties, ActiveMQProducerProperties> {
	
	private ActiveMQExtendedBindingProperties extendedProperties;

	private ActiveMQBinderConfigurationProperties activeMQBinderConfigurationProperties;

	public ActiveMQMessageChannelBinder(String[] headersToEmbed, ActiveMQQueueProvisioner provisioningProvider,
										ActiveMQBinderConfigurationProperties activeMQBinderConfigurationProperties) {
		super(headersToEmbed, provisioningProvider);
		this.activeMQBinderConfigurationProperties = activeMQBinderConfigurationProperties;
	}


	public void setExtendedProperties(ActiveMQExtendedBindingProperties extendedProperties) {
		this.extendedProperties = extendedProperties;
	}

	@Override
	protected MessageHandler createProducerMessageHandler(ProducerDestination destination,
                                                          ExtendedProducerProperties<ActiveMQProducerProperties> producerProperties, MessageChannel errorChannel)
			throws Exception {

        JmsTemplate jmsTemplate = new JmsTemplate(getConnectionFactory());
        if (producerProperties.getExtension().isTransaction()){
        	jmsTemplate.setSessionTransacted(true);
		}else {
        	jmsTemplate.setSessionTransacted(false);
		}
        JmsSendingMessageHandler jmsSendingMessageHandler = new JmsSendingMessageHandler(jmsTemplate);
        jmsSendingMessageHandler.setDestination(new ActiveMQTopic(destination.getName()));
        jmsSendingMessageHandler.setBeanFactory(getApplicationContext());
        return jmsSendingMessageHandler;
	}

	@Override
	protected MessageProducer createConsumerEndpoint(ConsumerDestination destination, String group,
                                                     ExtendedConsumerProperties<ActiveMQConsumerProperties> properties) throws Exception {
		ActiveMQMessageProducer producer = new ActiveMQMessageProducer();

		if (properties.getExtension().isTransaction()) {
			producer.setTransaction(true);
		}
		JmsTemplate jmsTemplate = new JmsTemplate(getConnectionFactory());
		jmsTemplate.setDefaultDestination(new ActiveMQTopic(destination.getName()));
		jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
		producer.setJmsTemplate(jmsTemplate);
		producer.setDestination(new ActiveMQTopic(destination.getName()));
		return producer;
	}

	@Override
	public ActiveMQConsumerProperties getExtendedConsumerProperties(String channelName) {
		return this.extendedProperties.getExtendedConsumerProperties(channelName);
	}

	@Override
	public ActiveMQProducerProperties getExtendedProducerProperties(String channelName) {
		return this.extendedProperties.getExtendedProducerProperties(channelName);
	}

	private ActiveMQConnectionFactory getConnectionFactory(){
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(activeMQBinderConfigurationProperties.getBrokerUrl());
		activeMQConnectionFactory.setUserName(activeMQBinderConfigurationProperties.getUser());
		activeMQConnectionFactory.setPassword(activeMQBinderConfigurationProperties.getPassword());
		return activeMQConnectionFactory;
	}
}
