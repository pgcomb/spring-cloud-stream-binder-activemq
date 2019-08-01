package com.github.wdx.spirngstreamactivemqbinder.activemq.properties;

import org.springframework.cloud.stream.config.MergableProperties;

public class ActiveMQProducerProperties implements MergableProperties {

	private String destination;

	private String type;

	private String partition;
	
	private boolean transaction = false;

	public boolean isTransaction() {
		return transaction;
	}

	public void setTransaction(boolean transaction) {
		this.transaction = transaction;
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
