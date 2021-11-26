
package com.imranfx.noods_gallery.database;

// import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author black
 */
public class mysql {
    
    String url;
    String user;
    String pass;
    String tableName;
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
        this.tableName = dotenv.get("env_tableName");
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
            System.out.println("Query failed");
            System.out.println(ex);
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
        String query = "select * from " + tableName + " where (name like '%" + q + "%') or (description like '%" + q + "%')";
        return query_result(query);
    }
    
    public ResultSet get_all_image(){
        String query = "select * from " + tableName;
        return query_result(query);
    }
    
    public void add_image(String name, String description) {
        String q = String.format("insert into %s(name, description) values ('%s', '%s');", tableName, name, description);
        this.query(q);
    }
    
    public void add_image(String name) {
        this.add_image(name, "");
    }
    
    public void update_description(String imgName, String desc) {
        String q = String.format("update %s set description = '%s' where name = '%s'", tableName, desc, imgName);
        this.query(q);
    }
    
    public void delete_image(String name) {
        String q = String.format("delete from %s where (name like '%s')", tableName, name);
        this.query(q);
    }
    
    public boolean isEmpty() {
        try {
            String q = String.format("select count(name) as total from %s", tableName);
            
            ResultSet r = this.query_result(q);
            
            while(r.next()){
                System.out.println(r.getInt("total"));
                return !(r.getInt("total") > 0);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return false;
    }
}
