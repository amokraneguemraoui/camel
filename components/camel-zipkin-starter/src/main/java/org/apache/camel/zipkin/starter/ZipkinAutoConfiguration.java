/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.zipkin.starter;

import org.apache.camel.CamelContext;
import org.apache.camel.zipkin.ZipkinEventNotifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ZipkinConfigurationProperties.class)
@ConditionalOnProperty(value = "camel.zipkin.enabled", matchIfMissing = true)
public class ZipkinAutoConfiguration {

    @Bean(initMethod = "", destroyMethod = "")
    // Camel handles the lifecycle of this bean
    @ConditionalOnMissingBean(ZipkinEventNotifier.class)
    ZipkinEventNotifier zipkinEventNotifier(CamelContext camelContext,
                                            ZipkinConfigurationProperties configurationProperties) {

        ZipkinEventNotifier notifier = new ZipkinEventNotifier();
        notifier.setHostName(configurationProperties.getHostName());
        notifier.setPort(configurationProperties.getPort());
        notifier.setRate(configurationProperties.getRate());
        notifier.setServiceName(configurationProperties.getServiceName());
        notifier.setIncludeMessageBody(configurationProperties.isIncludeMessageBody());

        // register the bean into CamelContext
        camelContext.getManagementStrategy().addEventNotifier(notifier);

        return notifier;
    }

}