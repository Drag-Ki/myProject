package zzg.o2o.interceptor.superadmin;

import org.springframework.web.servlet.HandlerInterceptor;
import zzg.o2o.entity.PersonInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class SuperAdminLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userObj=request.getSession().getAttribute("user");
        if(userObj!=null){
            PersonInfo user=(PersonInfo) userObj;
            if(user!=null&&user.getUserId()!=null&&user.getUserId()>0&&user.getUserType()==3)
                return true;
        }
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open ('" + request.getContextPath() + "/superadmin/login','_top')");
        out.println("</script>");
        out.println("</html>");

        return false;

    }
}
