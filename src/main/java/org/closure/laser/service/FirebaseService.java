package org.closure.laser.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.closure.laser.domain.UserApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    @Autowired
    ResourceLoader resourceLoader;

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    Logger logger = LoggerFactory.getLogger(FirebaseService.class);

    @PostConstruct
    public void initialize() {
        /** */
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
                .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public String sendNotification(UserApplication user, String token, Long offer_id) {
        Notification notification = Notification.builder().build();

        notification =
            Notification.builder().setTitle("Deal request").setBody(user.getUser().getFirstName() + " want to make deal with you").build();
        Message packet = Message.builder().setToken(token).setNotification(notification).putData("offer_id", offer_id.toString()).build();
        String s = "Notifaction not sent";
        try {
            s = FirebaseMessaging.getInstance().send(packet);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

        return s;
    }
    /*
     * public void sendMessage(Map<String, String> data, PushNotificationRequest
     * request)
     * throws InterruptedException, ExecutionException {
     * Message message = getPreconfiguredMessageWithData(data, request);
     * Gson gson = new GsonBuilder().setPrettyPrinting().create();
     * String jsonOutput = gson.toJson(message);
     * String response = sendAndGetResponse(message);
     * logger.info("Sent message with data. Topic: " + request.getTopic() + ", " +
     * response + " msg " + jsonOutput);
     * }
     *
     * public void sendMessageWithoutData(PushNotificationRequest request)
     * throws InterruptedException, ExecutionException {
     * Message message = getPreconfiguredMessageWithoutData(request);
     * String response = sendAndGetResponse(message);
     * logger.info("Sent message without data. Topic: " + request.getTopic() + ", "
     * + response);
     * }
     *
     * public void sendMessageToToken(PushNotificationRequest request)
     * throws InterruptedException, ExecutionException {
     * Message message = getPreconfiguredMessageToToken(request);
     * Gson gson = new GsonBuilder().setPrettyPrinting().create();
     * String jsonOutput = gson.toJson(message);
     * String response = sendAndGetResponse(message);
     * logger.info(
     * "Sent message to token. Device token: " + request.getToken() + ", " +
     * response + " msg " + jsonOutput);
     * }
     *
     * private String sendAndGetResponse(Message message) throws
     * InterruptedException, ExecutionException {
     * return FirebaseMessaging.getInstance().sendAsync(message).get();
     * }
     *
     * private AndroidConfig getAndroidConfig(String topic) {
     * return AndroidConfig.builder()
     * .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
     * .setPriority(AndroidConfig.Priority.HIGH)
     * .setNotification(AndroidNotification.builder().setSound(NotificationParameter
     * .SOUND.getValue())
     * .setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build())
     * .build();
     * }
     *
     * private ApnsConfig getApnsConfig(String topic) {
     * return ApnsConfig.builder()
     * .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
     * }
     *
     * private Message getPreconfiguredMessageToToken(PushNotificationRequest
     * request) {
     * return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
     * .build();
     * }
     *
     * private Message getPreconfiguredMessageWithoutData(PushNotificationRequest
     * request) {
     * return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
     * .build();
     * }
     *
     * private Message getPreconfiguredMessageWithData(Map<String, String> data,
     * PushNotificationRequest request) {
     * return
     * getPreconfiguredMessageBuilder(request).putAllData(data).setToken(request.
     * getToken())
     * .build();
     * }
     *
     * private Message.Builder
     * getPreconfiguredMessageBuilder(PushNotificationRequest request) {
     * AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
     * ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
     * return Message.builder()
     * .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
     * new Notification(request.getTitle(), request.getMessage()));
     * }
     */
}
