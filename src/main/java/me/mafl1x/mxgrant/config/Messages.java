package me.mafl1x.mxgrant.config;

import de.exlll.configlib.Configuration;

@Configuration
public class Messages {
    public String PLAYER_IS_NULL = "&cИгрок не найден или не в сети.";
    public String SUCCESSFULLY_GIVE = "&a{granter} выдал группу {group} игроку {player}!";
    public String PLAYER_HAVE_NICE_GROUP = "&cУ игрока уже есть группа с таким же или более высоким приоритетом.";
    public String DO_NOT_HAVE_PERMISSIONS = "&cУ вас нет прав на эту команду.";
    public String CANT_GIVE_YOURSELF = "&cВы не можете выдать группу самому себе.";
    public String USAGE = "&cИспользование: /grant <ник>";
    public String NOT_A_PLAYER = "&cЭта команда только для игроков.";
    public String NO_GRANTS_AVAILABLE = "&cУ вас нет доступных выдач.";
}
