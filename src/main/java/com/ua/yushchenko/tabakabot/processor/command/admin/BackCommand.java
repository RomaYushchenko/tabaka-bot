package com.ua.yushchenko.tabakabot.processor.command.admin;

import static com.ua.yushchenko.tabakabot.model.enums.TobaccoBotCommand.ORDERED_MENU;
import static com.ua.yushchenko.tabakabot.model.enums.TobaccoBotCommand.PLANNED_MENU;
import static com.ua.yushchenko.tabakabot.model.enums.TobaccoBotCommand.PROCESSING_ORDERS_MENU;
import static com.ua.yushchenko.tabakabot.model.enums.TobaccoBotCommand.START;

import java.util.List;

import com.ua.yushchenko.tabakabot.builder.ui.admin.MenuBuilder;
import com.ua.yushchenko.tabakabot.builder.ui.admin.ProcessOrderMenuBuilder;
import com.ua.yushchenko.tabakabot.model.domain.UserRequestModel;
import com.ua.yushchenko.tabakabot.model.enums.TobaccoBotCommand;
import com.ua.yushchenko.tabakabot.processor.command.TobaccoCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

/**
 * Class that represents the processing of the {@link TobaccoCommand} {@link TobaccoBotCommand#BACK} for Admin
 *
 * @author romanyushchenko
 * @version v.0.1
 */
@Slf4j
@Component("backCommandOfAdmin")
@RequiredArgsConstructor
public class BackCommand implements TobaccoCommand {

    @NonNull
    private final MenuBuilder menuBuilder;
    @NonNull
    private final ProcessOrderMenuBuilder processOrderMenuBuilder;

    @Override
    public BotApiMethod<?> buildMessage(final UserRequestModel model) {
        log.info("execute.E: [ADMIN] Processing {} command", getCommand());

        final Long chatId = model.getChatId();
        final Integer messageId = model.getMessageId();
        final List<Object> tobaccoBotCommands = model.getTobaccoBotCommands();

        if (tobaccoBotCommands.size() == 1) {
            log.error("Second bot command is null");
            return null;
        }

        final Object secondBotCommand = tobaccoBotCommands.get(1);

        switch ((TobaccoBotCommand) secondBotCommand) {
            case START -> {
                final var sendMessage = menuBuilder.buildTobaccoAdminMenu(chatId, messageId);

                log.info("execute.X: [ADMIN] Processed {} commands", List.of(getCommand(), START));
                return sendMessage;
            }
            case PROCESSING_ORDERS_MENU -> {
                final var sendMessage = processOrderMenuBuilder.buildProcessingOrdersMenu(chatId, messageId);

                log.info("execute.X: [ADMIN] Processed {} commands", List.of(getCommand(), PROCESSING_ORDERS_MENU));
                return sendMessage;
            }
            case PLANNED_MENU -> {
                final var sendMessage = processOrderMenuBuilder.buildPlannedMenu(chatId, messageId);

                log.info("execute.X: [ADMIN] Processed {} commands", List.of(getCommand(), PLANNED_MENU));
                return sendMessage;
            }
            case ORDERED_MENU -> {
                final var sendMessage = processOrderMenuBuilder.buildOrderedMenu(chatId, messageId);
                log.info("execute.X: [ADMIN] Processed {} commands", List.of(getCommand(), ORDERED_MENU));
                return sendMessage;
            }
            default -> {
                log.error("Unhandled second command: {}", secondBotCommand);
                return null;
            }
        }
    }

    @Override
    public TobaccoBotCommand getCommand() {
        return TobaccoBotCommand.BACK;
    }
}
