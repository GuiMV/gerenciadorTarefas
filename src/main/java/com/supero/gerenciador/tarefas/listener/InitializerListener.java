package com.supero.gerenciador.tarefas.listener;

import com.supero.gerenciador.tarefas.persistence.PersistenceUtil;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("--> InitializerListener iniciando");
        PersistenceUtil.initializeDb();
        System.out.println("--> InitializerListener iniciado");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("--> InitializerListener destruido");
    }

}
