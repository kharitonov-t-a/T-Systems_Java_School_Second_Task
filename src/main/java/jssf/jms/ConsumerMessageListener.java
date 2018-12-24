package jssf.jms;

import jssf.service.ProductService;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.Consume;
import org.apache.camel.language.Bean;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.annotation.ejb.ResourceAdapter;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.Singleton;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.*;
import java.io.Serializable;


//@MessageDriven(activationConfig = {
//        @ActivationConfigProperty(propertyName = "destination", propertyValue = "jssf.jms.JmsConfig"),
//        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
//        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "AUTO_ACKNOWLEDGE")
//})

//@MessageDriven(activationConfig =
//        {
//                @ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue"),
////                @ActivationConfigProperty(propertyName="url", propertyValue="tcp://localhost:61616"),
//                @ActivationConfigProperty(propertyName="destination", propertyValue="productQueue"),
//                @ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
//        })
//@ResourceAdapter("resources/activemq-rar-5.15.8.rar")
@Singleton
public class ConsumerMessageListener  implements Serializable {

    private static Logger logger = LogManager.getLogger(ConsumerMessageListener.class);

    @Inject
    private ProductService productService;

//    public void onMessage(Message message) {
//        TextMessage textMessage = (TextMessage) message;
//        try {
//            if("updated".equals(textMessage.getText())){
//                productService.updateProducts();
//            }
//        } catch (JMSException e) {
//            e.printStackTrace();
//        }
//    }


    private static ActiveMQConnectionFactory connectionFactory = null; //управляемый объект от ApacheMQ
//служащий для создания объекта Connection.

    private static Connection connection = null; //сам Connection.

    private static Session session; //контекст для посылки и приема сообщений.

    private static Destination destination; //буфер отправки и получения сообщений.

    private static String queue = null; //имя очереди или топика.

//    public ConsumerMessageListener() {
//        receivedQueue();
//    }

    public static Boolean Connected() {
        try {
            if (connection == null) {
                connectionFactory = getConnectionFactory();
                connection = connectionFactory.createConnection();
                //получаем экзмпляр класса подключения
                connection.start(); //стартуем
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                //создаем объект сессию без транзакций
                //параметром AUTO_ACKNOWLEDGE мы указали что отчет о доставке будет
                //отправляться автоматически при получении сообщения.
            } else {
                connection.start();
            }
            return true;
        } catch (JMSException ex) {
            return false;
        }
    }

    private static ActiveMQConnectionFactory getConnectionFactory() {
        return new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "failover://tcp://localhost:61616");
    }

    private static Destination getDestinationQueue() {
        try {
            return session.createQueue(queue);
        } catch (JMSException ex) {
            return null;
        }
    }

    public void receivedQueue() {
        queue = "productQueue";
        if (Connected()) {
            destination = getDestinationQueue();
            if (destination != null) {
                try {
                    MessageConsumer consumer = session.createConsumer(destination);
                    consumer.setMessageListener(msg -> {
                        TextMessage textmessage = (TextMessage) msg;
                        try {
                            if("updated".equals(textmessage.getText())){
                                productService.updateProducts();
                            }
                            logger.log(Level.INFO, textmessage.getText());
                        } catch (JMSException ex) {
                            logger.log(Level.ERROR, ex);
                        }
                    });
                } catch (JMSException ex) {
                    logger.log(Level.ERROR, ex);
                }
            }
        }
    }

}