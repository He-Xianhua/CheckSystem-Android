package student;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ChangePasswordServlet extends HttpServlet {
    private String userName;
    private String newPassword;
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse responce) throws IOException {
        this.doPost(request,responce);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String acceptjson;
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
            StringBuffer sb = new StringBuffer();
            String temp;
            while((temp = br.readLine()) != null){
                sb.append(temp);
            }
            br.close();
            acceptjson = sb.toString();
            System.out.println("jsonis:______"+ acceptjson);
            if(!acceptjson.isEmpty()){
                JSONObject jo = JSONObject.fromObject(acceptjson);
                userName = (String)jo.get("UserName");
                newPassword = (String)jo.get("NewPassword");
                out.write("success");
            }else{
                System.out.println("get the json failed");
                out.write("failed");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        out.flush();
        out.close();


    }

}
