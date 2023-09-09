package com.jhelper.utils;

import java.util.Map;

public class TextTokenizer {

    private final String _text;
    private final char[] _buff;
    private int pos;
    private Map<String, String> customToken;

    public TextTokenizer(String text) {
        _text = text;
        _buff = text.toCharArray();
        this.pos = -1;
    }

    public TextTokenizer(String text, Map<String, String> customToken) {
        this(text);
        this.customToken = customToken;
    }

    public boolean isAlphaNumeric(char c) {

        if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
                || c == '_') {
            return true;
        }

        return false;
    }

    public boolean remaning() {
        return !(this.pos == _buff.length - 1);
    }

    public int position() {
        return this.pos;
    }

    public int position(int pos) {
        return this.pos += pos;
    }

    public char get() {
        return _buff[++this.pos];
    }

    public char get(int pos) {
        return _buff[this.pos + pos];
    }

    public String getString(int pos, int offset) {
        // pos += this.pos;
        int thisPos = pos + this.pos;
        return _text.substring(thisPos, Math.min(thisPos + offset, _buff.length));
    }

    public String getString(int offset) {
        return getString(0, offset);
    }

    public boolean hasNext() {
        return remaning();
    }

    public String next() {

        for (Map.Entry<String, String> elem : this.customToken.entrySet()) {

            String begin = elem.getKey(),
                    end = elem.getValue();

            char c = get(1);

            if (begin.equals(getString(1, begin.length()))) {

                StringBuilder append = new StringBuilder();
                append.append(begin);
                position(begin.length());

                while (remaning()) {

                    c = get();

                    if (end.charAt(0) == c) {
                        if (end.equals(getString(end.length()))) {
                            break;
                        }
                    }

                    append.append(c);
                }

                append.append(end);
                position(end.length() - 1);
                return append.toString();
            }

        }

        StringBuilder append = new StringBuilder();

        do {

            char c = get();

            append.append(c);

            if (!isAlphaNumeric(c)) {
                break;
            }

            if (!remaning()) {
                break;
            }

        } while (isAlphaNumeric(get(1)));

        return append.toString();
    }
}