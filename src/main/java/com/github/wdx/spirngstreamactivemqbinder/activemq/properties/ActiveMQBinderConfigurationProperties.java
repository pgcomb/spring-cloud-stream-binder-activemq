package com.github.wdx.spirngstreamactivemqbinder.activemq.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Title: ActiveMQBinderConfigurationProperties <br>
 * Description: ActiveMQBinderConfigurationProperties <br>
 * Date: 2019年07月27日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
@ConfigurationProperties(prefix = "spring.cloud.stream.activemq.binder")
public class ActiveMQBinderConfigurationProperties {
    private String brokerUrl;
    private String user;
    private String password;

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
