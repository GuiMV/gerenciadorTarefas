package com.supero.gerenciador.tarefas.filter;

import com.supero.gerenciador.tarefas.persistence.PersistenceUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class PersistenceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        EntityManager em = PersistenceUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction.begin();
            }
            chain.doFilter(request, response);
            if (transaction.getRollbackOnly()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            PersistenceUtil.closeEntityManager();
        }
    }

    @Override
    public void destroy() {
        PersistenceUtil.closeEntityManager();
    }

}
