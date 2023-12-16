package com.akkulov.telegrambot.controller;

import com.akkulov.telegrambot.config.TelegramBotProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс телеграм-бота.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotController extends TelegramLongPollingBot {

  private final TelegramBotProperties telegramBotProperties;

  @Override
  public void onUpdateReceived(Update update) {
    if (update.getMessage() == null) {
      return;
    }

    log.info("Incoming message: chatId={}, username={}, message={}",
        update.getMessage().getChatId(),
        update.getMessage().getFrom().getUserName(),
        update.getMessage().getText()
    );

    // если сообщение просто отредачили, то ничего не делать
    if (update.getEditedMessage() != null) {
      return;
    }

    // если отправили не текст, а что-то другое
    if (update.getMessage().getText() == null) {
      processUnsupportedMessageType(update);
    }

    var sendMessage = SendMessage.builder()
        .chatId(update.getMessage().getChatId())
        .text(update.getMessage().getText())
        .build();

    try {
      execute(sendMessage);
      log.info("Success send response to user: text={}, chatId={}",
          sendMessage.getText(),
          sendMessage.getChatId()
      );
    } catch (TelegramApiException e) {
      val msg = String.format("Error while during sending message to user: text=%s, chatId=%s",
          sendMessage.getText(),
          sendMessage.getChatId()
      );
      log.error(msg);
      throw new IllegalStateException(msg, e);
    }
  }

  /**
   * Обработать неподдерживаемый вид сообщения.
   *
   * @param update апдейт
   */
  private void processUnsupportedMessageType(Update update) {
    log.error("Unsupported message type from chatId: chatId={}", update.getMessage().getChatId());

    var sendMessage = SendMessage.builder()
        .chatId(update.getMessage().getChatId())
        .text("Неподдерживаемый вид сообщения!")
        .build();

    try {
      execute(sendMessage);
      log.info("Success send response to user: text={}, chatId={}",
          sendMessage.getText(),
          sendMessage.getChatId()
      );
    } catch (TelegramApiException e) {
      log.warn("Error while during sending message to user: text={}, chatId={}",
          sendMessage.getText(),
          sendMessage.getChatId());
      throw new IllegalStateException(e);
    }
  }

  @Override
  public String getBotUsername() {
    return telegramBotProperties.getUsername();
  }

  @Override
  public String getBotToken() {
    return telegramBotProperties.getToken();
  }
}
