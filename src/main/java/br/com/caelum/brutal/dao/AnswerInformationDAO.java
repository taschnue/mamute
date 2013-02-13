package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AnswerInformationDAO {

    private final Session session;

    public AnswerInformationDAO(Session session) {
        this.session = session;
    }
    
    @SuppressWarnings("unchecked")
    public UpdatablesAndPendingHistory pendingByUpdatables() {
        String hql = "select answer, answer_info from Answer answer " +
                "join answer.history answer_info " +
                "where answer_info.status = :pending order by answer_info.createdAt asc";
        Query query = session.createQuery(hql);
        query.setParameter("pending", UpdateStatus.PENDING);
        List<Object[]> results = query.list();
        UpdatablesAndPendingHistory pending = new UpdatablesAndPendingHistory(results);
        return pending;
    }

    @SuppressWarnings("unchecked")
    public List<AnswerInformation> pendingFor(Long answerId) {
        String hql = "select answer_info from AnswerInformation answer_info " +
        		"join answer_info.answer answer " +
        		"where answer.id=:id and answer_info.status=:pending";
        Query query = session.createQuery(hql);
        return query.setParameter("id", answerId)
            .setParameter("pending", UpdateStatus.PENDING)
            .list();
    }

}
