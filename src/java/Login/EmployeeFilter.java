/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import Models.Users;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Hillo
 */
@WebFilter(filterName = "SecureFilter", urlPatterns = {"/secure/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class EmployeeFilter implements Filter {
    
    private static final boolean debug = true;

    private FilterConfig filterConfig = null;
    
    public EmployeeFilter() {
    }    
    
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain)
        throws IOException, ServletException {
            HttpSession session = ((HttpServletRequest) request).getSession();
            try {
            Users u = (Users) session.getAttribute("user");
            if (u.getPermissionsId() == null)
            {
                request.setAttribute("message", "Insufficient permissions");
                request.getRequestDispatcher("/error").forward(request, response);
            }
            else
                chain.doFilter(request, response);
            }
            catch (Exception e)
            {
                request.setAttribute("message", "Insufficient permissions");
                request.getRequestDispatcher("/error").forward(request, response);
            }

    }

        

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
    }
    
}
