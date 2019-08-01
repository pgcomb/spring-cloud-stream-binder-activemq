package com.github.wdx.spirngstreamactivemqbinder.activemq.config;

import com.github.wdx.spirngstreamactivemqbinder.activemq.ActiveMQMessageChannelBinder;
import com.github.wdx.spirngstreamactivemqbinder.activemq.properties.ActiveMQBinderConfigurationProperties;
import com.github.wdx.spirngstreamactivemqbinder.activemq.properties.ActiveMQDefaultExtendedBindingProperties;
import com.github.wdx.spirngstreamactivemqbinder.activemq.properties.ActiveMQExtendedBindingProperties;
import com.github.wdx.spirngstreamactivemqbinder.activemq.provisioning.ActiveMQProvisioningProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * Title: ActiveMqBinderConfiguration <br>
 * Description: ActiveMqBinderConfiguration <br>
 * Date: 2019年07月24日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */


@Configuration
//@ConditionalOnMissingBean(Binder.class)
//@AutoConfigureAfter({JmsAutoConfiguration.class})
//@Import({PropertyPlaceholderAutoConfiguration.class, KryoCodecAutoConfiguration.class})
@Import({ PropertyPlaceholderAutoConfiguration.class })
@EnableConfigurationProperties({ActiveMQExtendedBindingProperties.class,ActiveMQBinderConfigurationProperties.class, ActiveMQDefaultExtendedBindingProperties.class})
public class ActiveMqBinderConfiguration {
    @Autowired
    private ActiveMQBinderConfigurationProperties activeMQBinderConfigurationProperties;

    @Autowired
    private ActiveMQDefaultExtendedBindingProperties activeMQDefaultExtendedBindingProperties;
    @Bean
    public ActiveMQMessageChannelBinder activeMQMessageChannelBinder(ActiveMQExtendedBindingProperties activeMQExtendedBindingProperties){
        ActiveMQMessageChannelBinder activeMQMessageChannelBinder = new ActiveMQMessageChannelBinder(new String[0], new ActiveMQProvisioningProvider(),
                activeMQBinderConfigurationProperties,activeMQDefaultExtendedBindingProperties);
        activeMQMessageChannelBinder.setExtendedProperties(activeMQExtendedBindingProperties);
        return activeMQMessageChannelBinder;
    }
}

