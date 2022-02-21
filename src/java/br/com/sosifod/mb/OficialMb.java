package br.com.sosifod.mb;

import br.com.sosifod.bean.Intimacao;
import br.com.sosifod.bean.Oficial;
import br.com.sosifod.exception.IntimacaoException;
import br.com.sosifod.exception.OficialException;
import br.com.sosifod.facade.IntimacaoFacade;
import br.com.sosifod.facade.OficialFacade;
import br.com.sosifod.util.SosifodUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class OficialMb implements Serializable {

    @Inject
    private LoginMb login;
    private Oficial oficial;
    private String confirmaSenha;
    private Boolean cadastroConcluido;
    private List<Intimacao> intimacoes;
    private Intimacao intimacao;

    @PostConstruct
    public void init() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (ctx.getViewRoot().getViewId().equals("/Oficial/VerIntimacao.xhtml")) {
                if (FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idIntimacao") == null) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/Oficial/InicioOficial.jsf");
                } else {
                    int idProcesso = (int) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idIntimacao");
                    this.intimacao = IntimacaoFacade.buscaIntimacaoId(idProcesso);
                }
            }
            this.oficial = new Oficial();
            if ((login != null) && (login.getOficial().getId() != 0)) {
                this.intimacoes = IntimacaoFacade.listaIntimacaoOficial(login.getOficial().getId());
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            String msg = "Problemas ao inicializar página " + FacesContext.getCurrentInstance().getViewRoot().getViewId();
            SosifodUtil.mensagemErroRedirecionamento(msg);
        }
    }

    public void cadastrarOficial() {
        FacesContext ctx = null;
        try {
            FacesMessage msg;
            ctx = FacesContext.getCurrentInstance();
            if (!this.oficial.getSenha().equals(this.confirmaSenha)) {
                msg = SosifodUtil.emiteMsg("Senha e confirmação não conferem", 2);
                ctx.addMessage(null, msg);
            } else {
                OficialFacade.cadastrarOficial(this.oficial);
                this.cadastroConcluido = true;
            }
        } catch (OficialException e) {
            if (ctx != null) {
                ctx.addMessage(null, SosifodUtil.emiteMsg(e.getMessage(), 2));
            } else {
                e.printStackTrace(System.out);
                SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao cadastrar um oficial");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao cadastrar um oficial");
        }
    }

    public String statusIntimacao(Intimacao intimacao) {
        String rtn;
        try {
            if (intimacao.getStatus() == null) {
                rtn = "PARA EXECUÇÃO";
            } else if (intimacao.getStatus() == false) {
                rtn = "EM EXECUÇÃO";
            } else {
                rtn = "CONCLUÍDO";
            }
            return rtn;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao obter o status da intimação");
            return null;
        }
    }

    public void verIntimacao(int id) {
        try {
            ExternalContext ctxExt = FacesContext.getCurrentInstance().getExternalContext();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idIntimacao", id);
            ctxExt.redirect(ctxExt.getRequestContextPath() + "/Oficial/VerIntimacao.jsf");
        } catch (IOException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao redirecionar página");
        }
    }

    public void atualizarIntimacao(Boolean opc) {
        try {
            this.intimacao.setStatus(opc);
            IntimacaoFacade.atualizarIntimacao(this.intimacao);
            ExternalContext ctxExt = FacesContext.getCurrentInstance().getExternalContext();
            ctxExt.redirect(ctxExt.getRequestContextPath() + "/Oficial/InicioOficial.jsf");
        } catch (IntimacaoException e) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (ctx != null) {
                ctx.addMessage(null, SosifodUtil.emiteMsg(e.getMessage(), 2));
            } else {
                e.printStackTrace(System.out);
                SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao atualizar intimação");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao atualizar intimação");
        }
    }

    public String imprimeCpf(String cpf) {
        return SosifodUtil.imprimeCPF(cpf);
    }

    public Oficial getOficial() {
        return oficial;
    }

    public void setOficial(Oficial oficial) {
        this.oficial = oficial;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public Boolean getCadastroConcluido() {
        return cadastroConcluido;
    }

    public void setCadastroConcluido(Boolean cadastroConcluido) {
        this.cadastroConcluido = cadastroConcluido;
    }

    public LoginMb getLogin() {
        return login;
    }

    public void setLogin(LoginMb login) {
        this.login = login;
    }

    public List<Intimacao> getIntimacoes() {
        return intimacoes;
    }

    public void setIntimacoes(List<Intimacao> intimacoes) {
        this.intimacoes = intimacoes;
    }

    public Intimacao getIntimacao() {
        return intimacao;
    }

    public void setIntimacao(Intimacao intimacao) {
        this.intimacao = intimacao;
    }
}
