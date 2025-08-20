# Nurigo Java SDK

## 🛑 해당 Java SDK는 더 이상 유지보수 되지 않습니다! 향후 Java/Kotlin SDK는 [SOLAPI SDK](https://github.com/solapi/solapi-kotlin)를 통해 이용 해 주세요.

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
