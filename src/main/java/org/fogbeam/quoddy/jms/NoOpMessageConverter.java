package org.fogbeam.quoddy.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;


public class NoOpMessageConverter implements MessageConverter
{
	Logger log = LoggerFactory.getLogger( NoOpMessageConverter.class );
	
    @Override
    public Message toMessage( Object object, Session session )
            throws JMSException, MessageConversionException
    {
        log.info( "toMessage() called" );
        log.info( "Initial object class: " + object.getClass().getName() );
        log.info( "Initial object: " + object );
        
        SimpleMessageConverter simpleConverter = new SimpleMessageConverter();
        
        Message convertedMessage = simpleConverter.toMessage( object, session );
        
        log.info( "convertedMessage class: " + convertedMessage.getClass().getName() );
        log.info( "convertedMessage: " + convertedMessage );
        
        return convertedMessage;
    }

    @Override
    public Object fromMessage( Message message )
            throws JMSException, MessageConversionException
    {
    	log.info( "fromMessage() called" );
        log.info( "Initial Message class: " + message.getClass().getName() );
        log.info( "Initial Message: " + message );
    	
    	
        return message;
    }
}