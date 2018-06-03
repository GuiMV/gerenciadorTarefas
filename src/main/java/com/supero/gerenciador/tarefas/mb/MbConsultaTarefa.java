package com.supero.gerenciador.tarefas.mb;

import com.supero.gerenciador.tarefas.constant.ViewPath;
import com.supero.gerenciador.tarefas.dao.TarefaDao;
import com.supero.gerenciador.tarefas.entity.Tarefa;
import com.supero.gerenciador.tarefas.enums.PROPERTY;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import java.util.List;

@Named
@ViewScoped
public class MbConsultaTarefa extends BasicMb {

    @Inject
    private TarefaDao tarefaDao;
    private List<Tarefa> tarefas;

    @PostConstruct
    public void init(){
        tarefas = tarefaDao.getAllAtivos();
    }

    public void editar(Tarefa tarefa){
        putOnFlash(PROPERTY.TAREFA_EDITAR.name(), tarefa.getId());
        redirectOnContextPath(ViewPath.CADASTRO_TAREFA);
    }

    public void excluir(Tarefa tarefa){
        tarefa.setDataExclusao(new Date());
        tarefaDao.edit(tarefa);
        tarefas.remove(tarefa);
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }
    
}
