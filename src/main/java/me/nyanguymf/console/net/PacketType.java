package me.nyanguymf.console.net;

import java.io.Serializable;

public enum PacketType implements Serializable {
    STOP,
    INVALID_CREDENTIALS,
    INFO;
}
