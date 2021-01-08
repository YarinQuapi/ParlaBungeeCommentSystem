package xyz.yarinlevi.parlabungeecomments.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.yarinlevi.parlabungeecomments.ParlaBungeeComments;
import xyz.yarinlevi.parlabungeecomments.constants.SQLQueries;
import xyz.yarinlevi.parlabungeecomments.constants.Type;
import xyz.yarinlevi.parlabungeecomments.exceptions.UUIDNotFoundException;
import xyz.yarinlevi.parlabungeecomments.helpers.Utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class CommentCommand extends Command {
    public CommentCommand() {
        super("comment", "parlabungeecomments.use", "addcomment");
    }

    Pattern pattern = Pattern.compile("([A-z0-9])\\w+");

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender cmdSender, String[] args) {
        if (cmdSender instanceof ProxiedPlayer) {

            ProxiedPlayer sender = (ProxiedPlayer) cmdSender;

            if (args.length == 0) {
                Utilities.sendMessage(sender, "&cNo arguments detected, check your syntax. (&e/comment <player> <comment>&c)");
            } else {

                if (args.length > 1) {
                    if (args[0].length() >= 3 && args[0].length() <= 16 && pattern.matcher(args[0]).matches()) {
                        try {
                            String uuid = ParlaBungeeComments.getInstance().getProxy().getPlayer(args[0]) != null ? ParlaBungeeComments.getInstance().getProxy().getPlayer(args[0]).getUUID() : Utilities.getUUIDOfUsername(args[0]);

                            Connection connection = ParlaBungeeComments.getInstance().getConnection();

                            StringBuilder sb = new StringBuilder();
                            for (int i = 1; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }

                            String sql = String.format("INSERT INTO `%s` (`entity`, `note`,`type`,`staff`) VALUES (\"%s\", \"%s\", \"%s\", \"%s\")", SQLQueries.Comments.table, uuid, sb.toString(), Type.NOTE, sender.getName());

                            try {
                                connection.prepareStatement(sql).execute();
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                            Utilities.sendMessage(sender, "&aSuccessfully added a comment to &b" + args[0]);
                        } catch (IOException | UUIDNotFoundException e) {
                            Utilities.sendMessage(sender, "&cThe UUID for the player you requested was not found.");
                        }
                    } else {
                        Utilities.sendMessage(sender, "&cThe player you requested does not exist.");
                    }
                } else {
                    Utilities.sendMessage(sender, "&cThe comment you've entered is invalid. Please try: &e/comment " + args[0] + " <comment>");
                }
            }
        }
    }
}
