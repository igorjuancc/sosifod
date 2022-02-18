package br.com.sosifod.validator;

import br.com.sosifod.bean.Endereco;
import br.com.sosifod.bean.Intimacao;
import br.com.sosifod.exception.EnderecoException;
import br.com.sosifod.exception.IntimacaoException;
import br.com.sosifod.util.SosifodUtil;

public class IntimacaoValidator {

    public static void validaIntimacao(Intimacao intimacao) throws IntimacaoException {
        String mensagem = "";
        if (intimacao == null) {
            throw new IntimacaoException("Intimação inválida");
        } else {
            if (intimacao.getOficial() == null) {
                mensagem += "Oficial de justiça inválido<br/>";
            }
            if (intimacao.getProcesso() == 0) {
                mensagem += "Número do processo não é válido<br/>";
            }
            if ((intimacao.getCpf() == null) || (intimacao.getCpf().equals("")) || (SosifodUtil.isCPF(intimacao.getCpf()))) {
                mensagem += "CPF do intimado não é válido<br/>";
            }
            if ((intimacao.getNome() == null) || (intimacao.getNome().equals(""))) {
                mensagem += "Nome do intimado não é válido<br/>";
            }
            if (intimacao.getDataHora() == null) {
                mensagem += "Hora da intimação não é válida<br/>";
            }
        }
        if (!mensagem.equals("")) {
            throw new IntimacaoException(mensagem);
        }
    }

    public static void validaEndereco(Endereco endereco) throws EnderecoException {
        String mensagem = "";
        if (endereco == null) {
            throw new EnderecoException("Endereço inválido");
        } else {
            if (endereco.getNumero() == 0) {
                mensagem += "Número do endereço de intimação não é válido<br/>";
            }
            if ((endereco.getCep() == null) || (endereco.getCep().equals(""))) {
                mensagem += "CEP de intimação não é válido<br/>";
            }
            if ((endereco.getRua() == null) || (endereco.getRua().equals(""))) {
                mensagem += "Rua de endereço de intimação não é válido<br/>";
            }
            if ((endereco.getBairro() == null) || (endereco.getBairro().equals(""))) {
                mensagem += "Bairro de endereço de intimação não é válido<br/>";
            }
            if ((endereco.getCidade() == null) || (endereco.getCidade().getId() == 0)) {
                mensagem += "Cidade de endereço de intimação não é válido<br/>";
            }
        }
        if (!mensagem.equals("")) {
            throw new EnderecoException(mensagem);
        }
    }
}
