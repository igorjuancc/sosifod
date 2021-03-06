package br.com.sosifod.mb;

import br.com.sosifod.bean.Administrador;
import br.com.sosifod.bean.Intimacao;
import br.com.sosifod.exception.AdministradorException;
import br.com.sosifod.facade.AdministradorFacade;
import br.com.sosifod.facade.IntimacaoFacade;
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
public class AdministradorMb implements Serializable {

    @Inject
    private LoginMb login;
    private String confirmaSenha;
    private Boolean cadastroConcluido;
    private Administrador administrador;
    private Intimacao intimacao;
    private List<Intimacao> intimacoes;

    @PostConstruct
    public void init() {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            if (ctx.getViewRoot().getViewId().equals("/Administrador/VerIntimacao.xhtml")) {
                if (FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idIntimacao") == null) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/Oficial/InicioOficial.jsf");
                } else {
                    int idProcesso = (int) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idIntimacao");
                    this.intimacao = IntimacaoFacade.buscaIntimacaoId(idProcesso);
                }
            }
            this.administrador = new Administrador();
            if ((login != null) && (login.getAdm().getId() != 0)) {
                this.intimacoes = IntimacaoFacade.listaIntimacao();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            String msg = "Problemas ao inicializar p??gina " + FacesContext.getCurrentInstance().getViewRoot().getViewId();
            SosifodUtil.mensagemErroRedirecionamento(msg);
        }
    }

    public void cadastrarAdministrador() {
        FacesContext ctx = null;
        try {
            FacesMessage msg;
            ctx = FacesContext.getCurrentInstance();
            if (!this.administrador.getSenha().equals(this.confirmaSenha)) {
                msg = SosifodUtil.emiteMsg("Senha e confirma????o n??o conferem", 2);
                ctx.addMessage(null, msg);
            } else {
                AdministradorFacade.cadastrarAdministrador(this.administrador);
                this.cadastroConcluido = true;
            }
        } catch (AdministradorException e) {
            if (ctx != null) {
                ctx.addMessage(null, SosifodUtil.emiteMsg(e.getMessage(), 2));
            } else {
                e.printStackTrace(System.out);
                SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao cadastrar um administrador");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao cadastrar um administrador");
        }
    }

    public void verIntimacao(int id) {
        try {
            ExternalContext ctxExt = FacesContext.getCurrentInstance().getExternalContext();
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("idIntimacao", id);
            ctxExt.redirect(ctxExt.getRequestContextPath() + "/Administrador/VerIntimacao.jsf");
        } catch (IOException e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao redirecionar p??gina");
        }
    }

    public String statusIntimacao(Intimacao intimacao) {
        String rtn;
        try {
            if (intimacao.getStatus() == null) {
                rtn = "PARA EXECU????O";
            } else if (intimacao.getStatus() == false) {
                rtn = "EM EXECU????O";
            } else {
                rtn = "CONCLU??DO";
            }
            return rtn;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao obter o status da intima????o");
            return null;
        }
    }

    public void apagarIntimacao(Intimacao intimacao) {
        try {
            IntimacaoFacade.apagarIntimacao(intimacao);
            this.intimacoes = IntimacaoFacade.listaIntimacao();
            FacesContext ctx = FacesContext.getCurrentInstance();
            FacesMessage msg = SosifodUtil.emiteMsg("Intima????o deletada", 1);
            ctx.addMessage(null, msg);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            SosifodUtil.mensagemErroRedirecionamento("Houve um problema ao apagar uma intima????o");
        }
    }

    public String imprimeCpf(String cpf) {
        return SosifodUtil.imprimeCPF(cpf);
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Boolean getCadastroConcluido() {
        return cadastroConcluido;
    }

    public void setCadastroConcluido(Boolean cadastroConcluido) {
        this.cadastroConcluido = cadastroConcluido;
    }

    public Intimacao getIntimacao() {
        return intimacao;
    }

    public void setIntimacao(Intimacao intimacao) {
        this.intimacao = intimacao;
    }

    public List<Intimacao> getIntimacoes() {
        return intimacoes;
    }

    public void setIntimacoes(List<Intimacao> intimacoes) {
        this.intimacoes = intimacoes;
    }
}
