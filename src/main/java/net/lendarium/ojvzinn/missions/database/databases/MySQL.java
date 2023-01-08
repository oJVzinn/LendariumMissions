package net.lendarium.ojvzinn.missions.database.databases;

import net.lendarium.ojvzinn.missions.Main;
import net.lendarium.ojvzinn.missions.database.DataBase;
import net.lendarium.ojvzinn.missions.database.interfaces.DatabaseInterface;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.sql.*;
import java.util.Locale;

public class MySQL extends DataBase implements DatabaseInterface<MySQL> {

    private final Main main;
    public Connection connection;

    public MySQL(Main main) {
        this.main = main;
    }

    @Override
    public void setupDataBase() {}

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public void createTable(String table) {}

    @Override
    public void closeConnection() {}

    public Main getMain() {
        return main;
    }
}
