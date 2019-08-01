package com.github.wdx.spirngstreamactivemqbinder.activemq;


import com.github.wdx.spirngstreamactivemqbinder.activemq.properties.*;
import com.github.wdx.spirngstreamactivemqbinder.activemq.provisioning.ActiveMQProvisioningProvider;
import com.github.wdx.spirngstreamactivemqbinder.activemq.support.ActiveMQMessageProducer;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.binder.*;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.jms.JmsSendingMessageHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.jms.DeliveryMode;

public class ActiveMQMessageChannelBinder 
	extends AbstractMessageChannelBinder<ExtendedConsumerProperties<ActiveMQConsumerProperties>,
			ExtendedProducerProperties<ActiveMQProducerProperties>, ActiveMQProvisioningProvider>
	implements ExtendedPropertiesBinder<MessageChannel, ActiveMQConsumerProperties, ActiveMQProducerProperties> {
	
	private ActiveMQExtendedBindingProperties extendedProperties;

	private ActiveMQBinderConfigurationProperties activeMQBinderConfigurationProperties;

	private ActiveMQDefaultExtendedBindingProperties activeMQDefaultExtendedBindingProperties;

	public ActiveMQMessageChannelBinder(String[] headersToEmbed, ActiveMQProvisioningProvider provisioningProvider,
										ActiveMQBinderConfigurationProperties activeMQBinderConfigurationProperties,
										ActiveMQDefaultExtendedBindingProperties activeMQDefaultExtendedBindingProperties) {
		super(headersToEmbed, provisioningProvider);
		this.activeMQBinderConfigurationProperties = activeMQBinderConfigurationProperties;
		this.activeMQDefaultExtendedBindingProperties = activeMQDefaultExtendedBindingProperties;
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

		if ("topic".equals(producerProperties.getExtension().getType())){
			jmsTemplate.setDefaultDestination(new ActiveMQTopic(destination.getName()));
			jmsSendingMessageHandler.setDestination(new ActiveMQTopic(destination.getName()));
		}else {
			jmsTemplate.setDefaultDestination(new ActiveMQQueue(destination.getName()));
			jmsSendingMessageHandler.setDestination(new ActiveMQQueue(destination.getName()));
		}
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
		if ("topic".equals(properties.getExtension().getType())){
			jmsTemplate.setDefaultDestination(new ActiveMQTopic(destination.getName()));
		}else {
			jmsTemplate.setDefaultDestination(new ActiveMQQueue(destination.getName()));
		}

		jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
		producer.setJmsTemplate(jmsTemplate);
		if ("topic".equals(properties.getExtension().getType())){
			producer.setDestination(new ActiveMQTopic(destination.getName()));
		}else {
			producer.setDestination(new ActiveMQQueue(destination.getName()));
		}
		return producer;
	}

	@Override
	public ActiveMQConsumerProperties getExtendedConsumerProperties(String channelName) {
		ActiveMQConsumerProperties consumer = new ActiveMQConsumerProperties();
		BeanUtils.copyProperties(this.extendedProperties.getExtendedConsumerProperties(channelName),consumer);
		ActiveMQConsumerProperties consumerDef = activeMQDefaultExtendedBindingProperties.getConsumer();
		BeanUtils.copyProperties(activeMQDefaultExtendedBindingProperties.getConsumer(),consumerDef);
		consumerDef.merge(consumer);
		return consumer;
	}

	@Override
	public ActiveMQProducerProperties getExtendedProducerProperties(String channelName) {
		ActiveMQProducerProperties producerProperties = new ActiveMQProducerProperties();
		BeanUtils.copyProperties(this.extendedProperties.getExtendedProducerProperties(channelName),producerProperties);
		ActiveMQProducerProperties producerPropertiesDef = activeMQDefaultExtendedBindingProperties.getProducer();
		BeanUtils.copyProperties(activeMQDefaultExtendedBindingProperties.getProducer(),producerPropertiesDef);
		producerPropertiesDef.merge(producerProperties);
		return producerProperties;
	}

	private ActiveMQConnectionFactory getConnectionFactory(){
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(activeMQBinderConfigurationProperties.getBrokerUrl());
		activeMQConnectionFactory.setUserName(activeMQBinderConfigurationProperties.getUser());
		activeMQConnectionFactory.setPassword(activeMQBinderConfigurationProperties.getPassword());
		return activeMQConnectionFactory;
	}
}
