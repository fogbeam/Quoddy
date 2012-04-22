package org.fogbeam.quoddy.taglib

import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamSource
import javax.xml.transform.stream.StreamResult

class XmlTagLib 
{
    /**
     * Applies an XSL stylesheet to a source DOM document or file and
     * writes the result to the output stream.
     */
    def transform = { attrs ->
        // Check that some XML to transform has been provided.
        def input = attrs['source']
        if (!input) 
		{
            throwTagError('Tag [transform] missing required attribute [source].')
        }

        // Check the various attributes.
        //
        // We must have one of 'transformer' or 'stylesheet'.
        def transformer = attrs['transformer']
        
		if (!transformer) 
		{
            // No transformer specified, so use a stylesheet instead.
            def stylesheet = attrs['stylesheet']

            // If there's no stylesheet either, then we're stuck.
            if (!stylesheet) 
			{
                throwTagError('Tag [transform] missing required attribute [transformer] or [stylesheet].')
            }

            // See whether a factory class has been specified. If so,
            // we use that one to create a transformer. Otherwise, we
            // just use the default factory.
            def factory
            if (attrs['factory']) 
			{
                factory = Class.forName(attrs['factory']).newInstance()
            }
            else 
			{
                factory = TransformerFactory.newInstance()
            }

            // Load up the stylesheet into a transformer instance.
            def xslStream = servletContext.getResourceAsStream("/stylesheets/${stylesheet}.xsl")
			if( !xslStream ) 
			{
				throw new RuntimeException( "could not load stylesheet resource!");	
			}
			
			transformer = factory.newTransformer(new StreamSource(xslStream))
			if( !transformer ) 
			{
				throw new RuntimeException( "could not build XSLT Transformer!" );	
			}
        }
        else if (attrs['stylesheet']) 
		{
            throwTagError('Tag [transform]: [stylesheet] attribute can not be used with [transformer].')
        }

        // We have the transformer set up, so now prepare the source
        // XML and wrap the output writer in a Result.
        def output = new StreamResult(out)
		println "input is: ${input}";
        if( input instanceof DOMSource )
		{
			// NOP, we can use this directly as is
			println "input is already DOMSource, using as is";
		}
		else if( input instanceof org.w3c.dom.Node )
		{
			println "It's an org.w3c.dom.Node, so wrapping in DOMSource";
			input = new DOMSource(input)
		}
		else if (input instanceof Node) 
		{
			println "It's an groovy.util.Node, so wrapping in DOMSource";
            input = new DOMSource(input)
        }
        else if (input instanceof InputStream || input instanceof Reader) 
		{
			println "It's an InputStream OR a Reader, so wrapping in StreamSource";
            // Create a StreamSource for the given file.
            input = new StreamSource(input)
        }
        else 
		{
			println "It's something else, so trying to use as File()";
            input = new StreamSource(new File(input))
        }

        // Perform the transformation!
		println "Before transform, input is: ${input}";
		println "Before transform, transformer is: ${transformer}";
		try
		{
			transformer.transform(input, output);
		}
		catch( Exception e )
		{
			e.printStackTrace();
			throw e;	
		}
    }
}