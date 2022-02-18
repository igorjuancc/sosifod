package br.com.sosifod.dao;

import br.com.sosifod.bean.Intimacao;
import br.com.sosifod.exception.DaoException;
import br.com.sosifod.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class IntimacaoDao {

    public void cadastrarIntimacao(Intimacao intimacao) throws DaoException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(intimacao);
        } catch (HibernateException e) {
            throw new DaoException("****Problema ao cadastrar nova intimacao [Hibernate]****", e);
        } catch (Exception e) {
            throw new DaoException("****Problema ao cadastrar nova intimacao [DAO]****", e);
        } finally {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public void atualizarIntimacao(Intimacao intimacao) throws DaoException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(intimacao);
        } catch (HibernateException e) {
            throw new DaoException("****Problema ao atualizar intimacao [Hibernate]****", e);
        } catch (Exception e) {
            throw new DaoException("****Problema ao atualizar intimacao [DAO]****", e);
        } finally {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public List<Intimacao> listaIntimacaoOficial(int idOficial) throws DaoException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query select = session.createQuery("SELECT i FROM Intimacao i JOIN i.oficial WHERE i.oficial.id = :idOficial");
            select.setParameter("idOficial", idOficial);
            List<Intimacao> intimacoes = select.list();
            return intimacoes;
        } catch (HibernateException e) {
            throw new DaoException("****Problema ao listar intimacoes por oficial [Hibernate]****", e);
        } catch (Exception e) {
            throw new DaoException("****Problema ao listar intimacoes por oficial [DAO]****", e);
        } finally {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public List<Intimacao> listaIntimacao() throws DaoException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query select = session.createQuery("SELECT DISTINCT i FROM Intimacao i");
            List<Intimacao> intimacoes = select.list();
            return intimacoes;
        } catch (HibernateException e) {
            throw new DaoException("****Problema ao listar intimacoes  [Hibernate]****", e);
        } catch (Exception e) {
            throw new DaoException("****Problema ao listar intimacoes  [DAO]****", e);
        } finally {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public Intimacao buscaIntimacaoId(int id) throws DaoException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query select = session.createQuery("SELECT i FROM Intimacao i WHERE i.id = :id");
            select.setParameter("id", id);
            Intimacao intimacao = (Intimacao) select.uniqueResult();
            return intimacao;
        } catch (HibernateException e) {
            throw new DaoException("****Problema ao buscar intimacao por id [Hibernate]****", e);
        } catch (Exception e) {
            throw new DaoException("****Problema ao buscar intimacao por id [DAO]****", e);
        } finally {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public void apagarIntimacao(Intimacao intimacao) throws DaoException {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(intimacao);
        } catch (HibernateException e) {
            throw new DaoException("****Problema ao apagar intimacao [Hibernate]****", e);
        } catch (Exception e) {
            throw new DaoException("****Problema ao apagar intimacao [DAO]****", e);
        } finally {
            if (session != null) {
                session.getTransaction().commit();
                session.close();
            }
        }
    }
}
