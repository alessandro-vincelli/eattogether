package pagineGialleImport.it.av.tweat;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ClientWithResponseHandler {
    public final static void main(String[] args) throws Exception {
        
/*		sp.parse(new ByteArrayInputStream(Html2Xml.Html2Xml(new String(method.getResponseBody())).getBytes()), dh);
		XMLOutputFactory outFact =XMLOutputFactory.newInstance() ;
		outFact.createXMLEventWriter(System.out);
		OutputStreamWriter out = new OutputStreamWriter(System.out, "UTF8");
*/		
		HttpClient httpclient = new HttpClient();
		
    	for (int k = 1; k <= 1902; k++) {

			HttpMethod method = new GetMethod("http://www.paginegialle.it/pgol/1-007585100/4-ristoranti/l-1/p-" + k + "?mr=30");
			httpclient.executeMethod(method);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builderD = factory.newDocumentBuilder();
			org.w3c.dom.Document document = builderD.parse(new ByteArrayInputStream(Html2Xml.Html2Xml(new String(method.getResponseBody())).getBytes()));
			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xpathJDK = xpf.newXPath();
			XPathExpression xpathRisto = xpathJDK.compile(".//div[@class='listing-client-line-pg  clearfix']");
			NodeList list = (NodeList) xpathRisto.evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++) {
				Node nodo = list.item(i);
				XPathExpression xpathTitle = xpathJDK.compile(".//h3[@class='org orange']/a");
				Node nodeTitle = (Node) xpathTitle.evaluate(nodo, XPathConstants.NODE);
				System.out.println("title: " + nodeTitle.getFirstChild().getTextContent());

				XPathExpression xpathPostalCode = xpathJDK.compile(".//span[@class='postal-code']");
				Node nodeCAP = (Node) xpathPostalCode.evaluate(nodo, XPathConstants.NODE);
				System.out.println("title: " + nodeCAP.getFirstChild().getTextContent());

				XPathExpression xpathLocality = xpathJDK.compile(".//span[@class='locality']");
				Node nodeLocality = (Node) xpathLocality.evaluate(nodo, XPathConstants.NODE);
				System.out.println("title: " + nodeLocality.getFirstChild().getTextContent());

				XPathExpression xpathProvince = xpathJDK.compile(".//span[@class='region']");
				Node nodeProvince = (Node) xpathProvince.evaluate(nodo, XPathConstants.NODE);
				System.out.println("title: " + nodeProvince.getFirstChild().getTextContent());

				XPathExpression xpathStreet = xpathJDK.compile(".//p[@class='street-address']");
				Node nodeStreet = (Node) xpathStreet.evaluate(nodo, XPathConstants.NODE);
				System.out.println("title: " + nodeStreet.getFirstChild().getTextContent());

				XPathExpression xpathTels = xpathJDK.compile(".//p[@class='tel']");
				NodeList nodeTels = (NodeList) xpathTels.evaluate(nodo, XPathConstants.NODESET);
				for (int j = 0; j < nodeTels.getLength(); j++) {
					Node tel = nodeTels.item(j);
					XPathExpression xpathTelType = xpathJDK.compile(".//span[@class='type']");
					Node nodeTelType = (Node) xpathTelType.evaluate(tel, XPathConstants.NODE);
					System.out.println("Tel: " + nodeTelType.getFirstChild().getTextContent() + " " + tel.getLastChild().getNodeValue());
				}
				XPathExpression xpathWWW = xpathJDK.compile(".//a[@class='lnkwww']");
				Node nodeWWW = (Node) xpathWWW.evaluate(nodo, XPathConstants.NODE);

				if (nodeWWW != null) {
					HttpMethod methodWWW = new GetMethod(nodeWWW.getAttributes().getNamedItem("href").getTextContent());
					methodWWW.setFollowRedirects(true);
					httpclient.executeMethod(methodWWW);
					methodWWW.getResponseBody();
					System.out.println("www: " + methodWWW.getURI());
				}

			}

		}

	}

}
