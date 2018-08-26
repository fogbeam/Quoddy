package org.fogbeam.quoddy.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

public class NoOpMessageConverter implements MessageConverter
{
    @Override
    public Message toMessage( Object object, Session session )
            throws JMSException, MessageConversionException
    {
        System.out.println( "toMessage() called" );
        
        SimpleMessageConverter simpleConverter = new SimpleMessageConverter();
        return simpleConverter.toMessage( object, session );
    }

    @Override
    public Object fromMessage( Message message )
            throws JMSException, MessageConversionException
    {
        System.out.println( "fromMessage() called" );
        
        return message;
    }
}
