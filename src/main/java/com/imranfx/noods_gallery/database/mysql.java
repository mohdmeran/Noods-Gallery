
package com.imranfx.noods_gallery.database;

// import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author black
 */
public class mysql {
    
    String url;
    String user;
    String pass;
    Connection connection;
    
    public mysql() {
        Dotenv dotenv = Dotenv.configure()
        .directory(".\\")
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();
        
        this.url = dotenv.get("env_url");
        this.user = dotenv.get("env_user");
        this.pass = dotenv.get("env_pass");
        this.connect();
    }
    
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to database" + url);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
    }
    
    public void query(String q) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(q);
        } catch (SQLException ex) {

        }
    }
    
    public ResultSet query_result(String q) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(q);
            return result;
        } catch (SQLException ex) {
            System.out.println("Query search failed");
        }
        
        return null;
    }
    
    public ResultSet search_index(String q) {
        String query = "select * from image where match(name, description) against ('" + q + "' IN NATURAL LANGUAGE MODE)";
        return query_result(query);
    }
}
