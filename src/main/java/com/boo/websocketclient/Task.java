package com.boo.websocketclient;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
    private Long id;
    private String name;

}