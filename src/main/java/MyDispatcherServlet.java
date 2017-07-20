import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.appl.controller.DepartmentController;
import ro.teamnet.zth.appl.controller.EmployeeController;
import ro.teamnet.zth.fmk.MethodAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Beatrice.Chiriac on 7/20/2017.
 */
public class MyDispatcherServlet extends HttpServlet {

   Map<String,MethodAttributes> allowedMethods;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      dispatchReply(request,response,"POST");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        dispatchReply(request,response,"GET");
    }

    public  void init(){

//        MethodAttributes value1;
//
//        value1.setMethodName(EmployeeController.class.getMethod("getAllEmployees", ).getName());
//        value1.setMethodType(EmployeeController.class.getMethod("getAllEmployees", ).);
//        value1.setMethodName(EmployeeController.class.getMethod("getAllEmployees", ).getName());
//
//        allowedMethods.put("/employees/all" + "GET",value1);
//        allowedMethods.put("/employees/one" + "GET", EmployeeController.class.getMethod("getOneEmployee",).getName());
//        allowedMethods.put("/departments/all" + "GET", DepartmentController.class.getMethod("getAllDepartments",).getName());
   }

    private void dispatchReply(HttpServletRequest request, HttpServletResponse response,String myType) throws IOException {

        try{
            Object resultToDisplay=dispatch(request);
            reply(response,resultToDisplay);


        }catch (Exception e){
            sendExceptionError(response);
        }
    }


    public Object dispatch(HttpServletRequest request){

       MyController myController=request.getClass().getDeclaredAnnotation(MyController.class);
        String res="";


            if(myController.urlPath().equals("/employees"))
            {
                MyRequestMethod myRequestMethod=request.getClass().getDeclaredAnnotation(MyRequestMethod.class);
                   if(myRequestMethod.urlPath().equals("/one")&&myRequestMethod.methodeType().equals("GET")){

                       Method method;
                       try{
                           method=EmployeeController.class.getMethod("getOneEmployee",String.class);
                           res=(String) method.invoke(new EmployeeController());

                       } catch (NoSuchMethodException e) {
                           e.printStackTrace();
                       } catch (IllegalAccessException e) {
                           e.printStackTrace();
                       } catch (InvocationTargetException e) {
                           e.printStackTrace();
                       }
                   }

                  else if(myRequestMethod.urlPath().equals("/all")&&myRequestMethod.methodeType().equals("GET")){
                       Method method;
                       try{
                           method=EmployeeController.class.getMethod("getAllEmployees");
                           res=(String) method.invoke(new EmployeeController());
                       } catch (NoSuchMethodException e) {
                           e.printStackTrace();
                       } catch (IllegalAccessException e) {
                           e.printStackTrace();
                       } catch (InvocationTargetException e) {
                           e.printStackTrace();
                       }


                   }
            }
            else if(myController.urlPath().equals("/departments"))
            {
                MyRequestMethod myRequestMethod=request.getClass().getDeclaredAnnotation(MyRequestMethod.class);
                if(myRequestMethod.urlPath().equals("/all")&&myRequestMethod.methodeType().equals("GET")){

                    Method method;
                    try{
                        method= DepartmentController.class.getMethod("getAllDepartments");
                        res=(String) method.invoke(new DepartmentController());

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }


        return res;
    }

    public void reply(HttpServletResponse response,Object o) throws IOException {

        response.setContentType("text/html");

        response.getWriter().write((String) o);


    }

    public void sendExceptionError(HttpServletResponse response) throws IOException {

        response.setContentType("text/html");

        response.getWriter().write("There is an error!");
    }
}
