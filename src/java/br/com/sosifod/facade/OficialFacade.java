package br.com.sosifod.facade;

import br.com.sosifod.bean.Oficial;
import br.com.sosifod.dao.OficialDao;
import br.com.sosifod.dto.OficialDto;
import br.com.sosifod.exception.DaoException;
import br.com.sosifod.exception.OficialException;
import br.com.sosifod.util.Seguranca;
import br.com.sosifod.util.SosifodUtil;
import br.com.sosifod.validator.UsuarioValidator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class OficialFacade {

    private static final OficialDao oficialDao = new OficialDao();

    public static void cadastrarOficial(Oficial oficial) throws OficialException {
        try {
            if (oficial == null) {
                throw new OficialException("Oficial de justiça inválido");
            } else {
                oficial.setCpf((oficial.getCpf() != null) ? oficial.getCpf().replace(".", "").replace("-", "") : null);
                oficial.setNome((oficial.getNome() != null) ? oficial.getNome().trim().toUpperCase() : null);
                oficial.setEmail((oficial.getEmail() != null) ? oficial.getEmail().trim() : null);
                UsuarioValidator.validaOficial(oficial);
                oficial.setSenha(Seguranca.md5(oficial.getSenha()));
                if ((buscaOficialCpf(oficial.getCpf()) != null) || (AdministradorFacade.buscaAdministradorCpf(oficial.getCpf()) != null)) {
                    throw new OficialException("CPF já cadastrado");
                }
                if ((buscaOficialEmail(oficial.getEmail()) != null) || (AdministradorFacade.buscaAdministradorEmail(oficial.getEmail()) != null)) {
                    throw new OficialException("Email já cadastrado");
                }
                oficialDao.cadastrarOficial(oficial);
            }
        } catch (DaoException | NoSuchAlgorithmException e) {
            e.printStackTrace(System.out);
            String msg = "Houve um problema ao cadastrar os dados do oficial de justiça";
            SosifodUtil.mensagemErroRedirecionamento(msg);
        }
    }

    public static Oficial buscaOficialCpf(String cpf) {
        try {
            return oficialDao.buscaOficialCpf(cpf);
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            String msg = "Houve um problema ao buscar oficial por CPF";
            SosifodUtil.mensagemErroRedirecionamento(msg);
            return null;
        }
    }

    public static Oficial buscaOficialEmail(String email) {
        try {
            return oficialDao.buscaOficialEmail(email);
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            String msg = "Houve um problema ao buscar oficial por email";
            SosifodUtil.mensagemErroRedirecionamento(msg);
            return null;
        }
    }

    public static List<OficialDto> listaOficiaisDto() throws OficialException {
        try {
            List<Oficial> oficiais = oficialDao.listaOficiais();
            List<OficialDto> oficiaisDto = new ArrayList<>();

            oficiais.stream().map((o) -> {
                OficialDto oD = new OficialDto();
                oD.setCpf(o.getCpf());
                oD.setEmail(o.getEmail());
                oD.setId(o.getId());
                oD.setNome(o.getNome());
                return oD;
            }).forEachOrdered((oD) -> {
                oficiaisDto.add(oD);
            });
            return oficiaisDto;
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            throw new OficialException(e.getMessage());
        }
    }
}
