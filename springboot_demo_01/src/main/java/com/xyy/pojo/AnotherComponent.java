package com.xyy.pojo;

import lombok.Data;

import java.net.InetAddress;


@Data
public class AnotherComponent {
    private boolean enabled;
    private InetAddress remoteAddress;
}
