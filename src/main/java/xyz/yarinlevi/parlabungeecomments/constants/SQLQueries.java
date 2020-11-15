package xyz.yarinlevi.parlabungeecomments.constants;

public class SQLQueries {
    public static class Comments{
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

        public static final String insertEntry = "INSERT INTO `" + table + "` (entity, note, type, staff)"
                + "VALUES (?, ?, ?, ?);";

        public static final String getEntries = "SELECT id, note, type, staff, date FROM `" + table + "` "
                + "WHERE entity = ? ORDER BY date DESC;";
        public static final String getManagedEntries = "SELECT id, note, type, date, entity FROM `" + table + "` "
                + "WHERE staff = ? ORDER BY date DESC;";

        public static final String getMostRecentCommentDate = "SELECT date FROM `" + table + "` WHERE entity = ? ORDER BY date DESC";

        public static final String clearEntries = "DELETE FROM `" + table + "` WHERE entity = ?;";

        public static final String clearByID = "DELETE FROM `" + table + "` WHERE entity = ? AND id = ?;";

        public static final String simpleTriggerCheck = "SELECT COUNT(*) FROM `" + table + "` WHERE entity = ?;";
        public static final String patternTriggerCheck = "SELECT COUNT(*) FROM `" + table + "` WHERE entity = ? && note LIKE ?;";
    }
}
