package xyz.yarinlevi.parlabungeecomments.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import xyz.yarinlevi.parlabungeecomments.ParlaBungeeComments;
import xyz.yarinlevi.parlabungeecomments.constants.SQLQueries;
import xyz.yarinlevi.parlabungeecomments.helpers.Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProofCommand extends Command {
    public ProofCommand() {
        super("proof", "parlabungeecomments.proof", "comment");
    }

    @Override
    public void execute(CommandSender cmdSender, String[] args) {
        if (cmdSender instanceof ProxiedPlayer) {
            ProxiedPlayer sender = (ProxiedPlayer) cmdSender;

            if (args.length == 0) {
                Utilities.sendMessage(sender, "&eWrong syntax, try: /proof <player> [comment]");
            } else if (args.length == 1) {
                ProxiedPlayer player = ParlaBungeeComments.getInstance().getProxy().getPlayer(args[0]);

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                Connection connection = ParlaBungeeComments.getInstance().getConnection();

                try {
                    preparedStatement = connection.prepareStatement(String.format("SELECT * FROM `%s` WHERE `entity`=\"%s\"", SQLQueries.Comments.table, player.getUniqueId().toString()));

                    resultSet = preparedStatement.executeQuery();

                    StringBuilder stringBuilder = new StringBuilder("Comments of " + player.getName() + ": \n");
                    while (resultSet.isBeforeFirst()) {
                        stringBuilder.append(resultSet.getInt("id") + ", comment: " + resultSet.getString("note") + " commented by: " + resultSet.getString("staff") + " at: " + resultSet.getDate("date").toString() + "\n");
                    }

                    Utilities.sendMessage(sender, stringBuilder.toString());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
