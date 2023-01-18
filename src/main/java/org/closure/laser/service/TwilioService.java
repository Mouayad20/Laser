package org.closure.laser.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "ACcd4b28277786679b2d33ee37f2518c2a";
    public static final String AUTH_TOKEN = "2347f068f5924ed2ae6e9b8bee851a6c";

    // @PostConstruct
    public static void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message
            .creator(
                new PhoneNumber("+963987055364"),
                new PhoneNumber("+12058832417"),
                "This is the ship that made the Kessel Run in fourteen parsecs?"
            )
            .create();

        System.out.println("=================##############" + message.getSid());
    }
}
