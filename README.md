# Nurigo Java SDK

### 실행방법

---

#### 단일 메시지 발송

```
DefaultMessageService messageService = NurigoApp.INSTANCE.initialize("API Key", "API Secret Key", "Nurigo API URL");
Message message = new Message();
message.setFrom("발신번호");
message.setTo("수신번호");
message.setText("메시지 내용");
SingleMessageSendingRequest singleMessageSendingRequest = new SingleMessageSendingRequest(message);
messageService.sendOne(singleMessageSendingRequest);
```
