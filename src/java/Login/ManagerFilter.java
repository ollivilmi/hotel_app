package Login;

import Beans.UserBean;
import Models.Users;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Hillo
 */
@WebFilter(filterName = "ManagerFilter", urlPatterns = {"/r/users/manager/*", "/secure/manager/*", "/r/notes/manager/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class ManagerFilter implements Filter {
    
    private static final boolean debug = true;

    private FilterConfig filterConfig = null;
    
    public ManagerFilter() {
    }    
    
    @EJB
    private UserBean bean;
    
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain)
        throws IOException, ServletException {
            Cookie user = null;
            try {
                for (Cookie cookie : ((HttpServletRequest) request).getCookies())
                    if (cookie.getName().equals("user"))
                    {
                        user = cookie;
                        Users u = bean.getByUsername(user.getValue());
                        
                        if (u.getPermissionsId() != 1 && u.getPermissionsId() != null)
                            chain.doFilter(request, response);
                        else
                        {
                            request.setAttribute("message", "Insufficient permissions");
                            request.getRequestDispatcher("/error").forward(request, response);
                        }
                    }
            if (user == null) throw new NullPointerException();
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
