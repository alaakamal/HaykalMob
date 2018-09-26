package mypak;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

public class GetEmpImg extends HttpServlet {
    @SuppressWarnings("compatibility:-4124079373790895110")
    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "image/gif";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @SuppressWarnings({ "oracle.jdeveloper.java.nested-assignment", "org.adf.emg.audits.analyzer.system-out-usage" })
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        String empNo = request.getParameter("empNo");
        //empNo = "174129";

        OutputStream os = response.getOutputStream();
        Connection conn = null;
        try {
            Context ctx = new InitialContext();
            DataSource ds;
            ds = (DataSource) ctx.lookup("jdbc/HrXEDSak");
            conn = ds.getConnection();
            PreparedStatement statement =
                conn.prepareStatement(" SELECT CASE\n" +
                                      "          WHEN (length(doc_fix_no)=3 or length(doc_fix_no)>6) \n" +
                                      "          THEN\n" + "             (SELECT doc_image\n" +
                                      "                FROM DOCUMENTAION_INFORMATION\n" +
                                      "               WHERE doc_fix_no = 6008)\n" + "          ELSE\n" +
                                      "             doc_image\n" + "       END\n" + "          AS doc_image\n" +
                                      "  FROM DOCUMENTAION_INFORMATION\n" + " WHERE DOC_fix_no = ?");
            statement.setString(1, empNo);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob("DOC_IMAGE");
                InputStream in = blob.getBinaryStream();
                byte[] buffer = new byte[(int) blob.length()];
                while ((in.read(buffer, 0, (int) blob.length())) != -1) {
                    os.write(buffer, 0, (int) blob.length());
                }
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println(sqle.toString());
            }
        }
    }
}

