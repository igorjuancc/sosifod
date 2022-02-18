package br.com.sosifod.facade;

import br.com.sosifod.bean.Administrador;
import br.com.sosifod.dao.AdministradorDao;
import br.com.sosifod.exception.AdministradorException;
import br.com.sosifod.exception.DaoException;
import br.com.sosifod.util.Seguranca;
import br.com.sosifod.util.SosifodUtil;
import br.com.sosifod.validator.UsuarioValidator;
import java.security.NoSuchAlgorithmException;

public class AdministradorFacade {

    private static final AdministradorDao administradorDao = new AdministradorDao();

    public static void cadastrarAdministrador(Administrador administrador) throws AdministradorException {
        try {
            if (administrador != null) {
                administrador.setCpf((administrador.getCpf() != null) ? administrador.getCpf().replace(".", "").replace("-", "") : "");
                administrador.setNome((administrador.getNome() != null) ? administrador.getNome().trim().toUpperCase() : "");
                administrador.setFone((administrador.getFone() != null) ? administrador.getFone().replace("(", "").replace(")", "").replace("-", "") : null);
                administrador.setEmail((administrador.getEmail() != null) ? administrador.getEmail().trim() : "");
                administrador.setSenha((administrador.getSenha() != null) ? administrador.getSenha() : null);
            }
            UsuarioValidator.validaAdministrador(administrador);
            administrador.setSenha(Seguranca.md5(administrador.getSenha()));
            if ((buscaAdministradorCpf(administrador.getCpf()) != null) || (OficialFacade.buscaOficialCpf(administrador.getCpf()) != null)) {
                throw new AdministradorException("CPF já cadastrado");
            }
            if ((buscaAdministradorEmail(administrador.getEmail()) != null) || (OficialFacade.buscaOficialEmail(administrador.getEmail()) != null)) {
                throw new AdministradorException("Email já cadastrado");
            }
            administradorDao.cadastrarAdministrador(administrador);
        } catch (DaoException | NoSuchAlgorithmException e) {
            e.printStackTrace(System.out);
            String msg = "Houve um problema ao cadastrar os dados de administrador";
            SosifodUtil.mensagemErroRedirecionamento(msg);
        }
    }

    public static Administrador buscaAdministradorCpf(String cpf) {
        try {
            return administradorDao.buscaAdministradorCpf(cpf);
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            String msg = "Houve um problema ao buscar administrador por CPF";
            SosifodUtil.mensagemErroRedirecionamento(msg);
            return null;
        }
    }

    public static Administrador buscaAdministradorEmail(String email) {
        try {
            return administradorDao.buscaAdministradorEmail(email);
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            String msg = "Houve um problema ao buscar administrador por email";
            SosifodUtil.mensagemErroRedirecionamento(msg);
            return null;
        }
    }
}
