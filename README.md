# Nurigo Java SDK

### 실행방법

---

#### 단일 메시지 발송

```java
DefaultMessageService instance = SolapiClient.INSTANCE.createInstance("ENTER_YOUR_API_KEY", "ENTER_YOUR_API_SECRET_KEY");
Message message = new Message();

message.setFrom("발신번호");
message.setTo("수신번호");
message.setText("메시지 내용");

instance.send(message);
```
