package xyz.yarinlevi.parlabungeecomments.constants;

public class SQLQueries {
    public static class Comments {
        public static final String table = "bat_comments";

        public static final String createTable = "CREATE TABLE IF NOT EXISTS `" + table + "` ("
                + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                + "`entity` varchar(100) NOT NULL,"
                + "`note` varchar(255) NOT NULL,"
                + "`type` varchar(7) NOT NULL,"
                + "`staff` varchar(30) NOT NULL,"
                + "`date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "PRIMARY KEY (`id`),"
                + "INDEX(entity)"
                + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;";
    }
}
