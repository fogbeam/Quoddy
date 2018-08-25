package org.fogbeam.quoddy.spring.factorybean;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

class CustomBeanPostProcessor implements BeanPostProcessor
{
    Logger log = Logger.getLogger( CustomBeanPostProcessor.class );
    
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) 
    {
        return bean;
    }

    /* in case we need to selectively override some property / properties of a specific bean, without
     * redefining the entire bean definition
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) 
    {
        return bean;
    }
}