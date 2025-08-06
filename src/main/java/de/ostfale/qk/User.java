package de.ostfale.qk;

public class User {
    private final String username;
    private final int age;
    private final boolean vip;

    public User(String username, int age, boolean vip) {
        this.username = username;
        this.age = age;
        this.vip = vip;
    }

    public String getUsername() { return username; }
    public int getAge() { return age; }
    public boolean isVip() { return vip; }

    @Override
    public String toString() {
        return "User{username='" + username + "'}";
    }
}
