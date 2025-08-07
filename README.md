# Nurigo Java SDK

### 실행방법

---

#### 단일 메시지 발송

```java
DefaultMessageService solapiClient = SolapiClient.INSTANCE.createInstance("ENTER_YOUR_API_KEY", "ENTER_YOUR_API_SECRET_KEY");
Message solapiMessage = new Message();

solapiMessage.setFrom("발신번호");
solapiMessage.setTo("수신번호");
solapiMessage.setText("메시지 내용");

System.out.println(solapiClient.send(message));
```
