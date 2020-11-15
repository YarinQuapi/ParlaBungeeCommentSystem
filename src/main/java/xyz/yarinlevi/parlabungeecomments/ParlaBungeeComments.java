package xyz.yarinlevi.parlabungeecomments;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import xyz.yarinlevi.parlabungeecomments.commands.ProofCommand;
import xyz.yarinlevi.parlabungeecomments.constants.SQLQueries;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class ParlaBungeeComments extends Plugin {

    @Getter public static ParlaBungeeComments instance;
    @Getter private Configuration bungeeConfiguration;
    @Getter @Setter private Configuration pluginConfig;
    @Getter private static final String prefix = "";

    private HikariDataSource dataSource;
    @Getter private String table;
    @Getter private Connection connection;
    @Getter private int dataSavingThreadID;

    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        CommandSender commandSender = getProxy().getConsole();

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bungeeConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File("config.yml"));
            pluginConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        String hostName = Preconditions.checkNotNull(pluginConfig.getString("MySql_Hostname"));
        table = Preconditions.checkNotNull(pluginConfig.getString("MySql_TableName"));
        String database = Preconditions.checkNotNull(pluginConfig.getString("MySql_Database"));
        int port = pluginConfig.getInt("MySql_Port");
        String user = Preconditions.checkNotNull(pluginConfig.getString("MySql_User"));
        String pass = Preconditions.checkNotNull(pluginConfig.getString("MySql_Password"));
        boolean ssl = pluginConfig.getBoolean("MySql_UseSSL");

        dataSource = new HikariDataSource();

        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", hostName);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", user);
        dataSource.addDataSourceProperty("password", pass);
        dataSource.addDataSourceProperty("useSSL", ssl);

        String sql = SQLQueries.Comments.createTable;

        getProxy().getScheduler().runAsync(this, new Runnable() {
            @Override
            public void run() {
                try {
                    getLogger().warning("Please await mysql hook...");
                    connection = dataSource.getConnection();
                    Statement statement = connection.createStatement(); {
                        statement.executeUpdate(sql);
                    }
                    getLogger().info("MySQL Hooked!");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        getProxy().getPluginManager().registerCommand(this, new ProofCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
