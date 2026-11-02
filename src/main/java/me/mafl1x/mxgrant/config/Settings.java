package me.mafl1x.mxgrant.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class Settings {

    @Comment("Database settings. type: mysql or sqlite")
    public DatabaseConfig database = new DatabaseConfig();

    @Comment("Group priorities. Higher priority = better group. Used to prevent downgrading.")
    public List<Priority> priorities = Arrays.asList(
            new Priority("admin", 999),
            new Priority("king", 10),
            new Priority("knight", 2)
    );

    @Comment("Limits per granter group. group = granter's group, groupLimits = what they can give and how many times.")
    public List<Limit> limits = Arrays.asList(
            new Limit("admin", Arrays.asList(
                    new GroupLimit("knight", 5),
                    new GroupLimit("king", 10)
            ))
    );

    @Configuration
    public static class DatabaseConfig {
        @Comment("Database type: mysql or sqlite")
        public String type = "sqlite";

        public MySQLConfig mysql = new MySQLConfig();
        public SQLiteConfig sqlite = new SQLiteConfig();
    }

    @Configuration
    public static class MySQLConfig {
        public String host = "127.0.0.1";
        public int port = 3306;
        public String database = "mxgrant";
        public String user = "root";
        public String password = "password";
    }

    @Configuration
    public static class SQLiteConfig {
        public String file = "data.db";
    }

    @Configuration
    public static class Priority {
        public String group = "";
        public int priority = 0;

        public Priority() {}

        public Priority(String group, int priority) {
            this.group = group;
            this.priority = priority;
        }

        public String group() { return group; }
        public int priority() { return priority; }
    }

    @Configuration
    public static class Limit {
        public String group = "";
        public List<GroupLimit> groupLimits = List.of();

        public Limit() {}

        public Limit(String group, List<GroupLimit> groupLimits) {
            this.group = group;
            this.groupLimits = groupLimits;
        }

        public String group() { return group; }
        public List<GroupLimit> groupLimits() { return groupLimits; }
    }

    @Configuration
    public static class GroupLimit {
        public String group = "";
        public int limit = 0;

        public GroupLimit() {}

        public GroupLimit(String group, int limit) {
            this.group = group;
            this.limit = limit;
        }

        public String group() { return group; }
        public int limit() { return limit; }
    }
}
