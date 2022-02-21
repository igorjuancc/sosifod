package br.com.sosifod.util;

import br.com.sosifod.mb.LoginMb;
import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

public class VerificaLogin implements PhaseListener {

    @Inject
    private LoginMb usuario;

    @Override
    public void afterPhase(PhaseEvent event) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            String pagina = ctx.getViewRoot().getViewId();                           //Nome da pagina atual
            ExternalContext ctxExt = FacesContext.getCurrentInstance().getExternalContext();
            int opcCase = 0;

            if (usuario.getAdm().getId() != 0) {
                opcCase = 1;
            } else if (usuario.getOficial().getId() != 0) {
                opcCase = 2;
            }

            switch (opcCase) {
                case 0:
                    if ((!pagina.equals("/index.xhtml")) && (!pagina.equals("/CadastroAdministrador.xhtml")) && (!pagina.equals("/ErroPage.xhtml"))) {
                        ctxExt.redirect(ctxExt.getRequestContextPath() + "/index.jsf");
                    }
                    break;
                case 1:
                    if ((!pagina.equals("/Administrador/InicioAdministrador.xhtml")) && (!pagina.equals("/Administrador/CadastroOficial.xhtml")) && (!pagina.equals("/Administrador/VerIntimacao.xhtml")) && (!pagina.equals("/ErroPage.xhtml"))) {
                        ctxExt.redirect(ctxExt.getRequestContextPath() + "/Administrador/InicioAdministrador.jsf");
                    }
                    break;
                case 2:
                    if ((!pagina.equals("/Oficial/InicioOficial.xhtml")) && (!pagina.equals("/Oficial/VerIntimacao.xhtml")) && (!pagina.equals("/ErroPage.xhtml"))) {
                        ctxExt.redirect(ctxExt.getRequestContextPath() + "/Oficial/InicioOficial.jsf");
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            String msg = "Problemas ao verificar login";
            SosifodUtil.mensagemErroRedirecionamento(msg);
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
