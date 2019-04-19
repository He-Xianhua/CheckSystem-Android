package student;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetImageServlet extends HttpServlet {
    private String path;
    private String result;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doPost(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setSizeThreshold(1023*1024*10);
        String upload = getServletContext().getRealPath("/");
        diskFileItemFactory.setRepository(new File(upload));
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        try{
            List<FileItem> list = servletFileUpload.parseRequest(request);

            for(FileItem item : list){
                String name = item.getName();
                System.out.println(name);
                InputStream is = item.getInputStream();
                if(name.contains("jpg")){
                    try {
                        //System.out.println("aa");
                        path = upload+"\\"+item.getName();
                        inputStream2File(is, path);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(upload);
            //out.write(path);
            result = "success";
        }catch(FileUploadException e){
            e.printStackTrace();
        }


        System.out.println("鎺ユ敹鏁版嵁鏄�"+result);
        out.println(result);
        out.flush();
        out.close();
    }
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
    public static void inputStream2File(InputStream is, String savePath) throws Exception {
        System.out.println("savedPath:" + savePath);
        File file = new File(savePath);
        BufferedInputStream fis = new BufferedInputStream(is);
        FileOutputStream fos = new FileOutputStream(file);
        int f;
        while ((f = fis.read()) != -1) {
            fos.write(f);
        }
        fos.flush();
        fos.close();
        fis.close();
        is.close();

    }
}
