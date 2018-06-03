package com.supero.gerenciador.tarefas.dao;

import com.supero.gerenciador.tarefas.entity.Tarefa;
import com.supero.gerenciador.tarefas.persistence.GenericDao;
import java.util.Date;
import java.util.List;
import static java.util.Objects.isNull;
import org.jinq.orm.stream.JinqStream;

public class TarefaDao extends GenericDao<Tarefa> {

    public void save(Tarefa tarefa) {
        tarefa.setDataEdicao(new Date());
        if (isNull(tarefa.getId())) {
            tarefa.setDataInclusao(tarefa.getDataEdicao());
            add(tarefa);
        } else {
            edit(tarefa);
        }
    }
    
    public List<Tarefa> getAllAtivos() {
        JinqStream<Tarefa> query = getEntities().where(tarefa -> tarefa.getDataExclusao() == null);
        query = query.sortedBy(tarefa -> tarefa.getTitulo());
        return query.toList();
    }
}
