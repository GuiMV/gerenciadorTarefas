package com.supero.gerenciador.tarefas.mb;

import com.supero.gerenciador.tarefas.exception.HandledException;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class BasicMb implements Serializable {

    public void addErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null);
        context.addMessage(null, facesMessage);
    }

    public void addInfoMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
        context.addMessage(null, facesMessage);
    }

    public void addWarnMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, message, null);
        context.addMessage(null, facesMessage);
    }

    public void addFatalMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, message, null);
        context.addMessage(null, facesMessage);
    }

    public static void putOnSession(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put(key, value);
    }

    public static void removeFromSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().remove(key);
    }

    public static Object getOnSession(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().get(key);
    }

    public static <T> T getOnSession(String key, Class<T> classe) {
        return classe.cast(getOnSession(key));
    }

    public static void putOnFlash(String key, Object value) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().put(key, value);
    }

    public static Object getOnFlash(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getFlash().get(key);
    }

    public static  <T> T getOnFlash(String key, Class<T> classe) {
        return classe.cast(getOnFlash(key));
    }

    public void redirect(String url) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect(url);
        } catch (IOException ex) {
            throw new HandledException("Erro ao redirecionar Pagina!", ex);
        }
    }

    public void redirectOnContextPath(String url) {
        FacesContext context = FacesContext.getCurrentInstance();
        redirect(context.getExternalContext().getRequestContextPath() + url);
    }

    public void showModal(String name) {
        FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("$('#" + name + "').modal('show');");
    }

    public void hideModal(String name) {
        FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("$('#" + name + "').modal('hide');");
    }

}
