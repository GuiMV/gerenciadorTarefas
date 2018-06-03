package com.supero.gerenciador.tarefas.exception;

import com.supero.gerenciador.tarefas.persistence.PersistenceUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.persistence.EntityManager;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    //Obtém uma instância do FacesContext
    final FacesContext facesContext = FacesContext.getCurrentInstance();

    //Obtém um mapa do FacesContext
    final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();

    //Obtém o estado atual da navegação entre páginas do JSF
    final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

    //Declara o construtor que recebe uma exceptio do tipo ExceptionHandler como parâmetro


    public CustomExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    //Sobrescreve o método ExceptionHandler que retorna a "pilha" de exceções
    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    //Sobrescreve o método handle que é responsável por manipular as exceções do JSF
    @Override
    public void handle() throws FacesException {

        final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            // Recupera a exceção do contexto
            Throwable exception = context.getException();

            // Aqui tentamos tratar a exeção
            try {
                EntityManager entityManager = PersistenceUtil.getEntityManager();
                entityManager.getTransaction().setRollbackOnly();

                exception.printStackTrace();
                String msg = getMessage(exception);

                // Avisa o usuário do erro
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));

                // Seta a navegação para uma página padrão.
//                navigationHandler.handleNavigation(facesContext, null, "/restrict/home.faces");
                // Renderiza a pagina de erro e exibe as mensagens
                facesContext.renderResponse();
            } finally {
                // Remove a exeção da fila
                iterator.remove();
            }
        }
        // Manipula o erro
        getWrapped().handle();
    }

    private String getMessage(Throwable exception) {
        if (exception instanceof HandledException) {
            return exception.getMessage();
        }
        if (Objects.nonNull(exception)) {
            return getMessage(exception.getCause());
        }
        return "Erro inesperado";
    }
}
