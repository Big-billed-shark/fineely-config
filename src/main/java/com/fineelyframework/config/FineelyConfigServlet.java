package com.fineelyframework.config;

import com.alibaba.fastjson.JSONObject;
import com.fineelyframework.config.core.entity.ConfigSupport;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Processing requests for configuration classes
 *
 * <p>/get[className] and /update[className]
 *
 * @author Rey Kepler
 * @since 0.0.1
 * @see ConfigSupport
 * @see EnableAutoConfigScan
 */
public class FineelyConfigServlet extends HttpServlet {

    private ConfigIntermediary configIntermediary;

    /**
     * Init configIntermediary by webApplicationContext
     * Called by the servlet container to indicate to a servlet that the servlet
     * is being placed into service. See {@link Servlet#init}.
     * <p>
     * This implementation stores the {@link ServletConfig} object it receives
     * from the servlet container for later use. When overriding this form of
     * the method, call <code>super.init(config)</code>.
     *
     * @param config
     *            the <code>ServletConfig</code> object that contains
     *            configuration information for this servlet
     * @exception ServletException
     *                if an exception occurs that interrupts the servlet's
     *                normal operation
     * @see UnavailableException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        WebApplicationContext cont = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        configIntermediary = (ConfigIntermediary) cont.getBean("configIntermediary");
        super.init();
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        String className = requestURI.replaceAll(configIntermediary.getRequestMapping() + "get", "");
        ConfigSupport configByObject = configIntermediary.getConfigByObject(className);
        responseWriter(response, configByObject);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        String className = requestURI.replaceAll(configIntermediary.getRequestMapping() + "update", "");
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            sb.append(line);
        String configObjString = sb.toString();
        configIntermediary.updateConfigByObject(className, configObjString);
        responseWriter(response, true);
    }

    private void responseWriter(HttpServletResponse response, Object value) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json");
        response.getWriter().print(JSONObject.toJSONString(value));
    }

}
