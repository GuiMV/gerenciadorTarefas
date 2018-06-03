package com.supero.gerenciador.tarefas.util;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.MessagesRenderer;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.*;

@FacesRenderer(componentFamily = "javax.faces.Messages", rendererType = "javax.faces.Messages")
public class NotyMessagesRenderer extends MessagesRenderer {

    private static final Attribute[] ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.MESSAGESMESSAGES);

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        boolean mustRender = shouldWriteIdAttribute(component);

        UIMessages messages = (UIMessages) component;
        ResponseWriter writer = context.getResponseWriter();
        assert (writer != null);

        String clientId = ((UIMessages) component).getFor();
        if (clientId == null && messages.isGlobalOnly()) {
            clientId = "";
        }

        Iterator messageIter = getMessageIter(context, clientId, component);

        assert (messageIter != null);

        if (!messageIter.hasNext()) {
            if (mustRender) {
                if ("javax_faces_developmentstage_messages".equals(component.getId())) {
                    return;
                }
                writer.startElement("div", component);
                writeIdAttributeIfNecessary(context, writer, component);
                writer.endElement("div");
            }
            return;
        }

        writeIdAttributeIfNecessary(context, writer, component);

        // style is rendered as a passthru attribute
        RenderKitUtils.renderPassThruAttributes(context, writer, component, ATTRIBUTES);

        Map<Severity, List<FacesMessage>> msgs = new HashMap<>();
        msgs.put(FacesMessage.SEVERITY_INFO, new ArrayList<>()); // Bootstrap info
        msgs.put(FacesMessage.SEVERITY_WARN, new ArrayList<>()); // Bootstrap warning
        msgs.put(FacesMessage.SEVERITY_ERROR, new ArrayList<>()); // Bootstrap error
        msgs.put(FacesMessage.SEVERITY_FATAL, new ArrayList<>()); // Bootstrap error

        while (messageIter.hasNext()) {
            FacesMessage curMessage = (FacesMessage) messageIter.next();

            if (curMessage.isRendered() && !messages.isRedisplay()) {
                continue;
            }
            msgs.get(curMessage.getSeverity()).add(curMessage);
        }

        List<FacesMessage> severityMessages = msgs.get(FacesMessage.SEVERITY_FATAL);
        if (severityMessages.size() > 0) {
            encodeSeverityMessages(context, messages, FacesMessage.SEVERITY_FATAL, severityMessages);
        }

        severityMessages = msgs.get(FacesMessage.SEVERITY_ERROR);
        if (severityMessages.size() > 0) {
            encodeSeverityMessages(context, messages, FacesMessage.SEVERITY_ERROR, severityMessages);
        }

        severityMessages = msgs.get(FacesMessage.SEVERITY_WARN);
        if (severityMessages.size() > 0) {
            encodeSeverityMessages(context, messages, FacesMessage.SEVERITY_WARN, severityMessages);
        }

        severityMessages = msgs.get(FacesMessage.SEVERITY_INFO);
        if (severityMessages.size() > 0) {
            encodeSeverityMessages(context, messages, FacesMessage.SEVERITY_INFO, severityMessages);
        }
        super.encodeEnd(context, component);
    }

    private void encodeSeverityMessages(FacesContext facesContext, UIMessages uiMessages, Severity severity, List<FacesMessage> messages) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();

        String typeMessage = "";
        if (FacesMessage.SEVERITY_INFO.equals(severity)) {
            typeMessage = "info";
        } else if (FacesMessage.SEVERITY_WARN.equals(severity)) {
            typeMessage = "warning";
        } else if (FacesMessage.SEVERITY_ERROR.equals(severity)) {
            typeMessage = "error";
        } else if (FacesMessage.SEVERITY_FATAL.equals(severity)) {
            typeMessage = "error";
        }

        String mensagem = "";
        for (FacesMessage msg : messages) {
            String summary = msg.getSummary() != null ? msg.getSummary() : "";
            String detail = msg.getDetail() != null ? msg.getDetail() : summary;

            if (uiMessages.isShowSummary()) {
                mensagem += summary + "<br/>";
            }

            if (uiMessages.isShowDetail()) {
                mensagem += detail + "<br/>";
            }
        }

        writer.startElement("script", null);
        writer.writeText("showMessage('" + mensagem.replaceAll("\'", "\\\\'") + "','" + typeMessage + "');", null);
        writer.endElement("script");
    }
}
