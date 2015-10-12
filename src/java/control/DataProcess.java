/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author NguyenVanAi
 */
public class DataProcess {

    private static Connection connect;
    public static String host = "localhost:8080";
    private final static String databaseUrl = "jdbc:mysql://127.0.0.1:3306/test?user=root&password=admin";
    private final static String imagePath = "package";
    private String htmlPlayer = "<tr>\n"
            + "    <td class=\"p-name\">\n"
            + "        <a href=\"\">%s</a>\n"
            + "        <br>\n"
            + "            <small>ID: %s</small>\n"
            + "        </td>\n"
            + "        <td class=\"p-team\">\n"
            + "            <a href=\"#\">\n"
            + "                <img alt=\"image\" title=\"image\" onerror=\"this.src='./package/default.jpg'\" class=\"\" src=\"%s\">\n"
            + "                </a>\n"
            + "            <a href=\"%s\">\n"
            + "                <img alt=\"image\" title=\"%s\" class=\"\" src=\"%s\">\n"
            + "                </a>\n"
            + "                    </td>\n"
            + "                    <td class=\"p-progress\">\n"
            + "                        <div class=\"progress progress-xs\">\n"
            + "                            <div style=\"width: 87%%\" class=\"progress-bar progress-bar-success\"></div>\n"
            + "                        </div>\n"
            + "                        <small>87 Complete </small>\n"
            + "                    </td>\n"
            + "                    <td>\n"
            + "                        <span class=\"label label-primary\">Active</span>\n"
            + "                    </td>\n"
            + "                    <td>\n"
            + "                        <a href=\"project_details.html\" class=\"btn btn-primary btn-xs\">\n"
            + "                            <i class=\"fa fa-folder\"></i> View \n"
            + "                    \n"
            + "                        </a>\n"
            + "                        <a href=\"#\" class=\"btn btn-info btn-xs\">\n"
            + "                            <i class=\"fa fa-pencil\"></i> Edit \n"
            + "                    \n"
            + "                        </a>\n"
            + "                        <a href=\"#\" class=\"btn btn-danger btn-xs\">\n"
            + "                            <i class=\"fa fa-trash-o\"></i> Delete \n"
            + "                    \n"
            + "                        </a>\n"
            + "                    </td>\n"
            + "                </tr>";

    public Connection findConnection() {
        if (connect == null) {
            connect = getConnection();
        }
        return connect;
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                con = DriverManager.getConnection(databaseUrl);
                System.out.println("MySQL JDBC Driver Registered !!");
            } catch (SQLException ex) {
                Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("MySQL JDBC Driver not found !!");
        }
        return con;
    }

    /**
     * Nhập số trang hiển thị và row bắt đầu
     *
     * @return String html
     */
    public String getAllPlayers(int page, int number) {
        String response = "";
        String query = "select * from tna_player INNER JOIN tna_club ON tna_player.id=tna_club.player_id  ORDER BY tna_player.name LIMIT %d,%d";
        try {
            Statement st = (Statement) findConnection().createStatement();
            ResultSet rs = st.executeQuery(String.format(query, page, number));
            while (rs.next()) {
                //1: name 2:ID 3:
                response += String.format(htmlPlayer, rs.getString(4), rs.getString(1),"./"+imagePath+"/player/"+rs.getString(1)+".png","","","./"+imagePath+"/club/"+rs.getString(2)+".png");
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public String getTotalPlayer() {
        String query = "SELECT COUNT(*) FROM tna_player";
        try {
            Statement st = (Statement) findConnection().createStatement();
            ResultSet rs = st.executeQuery(String.format(query));
            while (rs.next()) {
                return rs.getString(1);
            }
            rs.close();
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    public boolean AddEvent(Event eve) {
//        int result  = 0;
//            String query = "INSERT INTO tblEvent VALUES(?,?,?,?,?,?)";
//        try {
//            PreparedStatement ps = getConnection().prepareStatement(query);
//            ps.setString(1, eve.getTitle());
//            ps.setString(2, eve.getLocation());
//            ps.setInt(3, eve.getDuration());
//            ps.setDate(4, eve.getDatestart());
//            ps.setString(5, eve.getContent());
//            ps.setString(6, eve.getTypename());
//            result = ps.executeUpdate();
//            ps.close();
//            getConnection().close();
//        } catch (SQLException ex) {
//            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
//        }
//            
//        return result >0;
//    }
//    
//    public ArrayList<Event> getallEvents() {
//        ArrayList<Event> events = new ArrayList<>();
//        String query = "SELECT * FROM tblEvent";
//        try {
//            Statement st = getConnection().createStatement();
//            ResultSet rs = st.executeQuery(query);
//            while(rs.next()) {
//                Event eve = new Event(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDate(4), rs.getString(5), rs.getString(6));
//                events.add(eve);
//            }
//            rs.close();
//            st.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return events;
//    }
//    public ArrayList<Type> getallTypes() {
//        ArrayList<Type> types = new ArrayList<>();
//        String query = "SELECT * FROM tblType";
//        try {
//            Statement st = getConnection().createStatement();
//            ResultSet rs = st.executeQuery(query);
//            while(rs.next()) {
//                Type type = new Type(rs.getString(1));
//                types.add(type);
//            }
//            rs.close();
//            st.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return types;
//    }
//    public ArrayList<Event> getUpEvent() {
//        ArrayList<Event> events = new ArrayList<>();
//        String query = "select * from tblEvent where _datestart between CONVERT(CHAR(10), GETDATE(), 111) and CONVERT(CHAR(10), DATEADD (day,7,GETDATE() ),111)";
//        try {
//            Statement st = getConnection().createStatement();
//            ResultSet rs = st.executeQuery(query);
//            while(rs.next()) {
//                Event eve = new Event(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDate(4), rs.getString(5), rs.getString(6));
//                events.add(eve);
//            }
//            rs.close();
//            st.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return events;
//    }
//    public ArrayList<Event> getEventbyTitle(String title)
//    {
//       ArrayList<Event> events = new ArrayList<>();
//        String query = "select * from tblEvent where _title=?";
//        try {
//            PreparedStatement ps = getConnection().prepareStatement(query);
//            ps.setString(1, title);
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()) {
//                Event eve = new Event(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDate(4), rs.getString(5), rs.getString(6));
//                events.add(eve);
//            }
//            rs.close();
//            ps.close();
//        } catch (SQLException ex) {
//            Logger.getLogger(DataProcess.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return events;
//    }
}
