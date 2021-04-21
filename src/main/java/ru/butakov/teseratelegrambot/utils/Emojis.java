package ru.butakov.teseratelegrambot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emojis {
    SETTINGS(EmojiParser.parseToUnicode(":hammer_and_wrench:")),
    WARNING(EmojiParser.parseToUnicode(":warning:")),
    PAPERCLIP(EmojiParser.parseToUnicode(":paperclip:")),
    WASTEBASKET(EmojiParser.parseToUnicode(":wastebasket:")),
    COMMENT(EmojiParser.parseToUnicode(":speech_balloon:")), // comment
    THOUGHT(EmojiParser.parseToUnicode(":thought_balloon:")), // thought
    NEWS(EmojiParser.parseToUnicode(":envelope_with_arrow:")), // news
    ARTICLE(EmojiParser.parseToUnicode(":book:")), // article
    JOURNAL(EmojiParser.parseToUnicode(":ledger:")), // journal
    DISABLED(EmojiParser.parseToUnicode(":heavy_multiplication_x:")),
    ENABLED(EmojiParser.parseToUnicode(":heavy_check_mark:")),
//    ENABLED(EmojiParser.parseToUnicode(":white_check_mark:")),


    ;


    private String emoji;

    @Override
    public String toString() {
        return emoji;
    }
}
