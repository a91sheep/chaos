package com.store59.printapi.common.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReceiverService {

    @Value("${rabbitmq.event.print.entry}")
    private String queueName;

    @Value("${rabbitmq.event.print.queue.gala}")
    private String galaQueueName;

    @Autowired
    GalaReceievr galaReceievr;
    @Autowired
    Receiver     receiver;
    @Autowired
    PicReceiver  picReceiver;
    @Autowired
    private OrderPicReceiver orderPicReceiver;
    @Autowired
    private OrderDocReceiver orderDocReceiver;
    private static Logger logger = LoggerFactory.getLogger(ReceiverService.class);

    @RabbitListener(queues = "${rabbitmq.event.print.queue.gala}")
    public void receiveGalaMessage(String message) throws Exception {
        logger.info("队列开始：" + galaQueueName);
        galaReceievr.receiveGalaMessage(message);
    }

    @RabbitListener(queues = "${rabbitmq.event.print.entry}")
    public void receiver(String message) throws Exception {
        logger.info("队列开始：" + queueName);
        receiver.receive(message);
    }

    @RabbitListener(queues = "printdoc.order.paid.user")
    public void orderDocReceiver(String message) throws Exception {
        logger.info("队列开始：" + "printdoc.order.paid.user");
        orderDocReceiver.orderDocReceive(message);
    }

    @RabbitListener(queues = "printphoto.order.paid.user")
    public void orderPicReceiver(String message) throws Exception {
        logger.info("队列开始：" + "printphoto.order.paid.user");
        orderPicReceiver.orderPicReceive(message);
    }

//	@RabbitListener(queues = "print-pic-convert")
//	public void receiverPic(String message)throws Exception{
//		logger.info("队列开始："+"queueName");
//		picReceiver.receive(message);
//	}
}
