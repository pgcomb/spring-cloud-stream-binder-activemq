package com.github.wdx.spirngstreamactivemqbinder.activemq.properties;

import org.springframework.cloud.stream.config.MergableProperties;

public class ActiveMQConsumerProperties implements MergableProperties {

	private String destination;

	/**
	 * topic or queue
	 */
	private String type;

	private String partition;
	
	private boolean transaction;
	
	public boolean isTransaction() {
		return transaction;
	}

	public void setTransaction(boolean transaction) {
		this.transaction = transaction;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ActiveMQConsumerProperties{" +
				"destination='" + destination + '\'' +
				", type='" + type + '\'' +
				", partition='" + partition + '\'' +
				", transaction=" + transaction +
				'}';
	}
}
