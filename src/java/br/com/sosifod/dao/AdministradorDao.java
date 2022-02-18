package br.com.sosifod.dao;

import br.com.sosifod.bean.Administrador;
import br.com.sosifod.exception.DaoException;
import br.com.sosifod.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class AdministradorDao {
    public void cadastrarAdministrador(Administrador administrador) throws DaoException {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                session.beginTransaction();
                session.save(administrador);                
            } finally {
                session.getTransaction().commit();
                session.close();
            }
        } catch (HibernateException e) {
            throw new DaoException("****Problema ao cadastrar novo administrador [Hibernate]****", e);
        } catch (Exception e) {
            throw new DaoException("****Problema ao cadastrar novo administrador [DAO]****", e);
        }
    }     
    
    public Administrador buscaAdministradorCpf(String cpf) throws DaoException {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                session.beginTransaction();
                Query select = session.createQuery("FROM Administrador a WHERE a.cpf = :cpf");
                select.setParameter("cpf", cpf);
                Administrador administrador = (Administrador) select.uniqueResult();
                return administrador;                
            } finally {
                session.getTransaction().commit();
                session.close();
            }
        } catch (HibernateException e) {
            throw new DaoException("****Problema ao buscar administrador por CPF [Hibernate]****", e);
        } catch (Exception e) {
            throw new DaoException("****Problema ao buscar administrador por CPF [DAO]****", e);
        }
    }     
    
    public Administrador buscaAdministradorEmail(String email) throws DaoException {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                session.beginTransaction();
                Query select = session.createQuery("FROM Administrador a WHERE a.email = :email");
                select.setParameter("email", email);
                Administrador administrador = (Administrador) select.uniqueResult();
                return administrador;                
            } finally {
                session.getTransaction().commit();
                session.close();
            }
        } catch (HibernateException e) {
            throw new DaoException("****Problema ao buscar administrador por Email [Hibernate]****", e);
        } catch (Exception e) {
            throw new DaoException("****Problema ao buscar administrador por Email [DAO]****", e);
        }
    }     
}
