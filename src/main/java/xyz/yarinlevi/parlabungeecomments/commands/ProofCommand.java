package xyz.yarinlevi.parlabungeecomments.commands;

import com.zaxxer.hikari.util.UtilityElf;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.chat.BaseComponentSerializer;
import xyz.yarinlevi.parlabungeecomments.ParlaBungeeComments;
import xyz.yarinlevi.parlabungeecomments.exceptions.UUIDNotFoundException;
import xyz.yarinlevi.parlabungeecomments.helpers.Utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ProofCommand extends Command {
    Pattern pattern = Pattern.compile("([A-z0-9])\\w+");
    Pattern urlPattern = Pattern.compile("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)");
    Pattern numberPattern = Pattern.compile("([0-9])+");

    public ProofCommand() {
        super("proof", "parlabungeecomments.use", "lookupproof", "lookupcomments");
    }

    @Override
    public void execute(CommandSender cmdSender, String[] args) {
        if (cmdSender instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) cmdSender;

            if (args.length == 0) {
                Utilities.sendMessage(sender, "&cNo arguments detected, check your syntax. (&e/lookupcomments <player> [number]&c)");
            } else {
                if (args[0].length() >= 3 && args[0].length() <= 16 && pattern.matcher(args[0]).matches()) {
                    try {
                        String uuid = ParlaBungeeComments.getInstance().getProxy().getPlayer(args[0]) != null ? ParlaBungeeComments.getInstance().getProxy().getPlayer(args[0]).getUUID() : Utilities.getUUIDOfUsername(args[0]);

                        int limit = 3;
                        if (args.length == 2) {
                            if (numberPattern.matcher(args[1]).matches()) {
                                limit = Integer.parseInt(args[1]);
                            } else {
                                Utilities.sendMessage(sender, "&cDefaulting to &63 &ccomments since the second argument is invalid.");
                            }
                        }

                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;

                        Connection connection = ParlaBungeeComments.getInstance().getConnection();

                        try {
                            preparedStatement = connection.prepareStatement(String.format("SELECT * FROM `bat_comments` WHERE `entity`=\"%s\" AND `type`=\"NOTE\" ORDER BY date DESC LIMIT " + limit, uuid));

                            resultSet = preparedStatement.executeQuery();


                            if (resultSet.isBeforeFirst()) {
                                TextComponent stringBuilder = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6Last " + limit + " comments of &b" + args[0] + "&6: \n"));
                                while (resultSet.next()) {
                                    //String[] noteArgs = resultSet.getString("note").split(" ");

                                    TextComponent textComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', String.format("&e -> &f%s &6by: &b%s &6at &b%s \n", resultSet.getString("note"), resultSet.getString("staff"), resultSet.getString("date"))));
                                    //String str = ChatColor.translateAlternateColorCodes('&', String.format("&eComment #%s &6-> &f%s &6by: &b%s &6at &b%s \n", resultSet.getInt("id"), resultSet.getString("note"), resultSet.getString("staff"), resultSet.getString("date")));
                                    stringBuilder.addExtra(textComponent);
                                }
                                Utilities.sendMessage(sender, stringBuilder);
                            } else {
                                Utilities.sendMessage(sender, "&cThis player has no comments.");
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    } catch (IOException | UUIDNotFoundException e) {
                        Utilities.sendMessage(sender, "&cThe UUID for the player you requested was not found.");
                    }
                } else {
                    Utilities.sendMessage(sender, "&cThe player you requested does not exist.");
                }
            }
        }
    }
}
