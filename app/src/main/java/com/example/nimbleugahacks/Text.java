//package com.example.nimbleugahacks;
//
////import com.twilio.Twilio;
////import com.twilio.rest.api.v2010.account.Message;
////import com.twilio.type.PhoneNumber;
//
//public class Text {
//
//        // Find your Account Sid and Token at twilio.com/console
//        // and set the environment variables. See http://twil.io/secure
//        public static final String ACCOUNT_SID = "ACebfa842d4f2d9234e9c969bd31451842";
//        public static final String AUTH_TOKEN = "d15a27963bc8004bcddf4faa82015325";
//        //System.getenv("TWILIO_AUTH_TOKEN")
//
//        public static void main() {
//            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//            Message message = Message
//                    .creator(new PhoneNumber("+14159352345"), // to
//                            new PhoneNumber("+17025087956"), // from
//                            "Testing?")
//                    .create();
//
//            System.out.println(message.getSid());
//        }
//}
//
