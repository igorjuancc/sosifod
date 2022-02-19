package br.com.sosifod.facade;

import br.com.sosifod.bean.Administrador;
import br.com.sosifod.bean.Oficial;
import br.com.sosifod.dao.LoginDao;
import br.com.sosifod.exception.DaoException;
import br.com.sosifod.util.Seguranca;
import br.com.sosifod.util.SosifodUtil;

public class LoginFacade {

    private static final LoginDao loginDao = new LoginDao();

    public static Administrador loginAdministrador(Administrador adm) {
        try {
            if (!Seguranca.isEmail(adm.getEmail()) || adm.getSenha() == null) {
                return null;
            } else {
                return loginDao.loginAdministrador(adm);
            }
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao efetuar login de administrador");
            return null;
        }
    }

    public static Oficial loginOficial(Oficial oficial) {
        try {
            if (!Seguranca.isEmail(oficial.getEmail()) || oficial.getSenha() == null) {
                return null;
            } else {
                return loginDao.loginOficial(oficial);
            }
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao efetuar login de oficial de justiça");
            return null;
        }
    }
}
