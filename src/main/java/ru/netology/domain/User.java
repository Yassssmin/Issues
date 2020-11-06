package ru.netology.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Comparable<User> {
    private int id;
    private String name;
    private String avatarUrl;

    @Override
    public int compareTo(User o) {
        return this.getName().compareTo(o.getName());
    }
}
