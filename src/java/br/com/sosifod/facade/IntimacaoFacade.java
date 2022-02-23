package br.com.sosifod.facade;

import br.com.sosifod.bean.Cidade;
import br.com.sosifod.bean.Endereco;
import br.com.sosifod.bean.Intimacao;
import br.com.sosifod.bean.Oficial;
import br.com.sosifod.dao.IntimacaoDao;
import br.com.sosifod.dto.EnderecoDto;
import br.com.sosifod.dto.IntimacaoDto;
import br.com.sosifod.dto.OficialDto;
import br.com.sosifod.exception.DaoException;
import br.com.sosifod.exception.EnderecoException;
import br.com.sosifod.exception.IntimacaoException;
import br.com.sosifod.util.SosifodUtil;
import br.com.sosifod.validator.IntimacaoValidator;
import java.util.Date;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class IntimacaoFacade {

    private static final IntimacaoDao intimacaoDao = new IntimacaoDao();

    public static void cadastrarIntimacao(Intimacao intimacao) throws IntimacaoException, EnderecoException {
        try {
            if (intimacao == null) {
                throw new IntimacaoException("Intimação inválida");
            } else if (intimacao.getEndereco() == null) {
                throw new IntimacaoException("Endereço não é válido");
            } else {
                intimacao.setCpf((intimacao.getCpf() != null) ? intimacao.getCpf().replace(".", "").replace("-", "") : null);
                intimacao.setNome((intimacao.getNome() != null) ? intimacao.getNome().trim().toUpperCase() : null);
                intimacao.getEndereco().setRua((intimacao.getEndereco().getRua() != null) ? intimacao.getEndereco().getRua().trim().toUpperCase() : null);
                intimacao.getEndereco().setBairro((intimacao.getEndereco().getBairro() != null) ? intimacao.getEndereco().getBairro().trim().toUpperCase() : null);
                intimacao.getEndereco().setCep((intimacao.getEndereco().getCep() != null) ? intimacao.getEndereco().getCep().replace(".", "").replace("-", "").trim() : null);
                intimacao.getEndereco().setComplemento((intimacao.getEndereco().getComplemento() != null) ? intimacao.getEndereco().getComplemento().trim().toUpperCase() : null);
                intimacao.setDataHora(new Date());
                IntimacaoValidator.validaIntimacao(intimacao);
                IntimacaoValidator.validaEndereco(intimacao.getEndereco());
                intimacaoDao.cadastrarIntimacao(intimacao);
            }
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao cadastrar nova intimação");
        }
    }

    public static Intimacao cadastrarIntimacaoDto(IntimacaoDto intimacaoDto) throws IntimacaoException {
        try {
            Intimacao intimacao = new Intimacao();
            intimacao.setCpf((intimacaoDto.getCpf() != null) ? intimacaoDto.getCpf() : null);
            intimacao.setDataHora((intimacaoDto.getDataHora() != null) ? intimacaoDto.getDataHora() : null);
            intimacao.setNome((intimacaoDto.getNome() != null) ? intimacaoDto.getNome() : null);
            intimacao.setProcesso(intimacaoDto.getProcesso());

            intimacao.setOficial(new Oficial());
            intimacao.getOficial().setId((intimacaoDto.getOficial() != null) ? intimacaoDto.getOficial().getId() : 0);

            intimacao.setEndereco(new Endereco());
            intimacao.getEndereco().setBairro((intimacaoDto.getEndereco() != null) ? intimacaoDto.getEndereco().getBairro() : null);
            intimacao.getEndereco().setCep((intimacaoDto.getEndereco() != null) ? intimacaoDto.getEndereco().getCep() : null);

            intimacao.getEndereco().setCidade(new Cidade());
            intimacao.getEndereco().getCidade().setId(((intimacaoDto.getEndereco() != null) && (intimacaoDto.getEndereco().getCidade() != 0)) ? intimacaoDto.getEndereco().getCidade() : 0);

            intimacao.getEndereco().setComplemento((intimacaoDto.getEndereco() != null) ? intimacaoDto.getEndereco().getComplemento() : null);
            intimacao.getEndereco().setNumero((intimacaoDto.getEndereco() != null) ? intimacaoDto.getEndereco().getNumero() : 0);
            intimacao.getEndereco().setRua((intimacaoDto.getEndereco() != null) ? intimacaoDto.getEndereco().getRua() : null);

            IntimacaoValidator.validaIntimacao(intimacao);
            IntimacaoValidator.validaEndereco(intimacao.getEndereco());
            intimacaoDao.cadastrarIntimacao(intimacao);
            return intimacao;
        } catch (DaoException | EnderecoException e) {
            e.printStackTrace(System.out);
            throw new IntimacaoException(e.getMessage());
        }
    }

    public static List<Intimacao> listaIntimacaoOficial(int idOficial) {
        try {
            return intimacaoDao.listaIntimacaoOficial(idOficial);
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao listar intimacoes por oficial");
            return null;
        }
    }

    public static List<Intimacao> listaIntimacao() {
        try {
            return intimacaoDao.listaIntimacao();
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao listar intimacoes");
            return null;
        }
    }

    public static Intimacao buscaIntimacaoId(int id) {
        try {
            return intimacaoDao.buscaIntimacaoId(id);
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao buscar intimacao por id");
            return null;
        }
    }

    public static void apagarIntimacao(Intimacao intimacao) {
        try {
            intimacaoDao.apagarIntimacao(intimacao);
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao apagar intimacao");
        }
    }

    public static void atualizarIntimacao(Intimacao intimacao) throws IntimacaoException {
        try {
            Intimacao intVerificar = intimacaoDao.buscaIntimacaoId(intimacao.getId());
            if (intVerificar != null) {
                if ((intVerificar.getStatus() != null) && (intVerificar.getStatus())) {
                    throw new IntimacaoException("Intimação já foi executada");
                }
            }
            intimacao.setDataHoraExec(new Date());
            intimacaoDao.atualizarIntimacao(intimacao);

            IntimacaoDto intimacaoDto = new IntimacaoDto();
            intimacaoDto.setCpf(intimacao.getCpf());
            intimacaoDto.setDataHora(intimacao.getDataHora());
            intimacaoDto.setDataHoraExec(intimacao.getDataHoraExec());
            intimacaoDto.setId(intimacao.getId());
            intimacaoDto.setNome(intimacao.getNome());
            intimacaoDto.setProcesso(intimacao.getProcesso());
            intimacaoDto.setStatus(intimacao.getStatus());

            intimacaoDto.setOficial(new OficialDto());
            intimacaoDto.getOficial().setId(intimacao.getOficial().getId());
            intimacaoDto.getOficial().setNome(intimacao.getOficial().getNome());

            intimacaoDto.setEndereco(new EnderecoDto());
            intimacaoDto.getEndereco().setBairro(intimacao.getEndereco().getBairro());
            intimacaoDto.getEndereco().setCep(intimacao.getEndereco().getCep());
            intimacaoDto.getEndereco().setCidade(intimacao.getEndereco().getCidade().getId());
            intimacaoDto.getEndereco().setComplemento(intimacao.getEndereco().getComplemento());
            intimacaoDto.getEndereco().setNumero(intimacao.getEndereco().getNumero());
            intimacaoDto.getEndereco().setRua(intimacao.getEndereco().getRua());

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target("http://localhost:8080/sijoga/webresources/sijoga/execIntimacao");
            Response resp = target.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(intimacaoDto, MediaType.APPLICATION_JSON));

            if ((resp.getStatus() != 200) && (resp.getStatus() != 201)) {
                String msg = "Houve um problema ao criar nova fase no processo: ";
                msg += SosifodUtil.statusHttp(resp.getStatus());
                throw new IntimacaoException(msg);
            }
        } catch (DaoException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao atualizar intimação");
        }
    }
}
