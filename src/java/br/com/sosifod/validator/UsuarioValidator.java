package br.com.sosifod.validator;

import br.com.sosifod.bean.Administrador;
import br.com.sosifod.exception.AdministradorException;
import br.com.sosifod.util.Seguranca;
import br.com.sosifod.util.SosifodUtil;

public class UsuarioValidator {

    public static void validaAdministrador(Administrador adm) throws AdministradorException {
        String mensagem = "";

        if ((adm.getNome() == null)
                || (adm.getNome().isEmpty())
                || (adm.getNome().trim().equals(""))) {
            mensagem += "Nome inválido<br/>";
        } else if (adm.getNome().length() > 100) {
            mensagem += "O nome não pode ultrapassar [100] caracteres<br/>";
        }
        if (!SosifodUtil.isCPF(adm.getCpf())) {
            mensagem += "CPF inválido<br/>";
        }
        if ((adm.getEmail() != null)
                && (!adm.getEmail().isEmpty())
                && (!adm.getEmail().trim().equals(""))) {
            if (!Seguranca.isEmail(adm.getEmail())) {
                mensagem += "Email inválido<br/>";
            }
        }
        if ((adm.getFone() != null) && (!adm.getFone().trim().isEmpty())) {
            if ((adm.getFone().length() < 10) && (adm.getFone().length() > 11)) {
                mensagem += "Telefone inválido<br/>";
            }
        }
        if ((adm.getSenha() == null)
                || (adm.getSenha().isEmpty())
                || (adm.getSenha().trim().equals(""))) {
            mensagem += "Senha inválida<br/>";
        } else {
            if (adm.getSenha().length() < 5) {
                mensagem += "Senha inválida, Mínimo [5] caracteres<br/>";
            }
            if (adm.getSenha().length() > 100) {
                mensagem += "Senha inválida, Máximo [100] caracteres<br/>";
            }
        }
        if (!mensagem.equals("")) {
            throw new AdministradorException(mensagem);
        }
    }
}
