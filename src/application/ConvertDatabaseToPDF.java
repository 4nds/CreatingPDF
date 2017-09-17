package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;

public class ConvertDatabaseToPDF {

	

	public static void FOPConvert(StreamSource xmlSource, String filePath) throws ConfigurationException, SAXException {
		try {
            convertToPDF(xmlSource, filePath);
        } catch (FOPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
        
    
    
    /**
     * Method that will convert the given XML to PDF 
     * @throws IOException
     * @throws TransformerException
     * @throws SAXException 
     * @throws ConfigurationException 
     */
    private static void convertToPDF(StreamSource xmlSource, String filePath)  throws IOException, TransformerException, ConfigurationException, SAXException {    	
        InputStream xsltFile = ConvertDatabaseToPDF.class.getResourceAsStream("/resources/template.xsl");		// the XSL FO file
        DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
        Configuration cfg = cfgBuilder.build(ConvertDatabaseToPDF.class.getResourceAsStream("/resources/fonts/mycfg.xconf"));
        FopFactoryBuilder fopFactoryBuilder = new FopFactoryBuilder(new File(".").toURI());
        fopFactoryBuilder.setConfiguration(cfg);
        FopFactory fopFactory = fopFactoryBuilder.build();
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();								// a user agent is needed for transformation
        OutputStream output = new FileOutputStream(filePath);								// Setup output
        try {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, output);		// Construct fop with desired output format
            TransformerFactory transformerFactory = TransformerFactory.newInstance();		// Setup XSLT
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(xsltFile));
            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result result = new SAXResult(fop.getDefaultHandler());
            // Start XSLT transformation and FOP processing, That's where the XML is first transformed to XSL-FO and then, PDF is created
            transformer.transform(xmlSource, result);
        } finally {
        	output.close();
        }
        
    }



    
    

    
    

}