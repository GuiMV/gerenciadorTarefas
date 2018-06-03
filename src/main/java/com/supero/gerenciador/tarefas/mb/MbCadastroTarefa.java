package com.supero.gerenciador.tarefas.mb;

import com.supero.gerenciador.tarefas.dao.TarefaDao;
import com.supero.gerenciador.tarefas.entity.Tarefa;
import com.supero.gerenciador.tarefas.enums.PROPERTY;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;

import static java.util.Objects.isNull;

@Named
@ViewScoped
public class MbCadastroTarefa extends BasicMb {

    @Inject
    private TarefaDao tarefaDao;
    private Tarefa tarefa;

    @PostConstruct
    public void init(){
        Long idTarefa = getOnFlash(PROPERTY.TAREFA_EDITAR.name(), Long.class);
        if(isNull(idTarefa)){
            novaTarefa();
        } else {
            tarefa = tarefaDao.get(idTarefa);
        }
    }

    private void novaTarefa() {
        tarefa = new Tarefa();
    }

    public void salvar(){
        tarefaDao.save(tarefa);
        novaTarefa();
        addInfoMessage("Tarefa salva com sucesso!");
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

}
