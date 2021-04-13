/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sk.stu.fiit.User.User;
import sk.stu.fiit.User.UserType;

/**
 *
 * @author Adam Bublavý
 */
public class XMLResponseParser implements IResponseParser {

    @Override
    public RegisterResponse parseRegisterData(CloseableHttpResponse response) {
        try {
            Document document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "//jwtToken/text()";

            String token = (String) xPath.compile(expression).evaluate(document,
                    XPathConstants.STRING);

            return new RegisterResponse(token);
        } catch (ParserConfigurationException | IOException
                | UnsupportedOperationException | SAXException ex) {
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public LoginResponse parseLoginData(CloseableHttpResponse response) {
        try {
            Document document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());

            XPath xPath = XPathFactory.newInstance().newXPath();

            String token = (String) xPath.compile("//jwtToken/text()").evaluate(
                    document, XPathConstants.STRING);

            String email = (String) xPath.compile("//user/email/text()").
                    evaluate(document, XPathConstants.STRING);
            String type = (String) xPath.compile("//user/type/text()").evaluate(
                    document, XPathConstants.STRING);
            String firstName = (String) xPath.compile("//user/firstName/text()").
                    evaluate(document, XPathConstants.STRING);
            String lastName = (String) xPath.compile("//user/lastName/text()").
                    evaluate(document, XPathConstants.STRING);
            String photo = (String) xPath.compile("//user/photo/text()").
                    evaluate(document, XPathConstants.STRING);
            
            return new LoginResponse(token, new User(email, UserType.valueOf(
                    type), firstName, lastName, photo));
        } catch (IOException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
