package com.hotel.booking.services.Impl;

import com.hotel.booking.dtos.VnPaymentDTO;
import com.hotel.booking.models.Booking;
import com.hotel.booking.models.Room;
import com.hotel.booking.models.Users;
import com.hotel.booking.payload.request.BookingRequest;
import com.hotel.booking.payload.response.MessageResponse;
import com.hotel.booking.payload.response.PaymentInfoResponse;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.RoomRepository;
import com.hotel.booking.repository.UserRepository;
import com.hotel.booking.services.ConfigPaymentService;
import com.hotel.booking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ConfigPaymentServiceImpl implements ConfigPaymentService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;
    private final StringRedisTemplate redisTemplate;
    private static final String tmnCode = "54AMN21G";
    private static final String version = "2.1.0";
    private static final String command = "pay";
    private static final String orderType = "other";
    private static final String ipAddress = "127.0.0.1";
    private static final String currCode = "VND";
    private static final String paymentDomain = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String location = "vn";
    private static final String returnUrl = "http://localhost:3000/payment";
    private static final String CHARACTERS = "0123456789";

    public ConfigPaymentServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Cacheable(value = "TxRef", key = "#email")
    public ResponseEntity<MessageResponse> createUrlPayment(String token, Double totalPrice) throws UnsupportedEncodingException {
        String email = userService.Authentication(token);
        int amount = totalPrice.intValue() * 100;
        String oderInfo = "Payment booking";
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("vnp_Version", version);
        urlParams.put("vnp_Command", command);
        urlParams.put("vnp_TmnCode", tmnCode);
        urlParams.put("vnp_Amount", String.valueOf(amount));
        urlParams.put("vnp_CurrCode", currCode);

        // add code TxnRef to redis
        String codeTxRef = initTxRef();
        urlParams.put("vnp_TxnRef", codeTxRef);
        redisTemplate.opsForValue().set(email, codeTxRef, 20, TimeUnit.MINUTES);
        System.out.println(codeTxRef);
        urlParams.put("vnp_OrderInfo", oderInfo);
        urlParams.put("vnp_OrderType", orderType);
        urlParams.put("vnp_Locale", location);
        urlParams.put("vnp_ReturnUrl", returnUrl);
        urlParams.put("vnp_IpAddr", ipAddress);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String createDate = formatter.format(cld.getTime());
        urlParams.put("vnp_CreateDate", createDate);
        cld.add(Calendar.MINUTE, 15);
        String expireDate = formatter.format(cld.getTime());
        urlParams.put("vnp_ExpireDate", expireDate);

        List<String> fieldNames = new ArrayList<>(urlParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hasData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = urlParams.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hasData.append(fieldName);
                hasData.append('=');
                hasData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hasData.append('&');
                }

            }
        }
        String queryUrl = query.toString();
        String secureHas = hmacSHA512("PTUUDWRXTGADIGKPCDQGZBHUKQPKJGJG", hasData.toString());
        queryUrl += "&vnp_SecureHash=" + secureHas;
        String paymentUrl = paymentDomain + "?" + queryUrl;
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("success");
        messageResponse.setResponseCode("200");
        messageResponse.setData(paymentUrl);
        return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
    }

    @Override
    public String initTxRef() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String createDate = formatter.format(cld.getTime());
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code + createDate;
    }

    @Override
    public ResponseEntity<?> handlePaymentResult(VnPaymentDTO paymentRequest, String token) {
        String email = userService.Authentication(token);
        String vnp_SecureHash = hmacSHA512("PTUUDWRXTGADIGKPCDQGZBHUKQPKJGJG", "");
        String vnpSecureHash = paymentRequest.getVnp_SecureHash();
        String orderInfo = paymentRequest.getVnp_OrderInfo();
        String vnpResponseCode = paymentRequest.getVnp_ResponseCode();
        String vnpTxnRefResponse = paymentRequest.getVnp_TxnRef();
        String vnpTmnCode = paymentRequest.getVnp_TmnCode();
        String vnpBankTranNo = paymentRequest.getVnp_BankTranNo();
        if (vnpBankTranNo == null || vnpBankTranNo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("vnp_BankTranNo is null");
        }
        String vpnBankCode = paymentRequest.getVnp_BankCode();
        String vnp_PayDate = paymentRequest.getVnp_PayDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        // Chuyển đổi chuỗi sang LocalDateTime
        LocalDate payDayFormat = LocalDate.parse(vnp_PayDate, formatter);
        //  xử lý múi giờ,  chuyển đổi LocalDateTime sang ZonedDateTime
        ZoneId zoneId = ZoneId.of("Etc/GMT-7");
//        ZonedDateTime payDayZonedDateTime = payDayFormat.atZone(zoneId);
//        System.out.println("LocalDateTime: " + localDateTime);
//        System.out.println("ZonedDateTime: " + zonedDateTime);

        if (vnpResponseCode.equals("00")) {
//            if (!vnpSecureHash.equals(vnp_SecureHash)) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("vnp_SecureHash incorrect");
//            }
            if (!vnpTmnCode.equals(tmnCode)) {
                return ResponseEntity.status(HttpStatus.OK).body("vnp_TmnCode incorrect");
            }
            if (vpnBankCode.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("vnp_BankCode is null");
            }

            // CHECK TxnRef
            String codeTxnRefFromRedis = redisTemplate.opsForValue().get(email);
            System.out.println(codeTxnRefFromRedis);
            System.out.println(vnpTxnRefResponse);
            if (vnpTxnRefResponse != null && vnpTxnRefResponse.equals(codeTxnRefFromRedis)) {
                PaymentInfoResponse paymentInfoResponse = new PaymentInfoResponse();
                paymentInfoResponse.setOrderInfo(orderInfo);
                paymentInfoResponse.setBankCode(vnpTxnRefResponse);
                paymentInfoResponse.setBankTranNo(vnpBankTranNo);
                paymentInfoResponse.setPayDay(payDayFormat);
                // save booking
                Users users = userRepository.findByEmail(email).orElse(null);
                ZoneId utcZone = ZoneId.of("UTC");
                ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
                for (BookingRequest request : paymentRequest.getBookingRequestList()) {
                    Booking booking = new Booking();
                    booking.setCheckIn(request.getCheckIn());
                    booking.setCheckOut(request.getCheckOut());
                    booking.setUser(users);
                    booking.setStatus("SUCCESS");
                    ZonedDateTime utcDateTime = ZonedDateTime.now(utcZone);
                    ZonedDateTime vietnamDateTime = utcDateTime.withZoneSameInstant(vietnamZone).plusHours(7);
                    booking.setBookingTime(vietnamDateTime);
                    booking.setPrice(request.getPrice());
                    booking.setCardId(request.getCardId());
                    Room room = roomRepository.findById(request.getRoomId()).orElse(null);
                    booking.setRoom(room);
                    bookingRepository.save(booking);
                }
                paymentInfoResponse.setMessage("success");
                paymentInfoResponse.setResCode("200");

                redisTemplate.delete(email);
                return ResponseEntity.status(HttpStatus.OK).body(paymentInfoResponse);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("vnp_TxnRef code is incorrect");
            }

        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Payment Failed");
        }
    }

    private String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

}
